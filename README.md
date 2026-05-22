# prototipo-login

Protótipo de login estilo Komga em Spring Boot 3 + Kotlin, com:

- Autenticação via **HTTP Basic** + **sessão server-side** (cookie `SESSION`)
- Persistência relacional com **jOOQ** + **Flyway**
- **H2** em modo PostgreSQL como banco padrão (perfil default)
- Perfil `supabase` pronto pra apontar pro **Supabase Postgres** com 3 variáveis de ambiente

## Stack

- Kotlin 1.9 / JDK 17 / Maven 3.6+
- Spring Boot 3.3.5 (Web, Security, Validation, jOOQ)
- Flyway 9.22.3 (H2 ainda embutido no core)
- H2 (default) / PostgreSQL (perfil supabase)
- BCrypt para hash de senha

## Camadas (estilo Komga)

```
src/main/kotlin/br/com/davijuvino/prototipologin
├── Application.kt
├── domain/
│   ├── model/
│   │   ├── Usuario.kt
│   │   ├── UsuarioRole.kt
│   │   └── Exceptions.kt
│   ├── persistence/
│   │   └── UsuarioRepository.kt          # interface (porta)
│   └── service/
│       └── UsuarioLifecycle.kt           # caso de uso: criar/atualizar
├── infrastructure/
│   ├── jooq/main/
│   │   └── UsuarioDao.kt                 # implementação jOOQ (adapter)
│   ├── security/
│   │   ├── SecurityConfiguration.kt
│   │   ├── PasswordEncoderConfiguration.kt
│   │   ├── PrototipoPrincipal.kt
│   │   └── PrototipoUserDetailsService.kt
│   └── seed/
│       └── UsuarioSeeder.kt              # cria admin@prototipo.local na 1a subida
└── interfaces/api/rest/
    ├── LoginController.kt                # POST /api/v1/login
    ├── UsuarioController.kt              # GET /api/v1/users/me, POST /api/v1/users
    └── dto/
        ├── UsuarioDto.kt
        ├── UsuarioCreationDto.kt
        └── ErrorResponseDto.kt

src/main/resources/
├── application.yml                       # H2 default
├── application-supabase.yml              # perfil Supabase
└── db/migration/
    └── V1__criar_tabela_usuario.sql
```

## Endpoints

| Método | Caminho              | Descrição                                                 | Auth          |
| ------ | -------------------- | --------------------------------------------------------- | ------------- |
| POST   | `/api/v1/login`      | Faz login via HTTP Basic, devolve `UsuarioDto` + cookie   | HTTP Basic    |
| GET    | `/api/v1/users/me`   | Devolve `UsuarioDto` do usuário autenticado               | Cookie SESSION|
| POST   | `/api/v1/users`      | Cria novo usuário (`USER`) — retorna `201 Created`        | Cookie SESSION|
| POST   | `/api/logout`        | Invalida a sessão; resposta `204 No Content`              | Cookie SESSION|

### Respostas conhecidas

- `200`: login OK ou consulta autenticada
- `201`: criação de usuário
- `204`: logout
- `400`: corpo inválido (validação Jakarta)
- `401`: sem Authorization válido ou sessão inválida
- `409`: tentativa de criar email já cadastrado

## Como rodar

### Banco H2 em memória (padrão)

```bash
mvn spring-boot:run
```

A app sobe em <http://localhost:8080>. O Flyway aplica `V1__criar_tabela_usuario.sql` e o `UsuarioSeeder` cria o admin inicial.

### Apontando para Supabase Postgres

1. Pegue a connection string em **Supabase → Project Settings → Database → Connection string → JDBC**, no formato `jdbc:postgresql://aws-0-XX.pooler.supabase.com:6543/postgres?sslmode=require` (use o **session pooler** na porta 5432 ou o **transaction pooler** na 6543).
2. Exporte:

   ```bash
   export SUPABASE_DB_URL='jdbc:postgresql://aws-0-XX.pooler.supabase.com:6543/postgres?sslmode=require'
   export SUPABASE_DB_USER='postgres.<project-ref>'
   export SUPABASE_DB_PASSWORD='<senha>'
   export SPRING_PROFILES_ACTIVE=supabase
   ```

3. Rode `mvn spring-boot:run`. O Flyway aplica as migrações na primeira execução.

## Usuário inicial (seed)

| Email                    | Senha     | Role  |
| ------------------------ | --------- | ----- |
| `admin@prototipo.local`  | `senha123`| ADMIN |

Criado automaticamente pelo `UsuarioSeeder` se a tabela `usuario` estiver vazia.

## Teste rápido com curl

```bash
# login (cria sessão)
curl -i -X POST -u 'admin@prototipo.local:senha123' \
  http://localhost:8080/api/v1/login -c jar.txt

# eu autenticado
curl -s http://localhost:8080/api/v1/users/me -b jar.txt

# criar outro usuário
curl -s -X POST http://localhost:8080/api/v1/users \
  -b jar.txt -H 'Content-Type: application/json' \
  -d '{"email":"alguem@x.com","password":"senha-forte"}'

# logout
curl -i -X POST http://localhost:8080/api/logout -b jar.txt
```

## Testes

```bash
mvn test
```

Cobre, com `@SpringBootTest` + `MockMvc` (H2 in-memory):

- Login com credenciais válidas (200, cookie de sessão criado)
- Login sem `Authorization` (401)
- Login com senha errada (401)
- Login com usuário inexistente (401)
- `GET /api/v1/users/me` sem sessão (401)
- Fluxo completo `login → /users/me` reaproveitando a sessão (200)
- Criação de novo usuário autenticado (201)
- Smoke test do contexto Spring (`ApplicationTests`)
