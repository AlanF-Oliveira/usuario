# Usuario

API REST para gerenciamento de usuários, responsável pelo cadastro, autenticação e manutenção de dados pessoais (incluindo endereços e telefones). Emite os tokens **JWT** utilizados em toda a plataforma de agendamento de tarefas.

> Este microsserviço faz parte de um ecossistema maior. O ponto de entrada recomendado para o frontend é o **[BFF Agendador de Tarefas](https://github.com/AlanF-Oliveira/bff-agendador-tarefas)**.

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2.5 | Framework base |
| Spring Data JPA | — | Persistência de dados |
| PostgreSQL | — | Banco de dados relacional |
| Spring Security | — | Autenticação e autorização |
| Spring Cloud OpenFeign | 2025.1.0 | Integração com a API ViaCEP |
| JWT (jjwt) | 0.13.0 | Geração e validação de tokens |
| BCryptPasswordEncoder | — | Criptografia de senhas |
| Springdoc OpenAPI | 3.0.2 | Documentação Swagger |
| SonarQube | 4.4.1 | Análise de qualidade de código |
| Lombok | — | Redução de boilerplate |
| Gradle | — | Build |
| Docker | — | Containerização |

---

## Estrutura do Projeto

```
usuario/
├── .github/
│   └── workflows/
│       └── gradle.yml
├── src/
│   └── main/
│       ├── java/com/alan/usuario/
│       │   ├── UsuarioApplication.java
│       │   ├── controller/
│       │   │   └── UsuarioController.java
│       │   ├── business/
│       │   │   ├── UsuarioService.java
│       │   │   ├── ViaCepService.java
│       │   │   ├── dto/
│       │   │   │   ├── UsuarioDTO.java
│       │   │   │   ├── EnderecoDTO.java
│       │   │   │   └── TelefoneDTO.java
│       │   │   └── converter/
│       │   │       └── UsuarioConverter.java
│       │   └── infrastructure/
│       │       ├── clients/
│       │       │   ├── ViaCepClient.java
│       │       │   └── ViaCepDTO.java
│       │       ├── entity/
│       │       │   ├── Usuario.java
│       │       │   ├── Endereco.java
│       │       │   └── Telefone.java
│       │       ├── repository/
│       │       │   ├── UsuarioRepository.java
│       │       │   ├── EnderecoRepository.java
│       │       │   └── TelefoneRepository.java
│       │       ├── exceptions/
│       │       │   ├── ResourceNotFoundException.java
│       │       │   └── ConflictException.java
│       │       └── security/
│       │           ├── JwtUtil.java
│       │           ├── JwtRequestFilter.java
│       │           ├── SecurityConfig.java
│       │           └── UserDetailsServiceImpl.java
│       └── resources/
│           └── application.properties
├── Dockerfile
├── docker-compose.yml
└── build.gradle
```

---

## Endpoints

Base URL: `/usuario`

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `POST` | `/usuario` | Não | Cadastro de novo usuário |
| `POST` | `/usuario/login` | Não | Login — retorna token JWT |
| `GET` | `/usuario?email={email}` | Sim | Busca dados do usuário |
| `PUT` | `/usuario` | Sim | Atualiza dados do usuário |
| `DELETE` | `/usuario/{email}` | Sim | Remove o usuário |
| `POST` | `/usuario/endereco` | Sim | Cadastra endereço |
| `PUT` | `/usuario/endereco?id={id}` | Sim | Atualiza endereço |
| `POST` | `/usuario/telefone` | Sim | Cadastra telefone |
| `PUT` | `/usuario/telefone?id={id}` | Sim | Atualiza telefone |
| `GET` | `/usuario/endereco/{cep}` | Sim | Consulta endereço pelo CEP (ViaCEP) |

### Exemplo — Cadastro

```json
// POST /usuario
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
// POST /usuario/login
{ "email": "alan@email.com", "senha": "minhasenha" }

// Response 200
"Bearer eyJhbGciOiJIUzI1NiJ9..."
```

---

## Autenticação

- Senhas armazenadas com **BCrypt**
- Token **JWT** com validade de **1 hora**
- Rotas públicas: `POST /usuario` e `POST /usuario/login`
- Todas as demais rotas exigem o header: `Authorization: Bearer <token>`

---

## Integração com ViaCEP

O endpoint `GET /usuario/endereco/{cep}` consulta a API pública **[ViaCEP](https://viacep.com.br)** via OpenFeign para retornar os dados de um endereço a partir do CEP informado. O CEP é validado e formatado antes da consulta — apenas dígitos são aceitos, e o valor deve ter exatamente 8 caracteres.

---

## Executando com Docker (recomendado)

O `docker-compose.yml` sobe a aplicação junto com o PostgreSQL:

```bash
git clone https://github.com/AlanF-Oliveira/usuario.git
cd usuario
docker-compose up --build
```

Para derrubar os containers:

```bash
docker-compose down
```

### Serviços e portas

| Serviço | Porta | Descrição |
|---|---|---|
| `app` | `8080` | API de usuários |
| `db` | `5432` | PostgreSQL |

### Variáveis de ambiente

| Variável | Valor no docker-compose |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db:5432/db_usuario` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | `2010` |

---

## Executando sem Docker

### Pré-requisitos

- Java 17+
- PostgreSQL rodando localmente

### Configuração

As propriedades lêem variáveis de ambiente. Defina-as no sistema ou exporte antes de rodar:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/db_usuario
export DB_USER=postgres
export DB_PASSWORD=sua_senha
```

Ou edite diretamente o `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_usuario
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### Executando

```bash
git clone https://github.com/AlanF-Oliveira/usuario.git
cd usuario
./gradlew bootRun
```

---

## Documentação da API (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui.html
```

---

## CI/CD

O projeto utiliza **GitHub Actions** para integração contínua. O pipeline é acionado automaticamente em:

- Pull Requests abertos, sincronizados ou reabertos para a branch `master`

**Etapas do pipeline:**

1. Checkout do código
2. Configuração do JDK 17 (Temurin)
3. Cache das dependências Gradle
4. Permissão de execução para o `gradlew`
5. Build com Gradle (`./gradlew build`)
6. Execução dos testes (`./gradlew test`)

O arquivo de configuração está em `.github/workflows/gradle.yml`.

---

## Microsserviços Relacionados

| Serviço | Repositório | Papel |
|---|---|---|
| **BFF** | [bff-agendador-tarefas](https://github.com/AlanF-Oliveira/bff-agendador-tarefas) | Ponto de entrada — orquestra todas as chamadas |
| **agendador-tarefas** | [agendador-tarefas](https://github.com/AlanF-Oliveira/agendador-tarefas) | Consome este serviço para validar tokens JWT |
| **notificacao** | [notificacao](https://github.com/AlanF-Oliveira/notificacao) | Envia notificações sobre tarefas agendadas |