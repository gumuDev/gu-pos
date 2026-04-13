# CLAUDE.md — Guía de trabajo para Claude

## Estructura del Monorepo

```
pos-saas/
├── backend/       ← Spring Boot (Java 21) — Arquitectura Hexagonal
├── web/           ← Next.js 14 + Refine + Ant Design
├── mobile/        ← React Native + Expo
└── docs/          ← Arquitectura, BD, decisiones
```

---
## Documentation proyecto
mi vault de obsidian está en `sas-pos/`

### Vault estructura
- /daily-notes
- /features
- /bugs
- /improves
- /plans

### Organización de docs por estado

**`/features`**, **`/improves`**, **`/bugs`** y **`/plans`** — todos organizados primero por proyecto, luego por estado:
```
features/
├── mobile/
│   ├── backlog/      ← priorizados, listos para implementar
│   ├── in-progress/  ← en desarrollo activo
│   ├── completed/    ← terminados y validados
│   └── icebox/       ← ideas sueltas sin priorizar
├── web/
│   └── ...
├── backend/
│   └── ...
└── trash/

improves/  (y bugs/ — misma estructura)
├── mobile/
│   ├── pending/
│   ├── in-progress/
│   └── completed/
├── web/
│   └── ...
└── backend/
    └── ...

plans/
├── mobile/
│   ├── pending/
│   ├── in-progress/
│   └── completed/    ← cada plan es un subfolder con fase1.md, fase2.md... + index.md
├── web/
│   └── ...
└── backend/
    └── ...
```

**Regla:** Al crear o mover un doc, ubicarlo en `<tipo>/<proyecto>/<estado>/`. Al cambiar el estado, mover el archivo (o carpeta si es un plan) al folder correspondiente.
## Stack por Proyecto

### Backend (`/backend`)
- Java 21 + Spring Boot 3
- Arquitectura Hexagonal (ver `docs/architecture/hexagonal.md`)
- PostgreSQL via Supabase
- Flyway para migraciones
- JWT propio (sin Supabase Auth)

### Web (`/web`)
- Next.js 14 (App Router)
- Refine + Ant Design para paneles admin
- Tailwind CSS
- next-intl (español default, inglés soportado)

### Mobile (`/mobile`)
- React Native + Expo SDK 51+
- NativeWind (Tailwind para RN)
- expo-sqlite + WatermelonDB (modo local)
- EAS Build para compilación

---

## Comandos Disponibles

### Backend
- Claude **puede ejecutar**: `./mvnw test`
- Claude **nunca ejecuta**: `./mvnw spring-boot:run` (el usuario lo corre manualmente)
- Cada vez que se agrega o modifica un endpoint, actualizar `backend/src/main/resources/api/openapi.yml`

### Web
- Claude **puede ejecutar**: `npm install`
- Claude **nunca ejecuta**: `npm run dev`, `npm run build`

### Mobile
- Claude **puede ejecutar**: `npm install`
- Claude **nunca ejecuta**: `npx expo start`, `eas build`

---

## Reglas de Comportamiento

### Development Approach
- NEVER rush ahead with multiple features at once. Implement ONE feature at a time.
- After implementing a feature, ask the user to validate before proceeding.
- When building UI components, show the user what you plan to build BEFORE writing code.
- NEVER mark a feature, improve, bug, or plan phase as `completed` in Obsidian until the user explicitly confirms it works and tells you to update it. Never update Obsidian status automatically.

### Debugging Rules
- Identify the ACTUAL root cause before attempting fixes.
- Limit yourself to 3 attempts on a single bug before asking for more context.
- If first fix doesn't work, step back and re-read error/logs before trying another.

### Code Changes
- After refactors touching multiple files, run `tsc --noEmit` (web/mobile) or `./mvnw compile` (backend) BEFORE presenting work as complete.
- Do not add columns, fields, or features that weren't explicitly requested.

---

## Arquitectura Hexagonal — Backend
Los archivos viven en `docs/architecture/`:

Toda nueva feature del backend sigue esta estructura **sin excepción**:

```
src/main/java/com/pos/
└── <feature>/
    ├── domain/
    │   ├── model/          ← Entidades y value objects (sin dependencias externas)
    │   ├── port/
    │   │   ├── in/         ← Casos de uso (interfaces que el mundo exterior llama)
    │   │   └── out/        ← Repositorios (interfaces que el dominio llama)
    │   └── service/        ← Implementación de casos de uso
    └── infrastructure/
        ├── adapter/
        │   ├── in/
        │   │   └── web/    ← Controllers REST
        │   └── out/
        │       └── persistence/  ← Implementación de repositorios (JPA/JDBC)
        └── config/         ← Beans de Spring, configuración
```

