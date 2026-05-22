# prototipo-login

Protótipo mínimo de **login de usuário** em Spring Boot 3 + Kotlin — somente endpoint REST, sem tela/form.

## Stack

- Kotlin 1.9 (JDK 17)
- Spring Boot 3.3
- Spring Security (stateless, sem form login, sem HTTP Basic, sem CSRF)
- Gradle (Kotlin DSL) + Gradle Wrapper

## Endpoint

### `POST /api/auth/login`

Autentica usando `AuthenticationManager` + `InMemoryUserDetailsManager`.

**Request**

```json
POST /api/auth/login
Content-Type: application/json

{ "username": "usuario", "password": "senha123" }
```

**Respostas**

- `200 OK`
  ```json
  { "authenticated": true, "username": "usuario", "authorities": ["ROLE_USER"] }
  ```
- `401 Unauthorized`
  ```json
  { "error": "Credenciais invalidas" }
  ```
- `400 Bad Request` quando `username` ou `password` estão em branco.

## Como rodar

```bash
./gradlew bootRun
```

A aplicação sobe em <http://localhost:8080>.

Exemplo de teste manual com `curl`:

```bash
curl -i -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"usuario","password":"senha123"}'
```

## Usuário de teste (em memória)

| Usuário   | Senha     |
| --------- | --------- |
| `usuario` | `senha123`|

Configurado em `SecurityConfiguration.kt` via `InMemoryUserDetailsManager` + `BCryptPasswordEncoder`. Substituir por um `UserDetailsService` apoiado em banco quando o protótipo evoluir.

## Estrutura

```
src/main/kotlin/br/com/davijuvino/prototipologin
├── PrototipoLoginApplication.kt   # entrypoint do Spring Boot
├── config
│   └── SecurityConfiguration.kt   # filtro de seguranca + usuario em memoria
└── web
    └── AuthController.kt          # POST /api/auth/login
```

## Testes

```bash
./gradlew test
```

Cobre:

- Login com credenciais válidas retorna `200` e o usuário.
- Login com senha errada retorna `401`.
- Login com usuário inexistente retorna `401`.
- Body inválido (campos em branco) retorna `400`.
