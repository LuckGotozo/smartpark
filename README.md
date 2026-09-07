# 🚗 SmartPark

API REST para gerenciamento de estacionamentos, veículos, vagas e reservas.

O **SmartPark** é um projeto Full Stack desenvolvido com o objetivo de colocar em prática conceitos de desenvolvimento backend, APIs REST, autenticação, autorização, banco de dados relacional e integração entre frontend e backend.

> Este repositório contém o **backend** da aplicação.

## 💻 Frontend

🔗 [Repositório do SmartPark Frontend](https://github.com/LuckGotoz0/smartpark)

---

## 🚀 Funcionalidades

### 👤 Usuários
- Cadastro de usuários
- Login
- Senhas armazenadas utilizando BCrypt
- Autenticação utilizando JWT
- Perfis de acesso `USER` e `ADMIN`

### 🚗 Veículos
- Cadastro de veículos
- Listagem dos veículos do usuário autenticado
- Associação do veículo ao proprietário
- Validação de placas duplicadas

### 🅿️ Estacionamentos
- Listagem de estacionamentos
- Cadastro de estacionamentos por administradores

### 🚘 Vagas
- Cadastro de vagas
- Listagem de vagas
- Consulta de vagas por estacionamento
- Consulta de vagas disponíveis por período
- Controle de vagas ativas e inativas

### 📅 Reservas
- Criação de reservas
- Listagem das reservas do usuário
- Associação entre usuário, veículo e vaga
- Validação de propriedade do veículo
- Controle de conflito de horários
- Cancelamento de reservas

---

## 🔐 Segurança

A aplicação utiliza **Spring Security + JWT**.

Após realizar login, o usuário recebe um token JWT que deve ser enviado nas requisições protegidas.

Exemplo:

```http
Authorization: Bearer SEU_TOKEN
```

Existem dois perfis de acesso:

### USER

Pode utilizar as funcionalidades comuns do sistema, como:

- consultar estacionamentos;
- consultar vagas;
- cadastrar seus veículos;
- criar reservas;
- visualizar suas reservas;
- cancelar suas reservas.

### ADMIN

Possui permissões administrativas adicionais, incluindo:

- cadastro de estacionamentos;
- cadastro de vagas;
- gerenciamento de recursos protegidos para administradores.

---

## 🛠️ Tecnologias utilizadas

### Backend

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven
- JWT / OAuth2 Resource Server

### Banco de dados

- PostgreSQL

### Ferramentas

- Git
- GitHub
- VS Code

---

## 🏗️ Estrutura do projeto

O backend foi organizado em camadas:

```text
src/main/java/com/smartpark/smartpark/

├── config/
├── controller/
├── dto/
├── exception/
├── model/
├── repository/
└── service/
```

### Controller

Responsável por receber as requisições HTTP e disponibilizar os endpoints da API.

### Service

Contém as regras de negócio da aplicação.

### Repository

Responsável pelo acesso e persistência dos dados.

### Model

Contém as entidades utilizadas pela aplicação.

### DTO

Objetos utilizados para transferência e validação de dados entre cliente e servidor.

### Config

Configurações relacionadas à segurança e à aplicação.

---

## 🔗 Principais endpoints

### Autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/auth/register` | Cadastrar usuário |
| POST | `/api/auth/login` | Realizar login |

### Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/users/me` | Dados do usuário autenticado |
| GET | `/api/users` | Listar usuários — ADMIN |

### Veículos

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/vehicles/me` | Cadastrar veículo |
| GET | `/api/vehicles/me` | Listar veículos do usuário |

### Estacionamentos

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/parking-lots` | Listar estacionamentos |
| POST | `/api/parking-lots` | Cadastrar estacionamento — ADMIN |

### Vagas

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/parking-spots` | Listar vagas |
| GET | `/api/parking-spots/parking-lot/{id}` | Listar vagas de um estacionamento |
| GET | `/api/parking-spots/parking-lot/{id}/available` | Consultar disponibilidade |
| POST | `/api/parking-spots/parking-lot/{id}` | Cadastrar vaga — ADMIN |

### Reservas

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/reservations` | Criar reserva |
| GET | `/api/reservations/me` | Listar minhas reservas |
| PATCH | `/api/reservations/{id}/cancel` | Cancelar reserva |

---

## 🗄️ Banco de dados

O SmartPark utiliza **PostgreSQL** para persistência dos dados.

Principais entidades:

```text
User
 │
 ├── Vehicle
 │
 └── Reservation
        │
        └── ParkingSpot
                │
                └── ParkingLot
```

---

## ⚙️ Como executar

### Pré-requisitos

Tenha instalado:

- Java
- PostgreSQL
- Git

Clone o projeto:

```bash
git clone https://github.com/LuckGotoz0/smartpark.git
```

Entre na pasta:

```bash
cd smartpark
```

Crie um banco PostgreSQL chamado:

```text
smartpark
```

A aplicação utiliza variáveis de ambiente para informações sensíveis.

Configure:

```text
DB_PASSWORD
JWT_SECRET
```

> Nenhuma senha ou chave secreta está armazenada no repositório.

Execute no Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

---

## 🖥️ Interface

O frontend foi desenvolvido separadamente utilizando **React + JavaScript**.

🔗 [Acessar repositório do frontend](https://github.com/LuckGotoz0/smartpark-frontend)

---

## 🎯 Objetivo do projeto

O SmartPark foi desenvolvido como projeto de estudo e portfólio durante minha formação em Engenharia de Software.

O projeto permitiu colocar em prática conceitos como:

- desenvolvimento de APIs REST;
- arquitetura em camadas;
- orientação a objetos;
- autenticação e autorização;
- JWT;
- relacionamento entre entidades;
- persistência com JPA/Hibernate;
- banco de dados PostgreSQL;
- tratamento de erros e validações;
- integração entre frontend e backend;
- versionamento com Git e GitHub.

---

## 👨‍💻 Autor

**Guilherme Neineska**

Estudante de Engenharia de Software com foco em desenvolvimento Backend Java.

[LinkedIn](https://www.linkedin.com/in/guilherme-neineska/)
