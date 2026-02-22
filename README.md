# Usuario API

API REST para gerenciamento de usuários, com autenticação via JWT, cadastro de endereço e telefone.

## Funcionalidades

- Cadastro de usuário
- Login com geração de token JWT
- Busca de usuário por e-mail
- Exclusão de usuário por e-mail
- Atualização de dados do usuário autenticado
- Cadastro e atualização de endereço
- Cadastro e atualização de telefone
- Proteção de rotas com Spring Security + JWT

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- JWT (jjwt)
- Lombok
- Gradle

## Estrutura do projeto

```bash
src/main/java/com/alan/usuario
├── business
│   ├── dto
│   └── ...
├── controller
├── infrastructure
│   ├── entity
│   ├── exceptions
│   ├── repository
│   └── security
└── UsuarioApplication.java
```

## Pré-requisitos

- Java 17+
- PostgreSQL
- Gradle (ou usar o wrapper `gradlew` / `gradlew.bat`)

## Configuração do ambiente

A aplicação usa variáveis de ambiente para conexão com o banco de dados:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD` (opcional, com valor padrão vazio)

### Exemplo (Windows PowerShell)

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/db_usuario"
$env:DB_USER="postgres"
$env:DB_PASSWORD=""
```

### Exemplo (Linux/macOS)

```bash
export DB_URL="jdbc:postgresql://localhost:5432/db_usuario"
export DB_USER="postgres"
export DB_PASSWORD=""
```

## Como executar

### 1) Clonar o repositório

```bash
git clone https://github.com/AlanF-Oliveira/usuario.git
cd usuario
```

### 2) Criar o banco no PostgreSQL

Crie um banco com o nome `db_usuario` (ou outro nome, se ajustar no `DB_URL`).

### 3) Rodar a aplicação

#### Linux/macOS

```bash
./gradlew bootRun
```

#### Windows

```bat
gradlew.bat bootRun
```

A aplicação irá subir por padrão em:

- `http://localhost:8080`

## Autenticação JWT

Após o login, a API retorna um token no formato:

```text
Bearer <token>
```

Use esse token no header das rotas protegidas:

```http
Authorization: Bearer <token>
```

## Endpoints principais

### 1. Cadastrar usuário

**POST** `/usuario`

#### Body (exemplo)

```json
{
  "nome": "Alan",
  "email": "alan@email.com",
  "senha": "123456"
}
```

---

### 2. Login

**POST** `/usuario/login`

#### Body (exemplo)

```json
{
  "email": "alan@email.com",
  "senha": "123456"
}
```

#### Resposta (exemplo)

```text
Bearer eyJhbGciOi...
```

---

### 3. Buscar usuário por e-mail

**GET** `/usuario?email=alan@email.com`

> Requer autenticação.

---

### 4. Deletar usuário por e-mail

**DELETE** `/usuario/{email}`

Exemplo:

```http
DELETE /usuario/alan@email.com
```

> Requer autenticação.

---

### 5. Atualizar dados do usuário autenticado

**PUT** `/usuario`

> Requer autenticação.

#### Body (exemplo)

```json
{
  "nome": "Alan Ferreira",
  "senha": "novaSenha123"
}
```

---

### 6. Cadastrar endereço

**POST** `/usuario/endereco`

> Requer autenticação.

#### Body (exemplo)

```json
{
  "rua": "Rua Exemplo",
  "numero": 123,
  "complemento": "Apto 10",
  "cidade": "Fortaleza",
  "estado": "CE",
  "cep": "60000-000"
}
```

---

### 7. Atualizar endereço

**PUT** `/usuario/endereco?id={id}`

> Requer autenticação.

#### Body (exemplo)

```json
{
  "rua": "Rua Nova",
  "numero": 456,
  "complemento": "Casa",
  "cidade": "Fortaleza",
  "estado": "CE",
  "cep": "60000-111"
}
```

---

### 8. Cadastrar telefone

**POST** `/usuario/telefone`

> Requer autenticação.

#### Body (exemplo)

```json
{
  "ddd": "85",
  "numero": "999999999"
}
```

---

### 9. Atualizar telefone

**PUT** `/usuario/telefone?id={id}`

> Requer autenticação.

#### Body (exemplo)

```json
{
  "ddd": "85",
  "numero": "988888888"
}
```

## Regras e comportamentos importantes

- E-mail duplicado pode gerar erro de conflito (`ConflictException`)
- Usuário não encontrado pode gerar erro (`ResourceNotFoundException`)
- A senha é criptografada antes de salvar (BCrypt)
- As rotas de cadastro e login são públicas
- As demais rotas exigem token JWT

## Melhorias futuras 

- Mover a chave JWT para variável de ambiente
- Adicionar validações com Bean Validation (`@NotBlank`, `@Email`, etc.)
- Criar handler global para padronizar erros
- Adicionar documentação com Swagger/OpenAPI
- Criar testes unitários e de integração

## Autor

**Alan Ferreira de Oliveira**
