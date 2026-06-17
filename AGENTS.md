# Task Management — Monorepo

## Overview

```
task-management-backend/          ← root (pnpm workspace root)
├── apps/
│   ├── backend/                  ← Java / Spring Boot (Gradle)
│   └── frontend/                 ← React + Vite + TypeScript (pnpm)
├── packages/                     ← shared configs/types
├── compose.yaml                  ← Postgres + MailHog
├── .env                          ← environment variables
├── pnpm-workspace.yaml
└── package.json                  ← root dev scripts
```

## Stacks

### Backend (`apps/backend/`)

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 4.0.3 (Spring Framework 7.x) |
| Build | Gradle (Groovy DSL) |
| Database | PostgreSQL (Spring Data JPA + Hibernate) |
| API | Spring GraphQL (single `/graphql` endpoint, no REST) |
| Auth | JWT (jjwt 0.12.x), access + refresh tokens, BCrypt |
| Email | Spring Mail + MailHog (dev) |
| Misc | Lombok, Jakarta Validation, Docker Compose |

### Frontend (`apps/frontend/`)

| Layer | Technology |
|-------|-----------|
| Language | TypeScript 6 |
| Framework | React 19 |
| Build | Vite 8 |
| GraphQL Client | Apollo Client 4 |
| Package Manager | pnpm (workspace) |

## Domain Model

```
Account ──< AccountMember >── User
  1                          *
  │
  └── Project ──< ProjectMember >── User
```

- **User** — identity/login, belongs to one or more Accounts via AccountMember
- **Account** — tenant/workspace, owns projects
- **AccountMember** — user's role (Admin/User) within an Account
- **Invitation** — invite a user (by email) to join an Account
- **Project** — owned by an Account and a User; has members
- **ProjectMember** — user's membership in a Project

## Architecture & SOLID

All Java code lives under `com.rrieck.taskmanagementbackend`.

### Backend package layout (feature-first, layer-second)

```
{feature}/
  model/         JPA entities + ID classes + value objects
  repository/    Spring Data JPA interfaces (no custom impls)
  service/       Business logic, one class per use-case
  schema/        GraphQL controller (@Controller) + DTO types
  config/        GraphQL scalar wiring, runtime wiring
  exception/     Domain exceptions (one file per exception)
  security/      Filters, auth plumbing (auth feature only)
```

Current features: `auth/`, `project/`, `email/`, `common/`.

### SOLID mapping

| Principle | How it's applied |
|-----------|-----------------|
| **S** — Single Responsibility | Each service does exactly one thing (`CreateAccessToken`, `CreateRefreshToken`, etc.). Entities own their shape only. |
| **O** — Open/Closed | `Identifier` base class — add new ID types by subclassing. Exception hierarchy (`OutgoingException`/`InternalException`) — add new exception types without touching handlers. |
| **L** — Liskov Substitution | All `Identifier` subtypes (`UserId`, `AccountId`, `ProjectId`, etc.) are interchangeable via the base type wherever used. |
| **I** — Interface Segregation | Services depend on narrow collaborators via constructor injection, never on fat god-interfaces. Each repo interface only exposes the queries that feature needs. |
| **D** — Dependency Inversion | All dependencies are injected (constructor injection + `@RequiredArgsConstructor`). Services never `new` their collaborators. Repos are interfaces. |

## Conventions

### Java (backend)
- **camelCase** for all identifiers
- **PascalCase** for enums: `Role.Admin`, `Role.User`
- **Services:** verb phrase — `CreateProjectService`, `LoginUserService`, `DeleteAllRefreshTokenForUser`
- **GraphQL controllers:** `{Noun}{Mutation|Query}` — `RegisterMutation`, `GetUserQuery`
- **DTO types:** `*Types.java` class containing inner records — `AuthTypes`, `UserTypes`, `AccountMemberTypes`
- **DTO factory:** static `from(Entity entity)` method on each DTO record
- **Repos:** custom prefixes — `findOpt*` (returns `Optional`), `getOpt*` (returns `Optional`), `getAll*` (returns `List`), `getBy*` (returns single, throws if missing)
- **Exceptions:** `{Problem}{NotFound|Expired|AlreadyExists}` — `AccountNotFound`, `InvitationExpired`, `EmailAlreadyRegistered`

### GraphQL schema (`.graphqls` files in `apps/backend/src/main/resources/graphql/`)
- **Root:** `root.graphqls` — declares `type Query`, `type Mutation`, shared scalars (`DateTime`, `UserId`, `AccountId`, etc.)
- **Feature files:** `auth.graphqls`, `account.graphqls`, `invitation.graphqls`, `user.graphqls` — `extend type Query` / `extend type Mutation`
- **Naming:** PascalCase types, camelCase fields, input types end in `Input`

### TypeScript / React (frontend)
- **camelCase** for variables, functions, file names
- **PascalCase** for components, types, interfaces
- **Components:** `src/components/{Feature}/{Name}.tsx`
- **Pages:** `src/pages/{Name}.tsx`
- **Hooks:** `src/hooks/use{Name}.ts`
- **GraphQL:** queries/mutations co-located with components or in `src/graphql/`
- **Apollo Client 4** — import React hooks from `@apollo/client/react`, core from `@apollo/client/core`, links from `@apollo/client/link/*`
- Use `graphql-codegen` for typed operations (once schema is stable)

