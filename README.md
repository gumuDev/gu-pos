# POS SaaS — Sistema de Punto de Venta Multiplataforma

Sistema SaaS de punto de venta para retail general (restaurantes, tiendas, etc.) con soporte nativo para iOS, Android y Web. Funciona en modo local (gratuito) y modo cloud (pago), con sincronización automática entre ambos.

---

## Proyectos del Monorepo

```
pos-saas/
├── backend/       ← Spring Boot + Arquitectura Hexagonal (Java 21)
├── web/           ← Next.js 14 + Refine + Ant Design (Admin SaaS + Admin negocio)
├── mobile/        ← React Native + Expo (iOS + Android + Web)
└── docs/          ← Arquitectura, base de datos, decisiones técnicas
```

---

## Stack Tecnológico

### Backend (`/backend`)
- **Spring Boot 3** — framework principal
- **Java 21** — lenguaje, con virtual threads
- **Arquitectura Hexagonal** — dominio aislado, puertos y adaptadores
- **PostgreSQL** — base de datos cloud (hosteado en Supabase)
- **JWT** — autenticación propia, roles: `saas_admin`, `business_admin`, `cashier`
- **Flyway** — migraciones de base de datos versionadas
- **Docker** — contenedores para desarrollo y producción

### Frontend Web (`/web`)
- **Next.js 14** (App Router) — framework principal
- **Refine** + **Ant Design** — panel admin SaaS y panel negocio
- **Tailwind CSS** — estilos utilitarios
- **next-intl** — i18n (español default, inglés soportado)

### App Móvil (`/mobile`)
- **React Native + Expo** — iOS, Android y Web desde un solo codebase
- **expo-sqlite** — base de datos local (modo offline)
- **WatermelonDB** — ORM local con sync integrado
- **NativeWind** — Tailwind CSS para React Native
- **Expo EAS Build** — compilación y distribución en la nube
- **react-native-thermal-receipt-printer** — impresión BT/WiFi/USB

### Infraestructura
- **Supabase** — PostgreSQL hosteado + Storage (imágenes de productos)
- **Stripe** — suscripciones y pagos web
- **RevenueCat** — in-app purchases iOS/Android (upgrade a cloud)

---

## Modos de Operación

### Modo Local (Gratuito)
- Datos almacenados en SQLite en el dispositivo
- Funciona 100% sin internet
- 1 sucursal, hasta 2 cajeros
- Historial completo de ventas y reportes
- Telemetría anónima (nombre del negocio + estado activo)
- Impresión Bluetooth/WiFi local
- Display cliente vía WiFi local

### Modo Cloud (Pago)
- Todo lo del modo local
- Datos en PostgreSQL (Supabase)
- Multi-sucursal ilimitada
- Cajeros y roles ilimitados
- Bodega central (con múltiples sucursales)
- Sync offline → online automático
- Reportes avanzados y análisis
- Backup automático diario
- Acceso desde cualquier dispositivo
- Exportación de datos (CSV/Excel)
- Migración desde modo local

---

## Roles del Sistema

| Rol | Panel | Acceso |
|-----|-------|--------|
| `saas_admin` | Admin SaaS | Gestión de tenants, planes, métricas globales |
| `business_admin` | Admin negocio | Productos, sucursales, reportes, cajeros |
| `cashier` | App móvil POS | Solo ventas de su sucursal asignada |

---

## Tipos de Negocio Soportados

El sistema es genérico — cada negocio configura su propio catálogo:
- Restaurantes (pollo, pizza, tacos, hamburguesas, etc.)
- Tiendas de retail
- Panaderías y cafeterías
- Cualquier negocio de venta en caja/mostrador

> El sistema **no soporta** gestión de mesas — es exclusivamente punto de venta en caja.

---

## Documentación

```
docs/
├── architecture/
│   ├── overview.md          ← Arquitectura general del sistema
│   ├── hexagonal.md         ← Detalle de arquitectura hexagonal (backend)
│   ├── local-cloud-sync.md  ← Estrategia de sync local ↔ cloud
│   └── multitenancy.md      ← Modelo multi-tenant
├── database/
│   ├── schema.sql           ← Schema actual (referencia)
│   ├── README.md            ← Convenciones de migración
│   └── migrations/          ← Un archivo por cambio aplicado
│       └── 001_initial_schema.sql
└── decisions/               ← ADRs (Architecture Decision Records)
```

---

## Configuración Inicial del Negocio

Al registrarse, el admin del negocio configura en este orden:

```
1. Datos del negocio     — nombre, logo, moneda
2. Sucursales            — al menos 1
3. Categorías            — tipos de productos
4. Productos             — nombre, precio, variantes, imagen
5. Insumos (opcional)    — si usa control de recetas
6. Stock inicial         — cantidad por sucursal
7. Promociones           — reglas, días y vigencia
8. Cajeros               — cuentas y asignación de sucursal
```

---

## Referencias
- [Loyverse POS](https://loyverse.com) — referencia de UX y features
- [Shopify POS](https://shopify.com/pos) — referencia de modelo de negocio
- [Refine + Supabase](https://refine.dev/docs/data/packages/supabase/)
- [Expo EAS](https://expo.dev/eas)
- [WatermelonDB Sync](https://nozbe.github.io/WatermelonDB/Advanced/Sync.html)