### Reglas de dependencia (estrictas)
- `domain/` — CERO dependencias de Spring, JPA, o cualquier framework
- `domain/service/` — solo depende de `domain/model/` y `domain/port/`
- `infrastructure/` — depende de `domain/`, nunca al revés
- Los controllers llaman puertos `in/`, nunca servicios directamente
- Los repositorios implementan puertos `out/`, nunca son llamados desde controllers

### Reglas de tamaño obligatorias

| Archivo | Máximo |
|---|---|
| Controller | 80 líneas |
| Service (caso de uso) | 150 líneas |
| Repository impl | 100 líneas |
| Domain model | 100 líneas |

---

## Arquitectura Frontend — Feature-Based

### Web (`src/features/<nombre>/`)

```
src/features/<nombre>/
├── components/   ← UI pura, sin queries ni lógica de negocio
├── hooks/        ← estado, lógica, llamadas a services
├── services/     ← llamadas a la API del backend
├── types/        ← interfaces TypeScript
└── constants/    ← config estática
```

### Mobile (`src/features/<nombre>/`)

Mobile extiende la estructura base con capas adicionales requeridas por WatermelonDB:

```
src/features/<nombre>/
├── components/     ← UI pura, sin queries ni lógica de negocio
├── hooks/          ← estado, lógica, llama a repositories o services
├── services/       ← lógica de negocio pura (sin estado, sin DB)
├── repositories/   ← acceso a WatermelonDB (queries, writes)
├── models/         ← modelos WatermelonDB (@Model, @field, @relation)
├── types/          ← interfaces TypeScript
└── constants/      ← config estática
```

### Reglas de dependencia (mobile)

```
app/ (pantallas)
    ↓ solo importa
  hooks/
    ↓ solo importa
  repositories/ o services/
    ↓ solo importa
  models/
```

- **Pantallas** — nunca importan repositories ni models directamente
- **hooks/** — orquestan estado y llaman repositories o services
- **repositories/** — solo acceso a WatermelonDB, sin lógica de negocio
- **services/** — lógica pura (cálculos, formateo, builders), sin estado ni DB
- **models/** — definición WatermelonDB, sin lógica de negocio

### Reglas de tamaño

| Archivo | Máximo |
|---|---|
| `page.tsx` / `Screen.tsx` | 100 líneas |
| `components/` | 200 líneas |
| `hooks/` | 200 líneas |
| `services/` | 150 líneas |
| `repositories/` | 100 líneas |

### Responsabilidades
- **Screen/Page** — solo layout, importa componentes y hooks
- **hooks/** — estado y lógica, llama a repositories o services
- **services/** — lógica pura o HTTP calls al backend, sin estado
- **components/** — UI pura, recibe props

---

## Multi-tenancy

- Cada request al backend lleva el `tenant_id` en el JWT
- Todos los queries filtran por `tenant_id` en la capa de persistencia
- El dominio no conoce el concepto de tenant — es responsabilidad del adaptador
- Nunca hacer queries sin filtro de tenant en datos de negocio

---

## Base de Datos — Convenciones de Migración

Los archivos viven en `docs/database/`:

```
docs/database/
├── schema.sql           ← Schema actual (solo referencia, exportado de Supabase)
├── README.md            ← Convenciones
└── migrations/          ← Un archivo por cambio
    └── NNN_descripcion.sql
```

### Reglas obligatorias
1. **Antes de aplicar** cualquier cambio en Supabase → crear `docs/database/migrations/NNN_descripcion.sql`
2. **Después de aplicar** → actualizar `docs/database/schema.sql`
3. Los archivos de migración son **documentación** — se aplican manualmente en Supabase SQL Editor
4. Flyway gestiona las migraciones del backend en `backend/src/main/resources/db/migration/`
5. Numbering: `001`, `002`, `003` — 3 dígitos, secuencial

---

## Sync Local ↔ Cloud

- Todo modelo local tiene campos: `id` (UUID), `sync_status` (`pending` | `synced` | `conflict`), `updated_at`, `deleted_at`
- El sync nunca borra registros — usa soft delete con `deleted_at`
- En conflictos: **cloud gana** por defecto (last-write-wins por `updated_at`)
- Ver detalle en `docs/architecture/local-cloud-sync.md`

---

## Código del Proyecto

- Código escrito en **inglés** (variables, funciones, clases, comentarios)
- Interfaz de usuario en **español**
- i18n con next-intl (web) y i18n-js (mobile)

---

## Referencias

Ver `README.md` y `docs/` para detalle completo del sistema.
