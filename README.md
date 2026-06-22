# URL Shortener API

[![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)](#)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff)](#)
[![Postgres](https://img.shields.io/badge/Postgres-%23316192.svg?logo=postgresql&logoColor=white)](#)
[![Redis](https://img.shields.io/badge/Redis-%23DD0031.svg?logo=redis&logoColor=white)](#)
[![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff)](#)
[![JUnit5](https://img.shields.io/badge/JUnit5-C21325?logo=junit5&logoColor=fff)](#)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-6BA539?logo=openapiinitiative&logoColor=white)](#)
[![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=173647)](#)


API para encurtamento de URLs desenvolvida com Spring Boot, PostgreSQL e Redis.

O projeto tem como objetivo explorar conceitos de backend modernos, com foco em cache distribuído utilizando Redis e proteção da API através de rate limiting com Bucket4j.

## Principais Funcionalidades
- Encurtamento de URLs
- Redirecionamento de URLs
- Expiração de URLs
- Cache com Redis
- Rate Limiting com Bucket4j
- Persistência com PostgreSQL
- Containerização com Docker

## Arquitetura

```text
                    ┌─────────────────┐
                    │     Cliente     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Spring Boot API │
                    └────────┬────────┘
                             │
             ┌───────────────┼───────────────┐
             │                               │
             ▼                               ▼
    ┌─────────────────┐            ┌─────────────────┐
    │      Redis      │            │   PostgreSQL    │
    │                 │            │                 │
    │ • Cache URLs    │            │ • URLs          │
    │ • Rate Limiter  │            │ • Analytics     │
    │   (Bucket4j)    │            │ • Metadata      │
    └─────────────────┘            └─────────────────┘
```

### Fluxo de Redirecionamento

```text
Cliente
   │
   ▼
Bucket4j
   │
   ▼
Busca URL
(Redis → PostgreSQL)
   │
   ▼
URL ativa?
   │
   ├── Não
   │      │
   │      ▼
   │   Erro
   │
   └── Sim
          │
          ▼
   Incrementa contador de cliques
          │
          ▼
   Registra analytics de acesso
          │
          ▼
   Atualiza PostgreSQL
          │
          ▼
   Invalida cache Redis
          │
          ▼
     HTTP 302 FOUND
          │
          ▼
      URL Original
```



## Estrutura do Projeto
```text
com.devgui.urlshortener
│
├── api.v1                    # Camada de exposição da API REST
│   ├── controller            # Endpoints da aplicação
│   ├── doc                   # Documentação e configuração da API
│   ├── dto                   # Objetos de transferência de dados
│   │   ├── request           # DTOs de entrada
│   │   └── response          # DTOs de saída
│   ├── exception             # Tratamento de exceções da API
│   └── mapper                # Conversão entre DTOs e domínio
│
├── domain                    # Regras de negócio da aplicação
│   ├── exception             # Exceções de domínio
│   ├── model                 # Entidades e modelos de domínio
│   └── service               # Casos de uso e lógica de negócio
│
├── infrastructure            # Implementações técnicas e integrações
│   ├── cache                 # Estratégias de cache
│   ├── config                # Configurações da aplicação
│   ├── redis                 # Integração com Redis
│   ├── ratelimiter           # Controle de taxa com Bucket4j
│   ├── repository            # Persistência de dados
│   └── scheduler             # Tarefas agendadas
│
└── UrlShortenerApplication   # Ponto de entrada da aplicação
```

## Variáveis de Ambiente
Copie o arquivo `.env.example`:

``` bash
cp .env.example .env
```

e defina os valores:

```env
# Server configuration
SERVER_PORT=

# Database configuration
DB_HOST=
DB_USERNAME=
DB_PASSWORD=
DB_NAME=
DB_PORT=

# Redis configuration
REDIS_HOST=
REDIS_PORT=
REDIS_PASSWORD=
```

## Como Executar

### 1. Subir PostgreSQL e Redis

```bash
docker compose up -d
```

### 2. Executar a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação ficará disponível na porta configurada:

```text
http://localhost:8080
```

## Documentação da API

A documentação dos endpoints é gerada automaticamente utilizando Swagger/OpenAPI.

Após iniciar a aplicação, ela pode ser acessada em:

```text
http://localhost:8080/swagger-ui/index.html
```

## Redis

O Redis é utilizado como camada de cache para reduzir consultas ao banco de dados durante o processo de gerenciamento das URLs.

### Estratégia

* Busca a URL no Redis
* Caso exista, retorna diretamente do cache
* Caso não exista, consulta o PostgreSQL
* Armazena o resultado no Redis para futuras consultas

### Exemplo de chave

```text
shortUrls:5beedc72-aeaf-41af-9edd-22c360329f17
```

### Benefícios

* Menor carga no PostgreSQL
* Redução de latência
* Melhor desempenho em URLs acessadas com frequência

## Rate Limiting

O controle de requisições é realizado utilizando Bucket4j com Redis como armazenamento distribuído dos buckets.

### Objetivos

- Evitar abuso da API
- Limitar tráfego excessivo
- Melhorar a estabilidade da aplicação

### Fluxo

```text id="h7w4zh"
Request
   |
   v
Rate Limiter Filter (Bucket4j)
   |
   v
Redis (Bucket State)
   |
   +---- Tokens disponíveis?
            |
            +---- Sim -> Consome token -> Continua requisição
            |
            +---- Não -> HTTP 429 Too Many Requests
```

### Como funciona

1. Uma chave é gerada para identificar o cliente (IP, usuário ou endpoint).
2. O Bucket4j consulta o estado do bucket armazenado no Redis.
3. Caso existam tokens disponíveis, um token é consumido e a requisição continua.
4. Caso o limite seja excedido, a API retorna HTTP 429.
5. Os tokens são reabastecidos automaticamente conforme a política configurada.
