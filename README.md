# prototipo-login

Protótipo mínimo de **login de usuário** em Spring Boot 3 + Kotlin.

## Stack

- Kotlin 1.9 (JDK 17)
- Spring Boot 3.3
- Spring Security (form login)
- Thymeleaf
- Gradle (Kotlin DSL) + Gradle Wrapper

## Como rodar

```bash
./gradlew bootRun
```

A aplicação sobe em <http://localhost:8080>. Qualquer rota não-pública redireciona para `/login`.

### Usuário de teste (em memória)

| Usuário   | Senha     |
| --------- | --------- |
| `usuario` | `senha123`|

Definido em `SecurityConfiguration.kt` via `InMemoryUserDetailsManager`. Trocar para um `UserDetailsService` apoiado em banco quando o protótipo evoluir.

## Estrutura

```
src/main/kotlin/br/com/davijuvino/prototipologin
├── PrototipoLoginApplication.kt   # entrypoint do Spring Boot
├── config
│   └── SecurityConfiguration.kt   # filtro de segurança + usuário em memória
└── web
    └── HomeController.kt          # rotas `/` e `/login`

src/main/resources
├── application.yml
├── static/css/styles.css
└── templates
    ├── home.html
    └── login.html
```

## Testes

```bash
./gradlew test
```

Cobre:

- `GET /login` retorna 200 para visitantes
- `GET /` redireciona quando não autenticado
- Login com credenciais válidas autentica
- Login com credenciais inválidas falha
