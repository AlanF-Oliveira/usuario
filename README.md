# 👤 Usuário

API REST para gerenciamento de usuários, desenvolvida com **Java 17** e **Spring Boot 4**. O serviço é responsável pelo cadastro, autenticação e manutenção dos dados de usuários (incluindo endereços e telefones), com autenticação via **JWT** e senhas criptografadas com **BCrypt**. Este microsserviço é consumido pelo [agendador-tarefas](https://github.com/AlanF-Oliveira/agendador-tarefas) para validar identidades.

---

## 🚀 Tecnologias Utilizadas

| Tecnologia | Descrição |
|---|---|
| Java 17 | Linguagem principal |
| Spring Boot 4.0.2 | Framework principal |
| Spring Data JPA | Persistência de dados |
| PostgreSQL | Banco de dados relacional |
| Spring Security | Autenticação e autorização |
| JWT (jjwt 0.13.0) | Geração e validação de tokens |
| BCryptPasswordEncoder | Criptografia de senhas |
| Lombok | Redução de boilerplate |
| Gradle | Gerenciamento de build |

---

## 📁 Estrutura do Projeto

```
src/main/java/com/alan/usuario/
├── UsuarioApplication.java                # Classe principal
├── controller/
│   └── UsuarioController.java             # Endpoints REST
├── business/
│   ├── UsuarioService.java                # Regras de negócio
│   ├── dto/
│   │   ├── UsuarioDTO.java                # DTO de usuário
│   │   ├── EnderecoDTO.java               # DTO de endereço
│   │   └── TelefoneDTO.java               # DTO de telefone
│   └── converter/
│       └── UsuarioConverter.java          # Conversão manual DTO <-> Entity
└── infrastructure/
    ├── entity/
    │   ├── Usuario.java                   # Entidade JPA de usuário
    │   ├── Endereco.java                  # Entidade JPA de endereço
    │   └── Telefone.java                  # Entidade JPA de telefone
    ├── repository/
    │   ├── UsuarioRepository.java         # Repositório JPA de usuário
    │   ├── EnderecoRepository.java        # Repositório JPA de endereço
    │   └── TelefoneRepository.java        # Repositório JPA de telefone
    ├── exceptions/
    │   ├── ResourceNotFoundException.java # Exceção para recurso não encontrado
    │   └── ConflictException.java         # Exceção para conflitos (ex: email duplicado)
    └── security/
        ├── JwtUtil.java                   # Geração e validação de tokens JWT
        ├── JwtRequestFilter.java          # Filtro de autenticação JWT
        ├── SecurityConfig.java            # Configuração do Spring Security
        └── UserDetailsServiceImpl.java    # Carregamento de detalhes do usuário
```

---

## 🔐 Autenticação

A API utiliza autenticação **stateless** baseada em **JWT**. O token é gerado no login e deve ser enviado no header das requisições protegidas:

```
Authorization: Bearer <seu_token_jwt>
```

- As senhas são armazenadas criptografadas com **BCrypt**.
- O token tem validade de **1 hora**.
- O `JwtRequestFilter` intercepta e valida o token em cada requisição.

### Rotas públicas (sem autenticação)

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/usuario` | Cadastro de novo usuário |
| `POST` | `/usuario/login` | Login e geração do token JWT |
| `GET` | `/auth` | Verificação de autenticação |

---

## 🌐 Endpoints

Base URL: `/usuario`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/usuario` | Cadastra um novo usuário | ❌ |
| `POST` | `/usuario/login` | Realiza login e retorna token JWT | ❌ |
| `GET` | `/usuario` | Busca dados do usuário por e-mail | ✅ |
| `PUT` | `/usuario` | Atualiza dados do usuário autenticado | ✅ |
| `DELETE` | `/usuario/{email}` | Remove um usuário pelo e-mail | ✅ |
| `PUT` | `/usuario/endereco` | Atualiza um endereço por ID | ✅ |
| `POST` | `/usuario/endereco` | Cadastra novo endereço para o usuário | ✅ |
| `PUT` | `/usuario/telefone` | Atualiza um telefone por ID | ✅ |
| `POST` | `/usuario/telefone` | Cadastra novo telefone para o usuário | ✅ |

### Detalhamento dos Endpoints

#### `POST /usuario` — Cadastro
```json
{
  "nome": "Alan Oliveira",
  "email": "alan@email.com",
  "senha": "minhasenha",
  "enderecos": [
    {
      "rua": "Rua das Flores",
      "numero": 123,
      "complemento": "Apto 4",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01310-100"
    }
  ],
  "telefones": [
    {
      "ddd": "11",
      "numero": "987654321"
    }
  ]
}
```

#### `POST /usuario/login` — Login
**Body:**
```json
{
  "email": "alan@email.com",
  "senha": "minhasenha"
}
```
**Resposta:**
```
Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### `GET /usuario?email={email}` — Busca usuário
**Header:** `Authorization: Bearer <token>`

#### `PUT /usuario` — Atualiza dados
Atualiza nome, e-mail e/ou senha do usuário autenticado. Campos `null` são ignorados.

**Header:** `Authorization: Bearer <token>`

#### `PUT /usuario/endereco?id={id}` — Atualiza endereço
Campos `null` são ignorados (atualização parcial).

#### `PUT /usuario/telefone?id={id}` — Atualiza telefone
Campos `null` são ignorados (atualização parcial).

---

## 📦 Modelo de Dados

### Usuario (tabela: `usuario`)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Long | Identificador único (auto-gerado) |
| `nome` | String (100) | Nome do usuário |
| `email` | String (100) | E-mail (usado como username) |
| `senha` | String (255) | Senha criptografada com BCrypt |
| `enderecos` | List\<Endereco\> | Lista de endereços (OneToMany) |
| `telefones` | List\<Telefone\> | Lista de telefones (OneToMany) |

### Endereco (tabela: `endereco`)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Long | Identificador único |
| `rua` | String | Nome da rua |
| `numero` | Long | Número |
| `complemento` | String (50) | Complemento |
| `cidade` | String (150) | Cidade |
| `estado` | String (2) | UF |
| `cep` | String (9) | CEP |

### Telefone (tabela: `telefone`)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Long | Identificador único |
| `ddd` | String (3) | DDD |
| `numero` | String (10) | Número |

---

## ⚙️ Como Executar

### Pré-requisitos

- Java 17+
- PostgreSQL rodando localmente ou em um container

### Configuração

Crie/edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/usuario
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Rodando com Gradle

```bash
# Clonar o repositório
git clone https://github.com/AlanF-Oliveira/usuario.git
cd usuario

# Executar
./gradlew bootRun
```

### Build

```bash
./gradlew build
```

### Testes

```bash
./gradlew test
```

---

## 🔗 Integração com outros microsserviços

Este serviço expõe o endpoint `GET /usuario?email={email}` consumido pelo microsserviço [agendador-tarefas](https://github.com/AlanF-Oliveira/agendador-tarefas) via **OpenFeign** para validar usuários autenticados por token JWT.

---

## 👤 Autor

Desenvolvido por **[AlanF-Oliveira](https://github.com/AlanF-Oliveira)**
