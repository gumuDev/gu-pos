# Arquitectura Hexagonal — Backend

## Concepto

El dominio de negocio no conoce ningún framework, base de datos, ni protocolo de comunicación. Se comunica con el mundo exterior solo a través de **puertos** (interfaces). Los **adaptadores** implementan esos puertos con tecnologías concretas.

```
          [ REST Controller ]   [ Scheduler ]   [ Event Listener ]
                  │                   │                 │
                  ▼                   ▼                 ▼
         ┌────────────────────────────────────────────────┐
         │              PUERTOS DE ENTRADA (in/)           │
         │         SaleUseCase  │  ProductUseCase  │ ...   │
         ├────────────────────────────────────────────────┤
         │                                                │
         │                  DOMINIO                       │
         │         (lógica de negocio pura)               │
         │                                                │
         ├────────────────────────────────────────────────┤
         │              PUERTOS DE SALIDA (out/)           │
         │      SaleRepository  │  ProductRepository │ ...  │
         └────────────────────────────────────────────────┘
                  │                   │                 │
                  ▼                   ▼                 ▼
         [ JPA Repository ]  [ Supabase Storage ]  [ Email Service ]
```

---

## Estructura de Carpetas

```
src/main/java/com/pos/
│
├── shared/                          ← Utilidades compartidas entre features
│   ├── domain/
│   │   └── TenantId.java            ← Value object compartido
│   └── infrastructure/
│       ├── security/                ← JWT filter, extracción de tenant
│       └── config/                  ← Spring config global
│
├── sale/                            ← Feature: Ventas
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Sale.java
│   │   │   ├── SaleItem.java
│   │   │   └── Money.java           ← Value object
│   │   ├── port/
│   │   │   ├── in/
│   │   │   │   ├── CreateSaleUseCase.java
│   │   │   │   └── GetSaleHistoryUseCase.java
│   │   │   └── out/
│   │   │       ├── SaleRepository.java
│   │   │       └── StockPort.java   ← Para descontar stock al vender
│   │   └── service/
│   │       └── SaleService.java     ← Implementa los casos de uso
│   └── infrastructure/
│       ├── adapter/
│       │   ├── in/
│       │   │   └── web/
│       │   │       ├── SaleController.java
│       │   │       └── dto/
│       │   │           ├── CreateSaleRequest.java
│       │   │           └── SaleResponse.java
│       │   └── out/
│       │       └── persistence/
│       │           ├── SaleRepositoryImpl.java
│       │           └── entity/
│       │               └── SaleEntity.java   ← JPA Entity
│       └── config/
│           └── SaleBeanConfig.java
│
├── product/                         ← Feature: Productos
├── inventory/                       ← Feature: Inventario
├── promotion/                       ← Feature: Promociones
├── branch/                          ← Feature: Sucursales
├── cashier/                         ← Feature: Cajeros
├── report/                          ← Feature: Reportes
└── tenant/                          ← Feature: Multi-tenant (SaaS admin)
```

---

## Reglas de Dependencia (Estrictas)

```
✅ Controller      →  Puerto IN (interfaz)
✅ Service         →  Puerto OUT (interfaz)
✅ Repository Impl →  Puerto OUT (implementa)
✅ Domain Model    →  nada externo

❌ Controller      →  Service directamente
❌ Domain          →  Spring / JPA / cualquier framework
❌ Service         →  Repository Impl directamente
❌ Feature A       →  Feature B (comunicación via puertos o eventos)
```

---

## Ejemplo Completo — Feature: Sale

### Puerto de entrada (caso de uso)
```java
// sale/domain/port/in/CreateSaleUseCase.java
public interface CreateSaleUseCase {
    Sale createSale(CreateSaleCommand command);
}

public record CreateSaleCommand(
    TenantId tenantId,
    BranchId branchId,
    UserId cashierId,
    List<SaleItemCommand> items
) {}
```

### Modelo de dominio
```java
// sale/domain/model/Sale.java
public class Sale {
    private final SaleId id;
    private final TenantId tenantId;
    private final BranchId branchId;
    private final List<SaleItem> items;
    private final Money total;
    private final LocalDateTime createdAt;

    // Sin anotaciones JPA, sin Spring — puro Java
    public static Sale create(TenantId tenantId, BranchId branchId, List<SaleItem> items) {
        Money total = items.stream()
            .map(SaleItem::subtotal)
            .reduce(Money.ZERO, Money::add);
        return new Sale(SaleId.generate(), tenantId, branchId, items, total, LocalDateTime.now());
    }
}
```

### Puerto de salida (repositorio)
```java
// sale/domain/port/out/SaleRepository.java
public interface SaleRepository {
    Sale save(Sale sale);
    List<Sale> findByTenantAndBranch(TenantId tenantId, BranchId branchId, DateRange range);
}
```

### Servicio (implementa caso de uso)
```java
// sale/domain/service/SaleService.java
@Service
public class SaleService implements CreateSaleUseCase {

    private final SaleRepository saleRepository;
    private final StockPort stockPort;

    public SaleService(SaleRepository saleRepository, StockPort stockPort) {
        this.saleRepository = saleRepository;
        this.stockPort = stockPort;
    }

    @Override
    @Transactional
    public Sale createSale(CreateSaleCommand command) {
        List<SaleItem> items = mapItems(command.items());
        Sale sale = Sale.create(command.tenantId(), command.branchId(), items);

        // Descontar stock automáticamente (via puerto, no implementación concreta)
        stockPort.decreaseStockForSale(sale);

        return saleRepository.save(sale);
    }
}
```

### Controller (adaptador de entrada)
```java
// sale/infrastructure/adapter/in/web/SaleController.java
@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

    private final CreateSaleUseCase createSaleUseCase;

    public SaleController(CreateSaleUseCase createSaleUseCase) {
        this.createSaleUseCase = createSaleUseCase;
    }

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(
            @RequestBody CreateSaleRequest request,
            @AuthenticationPrincipal JwtPrincipal principal) {

        CreateSaleCommand command = SaleMapper.toCommand(request, principal.tenantId());
        Sale sale = createSaleUseCase.createSale(command);
        return ResponseEntity.ok(SaleMapper.toResponse(sale));
    }
}
```

### Repositorio (adaptador de salida)
```java
// sale/infrastructure/adapter/out/persistence/SaleRepositoryImpl.java
@Repository
public class SaleRepositoryImpl implements SaleRepository {

    private final SaleJpaRepository jpaRepository;

    @Override
    public Sale save(Sale sale) {
        SaleEntity entity = SaleMapper.toEntity(sale);
        SaleEntity saved = jpaRepository.save(entity);
        return SaleMapper.toDomain(saved);
    }

    @Override
    public List<Sale> findByTenantAndBranch(TenantId tenantId, BranchId branchId, DateRange range) {
        return jpaRepository
            .findByTenantIdAndBranchIdAndCreatedAtBetween(
                tenantId.value(), branchId.value(),
                range.start(), range.end())
            .stream()
            .map(SaleMapper::toDomain)
            .toList();
    }
}
```

---

## Cambio de Base de Datos

Si se necesita migrar de PostgreSQL a otro motor:

1. Crear nueva implementación de los puertos `out/` (ej: `SaleMongoRepositoryImpl`)
2. Actualizar `@Bean` en `SaleBeanConfig.java` para inyectar la nueva implementación
3. El dominio, servicio y controller **no se modifican**

Esto es exactamente el valor de la arquitectura hexagonal.
