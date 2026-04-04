# 👤 Usuario

API REST para gerenciamento de usuários, responsável pelo cadastro, autenticação e manutenção de dados pessoais (incluindo endereços e telefones). Emite os tokens **JWT** utilizados em toda a plataforma de agendamento de tarefas.

> Este microsserviço faz parte de um ecossistema maior. O ponto de entrada recomendado para o frontend é o **[BFF Agendador de Tarefas](https://github.com/AlanF-Oliveira/bff-agendador-tarefas)**.

---

## 🚀 Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 4.0.2 | Framework base |
| Spring Data JPA | — | Persistência de dados |
| PostgreSQL | — | Banco de dados relacional |
| Spring Security | — | Autenticação e autorização |
| JWT (jjwt) | 0.13.0 | Geração e validação de tokens |
| BCryptPasswordEncoder | — | Criptografia de senhas |
| Lombok | — | Redução de boilerplate |
| Gradle | — | Build |
| Docker | — | Containerização |

---

## 📁 Estrutura do Projeto

```
src/main/java/com/alan/usuario/
├── UsuarioApplication.java
├── controller/
│   └── UsuarioController.java
├── business/
│   ├── UsuarioService.java
│   ├── dto/
│   │   ├── UsuarioDTO.java
│   │   ├── EnderecoDTO.java
│   │   └── TelefoneDTO.java
│   └── converter/
│       └── UsuarioConverter.java
└── infrastructure/
    ├── entity/
    │   ├── Usuario.java
    │   ├── Endereco.java
    │   └── Telefone.java
    ├── repository/
    │   ├── UsuarioRepository.java
    │   ├── EnderecoRepository.java
    │   └── TelefoneRepository.java
    ├── exceptions/
    │   ├── ResourceNotFoundException.java
    │   └── ConflictException.java
    └── security/
        ├── JwtUtil.java
        ├── JwtRequestFilter.java
        ├── SecurityConfig.java
        └── UserDetailsServiceImpl.java
```

---

## 🐳 Executando com Docker (recomendado)

O `docker-compose.yml` sobe a aplicação junto com o PostgreSQL:

```bash
git clone https://github.com/AlanF-Oliveira/usuario.git
cd usuario
docker-compose up --build
```

### Serviços e portas

| Serviço | Porta | Descrição |
|---|---|---|
| `app` | `8080` | API de usuários |
| `db` | `5432` | PostgreSQL |

### Variáveis de ambiente

| Variável | Valor padrão |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db:5432/db_usuario` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | `2010` |

### Derrubando os containers

```bash
docker-compose down
```

---

## 🔧 Dockerfile

Build multi-stage com Gradle:

```dockerfile
# Stage 1 — build
FROM gradle:8.14-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Stage 2 — runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/usuario.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/usuario.jar"]
```

---

## ▶️ Executando sem Docker

### Pré-requisitos

- Java 17+
- PostgreSQL rodando localmente

### Configuração

Edite o `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_usuario
spring.datasource.username=postgres
spring.datasource.password=2010
spring.jpa.hibernate.ddl-auto=update
```

### Executando

```bash
./gradlew bootRun
```

---

## 🔐 Autenticação

- Senhas armazenadas com **BCrypt**
- Token **JWT** com validade de **1 hora**
- Rotas públicas: `POST /usuario` e `POST /usuario/login`
- Todas as demais rotas exigem o header: `Authorization: Bearer <token>`

---

## 🌐 Endpoints

Base URL: `/usuario`

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `POST` | `/usuario` | ❌ | Cadastro de novo usuário |
| `POST` | `/usuario/login` | ❌ | Login — retorna token JWT |
| `GET` | `/usuario?email={email}` | ✅ | Busca dados do usuário |
| `PUT` | `/usuario` | ✅ | Atualiza dados do usuário |
| `DELETE` | `/usuario/{email}` | ✅ | Remove o usuário |
| `POST` | `/usuario/endereco` | ✅ | Cadastra endereço |
| `PUT` | `/usuario/endereco?id={id}` | ✅ | Atualiza endereço |
| `POST` | `/usuario/telefone` | ✅ | Cadastra telefone |
| `PUT` | `/usuario/telefone?id={id}` | ✅ | Atualiza telefone |

### Exemplo — Cadastro

```json
{
  "nome": "Alan Oliveira",
  "email": "alan@email.com",
  "senha": "minhasenha",
  "enderecos": [
    {
      "rua": "Rua das Flores",
      "numero": 123,
      "cidade": "Fortaleza",
      "estado": "CE",
      "cep": "60000-000"
    }
  ],
  "telefones": [
    { "ddd": "85", "numero": "987654321" }
  ]
}
```

### Exemplo — Login

```json
// Request
{ "email": "alan@email.com", "senha": "minhasenha" }

// Response
"Bearer eyJhbGciOiJIUzI1NiJ9..."
```

---

## 🧩 Microsserviços Relacionados

| Serviço | Repositório | Papel |
|---|---|---|
| **BFF** | [bff-agendador-tarefas](https://github.com/AlanF-Oliveira/bff-agendador-tarefas) | Ponto de entrada — orquestra todas as chamadas |
| **agendador-tarefas** | [agendador-tarefas](https://github.com/AlanF-Oliveira/agendador-tarefas) | Consome este serviço para validar tokens JWT |
| **notificacao** | [notificacao](https://github.com/AlanF-Oliveira/notificacao) | Envia notificações sobre tarefas agendadas |

---

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui.html
```

---

## 👤 Autor

**Alan F. Oliveira** — [github.com/AlanF-Oliveira](https://github.com/AlanF-Oliveira)