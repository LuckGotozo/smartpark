# SmartPark 🚗

API REST para gerenciamento de estacionamentos, vagas, veículos e reservas.

## Sobre o projeto

O SmartPark permite que usuários encontrem vagas disponíveis e realizem reservas em estacionamentos.

O sistema possui autenticação com JWT e dois níveis de acesso: USER e ADMIN.

## Funcionalidades

- Cadastro de usuários
- Login com JWT
- Cadastro de veículos
- Cadastro de estacionamentos
- Cadastro de vagas
- Consulta de vagas disponíveis
- Criação de reservas
- Cancelamento de reservas
- Controle de conflito de horários
- Perfis USER e ADMIN
- Proteção de rotas com Spring Security

## Tecnologias

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- BCrypt
- Maven

## Executando o projeto

Configure as variáveis de ambiente:

DB_PASSWORD
JWT_SECRET

Depois execute:

```bash
./mvnw spring-boot:run


No Windows:

.\mvnw.cmd spring-boot:run

A API estará disponível em:

http://localhost:8080

Status

✅ Backend funcional

🚧 Frontend em desenvolvimento