## Backend Code Patterns

### Entities
```java
@Entity
@Table(name = "table_name")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SomeEntity {
    @EmbeddedId
    private SomeId id;
    // ... columns, relationships
}
```

### IDs
```java
@Embeddable
public final class SomeId extends Identifier {
    public SomeId(UUID id) { super(id); }
    public SomeId() {}
    public static SomeId fromString(String value) { return new SomeId(UUID.fromString(value)); }
    public static SomeId generateId() { return new SomeId(UUID.randomUUID()); }
}
```

### Services
```java
@Service
@RequiredArgsConstructor
public class DoSomethingService {
    private final SomeRepository someRepository;
    private final AnotherService anotherService;

    public ReturnType doSomething(ParamType param) {
        // throw domain exception from exception/ package on failure
        // use builders for entity construction
        // delegate to other services for sub-steps
        return result;
    }
}
```

### GraphQL controller
```java
@Controller
@RequiredArgsConstructor
public class SomeMutation {
    private final DoSomethingService doSomethingService;

    @MutationMapping
    public ReturnType someMutation(@Argument SomeInput input) {
        return doSomethingService.doSomething(input.value());
    }
}
```

### DTO types
```java
public class SomeTypes {
    @Builder
    public record SomeType(
        SomeId id,
        String field
    ) {
        public static SomeType from(SomeEntity entity) {
            return SomeType.builder()
                .id(entity.getId())
                .field(entity.getField())
                .build();
        }
    }

    public record SomeInput(@NotBlank String value) {}
}
```

### Exceptions
- **User-facing errors:** extend `OutgoingException` (takes errorCode, HttpStatus, message) — caught by `GraphQlExceptionHandlerAdvice` → GraphQL error with extensions
- **Server errors:** extend `InternalException` → 500 response
- Exception class per error case in `exception/` sub-package

### Auth flow
- `AuthorizationWrapper.maybeAuthenticated()` / `AuthorizationWrapper.authenticated()` — use these in services that need the current user context
- They take the Spring `Authentication` object + a lambda that receives `Optional<AuthorizationContext>` or `AuthorizationContext`
- `AuthorizationContext` = `{ userId, accountId, email }`

## Dependency Flow (enforced)

```
GraphQL Controller → Service → Repository
                         ↓
                     Domain Exception
```

- Controllers are thin — no business logic, just destructure inputs and delegate
- Services never inject other controllers or schema types
- Repos are never injected into controllers (always go through a service)
- `model/` has zero dependencies on other layers
- `common/` contains shared infrastructure only

## Development Workflow

### Dev server
```bash
pnpm dev              # starts infra (Docker) + backend + frontend concurrently
pnpm dev:infra        # docker compose up -d (Postgres + MailHog)
pnpm dev:backend      # Gradle bootRun in apps/backend/
pnpm dev:frontend     # Vite dev server in apps/frontend/
```

The Vite dev server proxies `/graphql` to `localhost:8080` (see `apps/frontend/vite.config.ts`).

### Adding a new backend feature
1. Create `{feature}/model/`, `repository/`, `service/`, `schema/`, `config/` (exception/ if needed) under `apps/backend/src/main/java/...`
2. Add typed ID extending `Identifier` + entity class
3. Add Spring Data JPA repository interface
4. Add one or more service classes (one use-case per class)
5. Add `*Types.java` with DTO records + `from(entity)` factory
6. Add `@Controller` class with `@MutationMapping` / `@QueryMapping`
7. Register custom scalars in `config/` if the feature introduces new ID types
8. Add `.graphqls` schema file in `apps/backend/src/main/resources/graphql/`

### Adding a new frontend feature
1. Create component in `apps/frontend/src/components/{Feature}/`
2. Add GraphQL query/mutation (co-located or in `src/graphql/`)
3. Use Apollo Client 4 — `useQuery` / `useMutation` from `@apollo/client/react`
4. Wire into routing / pages

### When throwing exceptions (backend)
- Bad user input / domain violation → `OutgoingException` subclass (caught by `GraphQlExceptionHandlerAdvice`, returned as GraphQL error with `code` + `httpStatus` extensions)
- Bug / shouldn't happen → `InternalException` subclass (500)

### Tests
- **Backend:** JUnit 5 + Spring Boot Test; `@DataJpaTest` for repos, `@GraphQlTest` for controllers, `@SpringBootTest` for full context
- **Frontend:** Vitest + React Testing Library (standard Vite setup)
- Test files live alongside production code

### Environment
- Root `.env` is gitignored; `.env.example` in `apps/backend/` shows required vars
- `application.properties` looks for `.env` at root (`../.env`) or local (`./.env`)

### Remaining / known gaps
- `InviteToAccountService` has a TODO marker: email sending should go through a message queue (not inline)
