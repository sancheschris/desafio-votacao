# Votação API

## Como rodar

1. Clone o repositório
2. Acesse a pasta do projeto
3. Crie um arquivo .env na raiz do projeto com as seguintes variáveis de ambiente:

```
POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha
POSTGRES_DB=seu_banco_de_dados
RABBITMQ_DEFAULT_USER=seu_usuario_rabbitmq
RABBITMQ_DEFAULT_PASS=sua_senha_rabbitmq
```
3. Rode o docker-compose para subir o banco de dados com o comando `docker-compose up -d`
4. Execute o comando `mvn spring-boot:run` para iniciar a aplicação
5. A API estará disponível em `http://localhost:8080` ou no `http://localhost:8080/swagger-ui/index.html` para acessar a documentação da API

## Estrutura do Projeto

Neste projeto, utilizei o padrão de camadas para organizar o código, seguindo as melhores práticas de desenvolvimento. A estrutura do projeto é a seguinte:

- `backend/src/main/java`: Aplicação principal e configuração do Spring Boot
- `controller`: Camada responsável por receber as requisições HTTP e retornar as respostas
- `service`: Camada responsável pela lógica de negócio da aplicação
- `repository`: Camada responsável pela persistência dos dados, utilizando Spring Data JPA
- `model`: Camada responsável pelas entidades do domínio, representando as tabelas do banco de dados
- `dto`: Camada responsável pelos objetos de transferência de dados, utilizados para comunicação entre as camadas e para a exposição da API
- `integration`Camada responsável pela integração com serviços externos, como o serviço de votação
- `exception`: Camada responsável pelo tratamento de exceções personalizadas da aplicação
- `config`: Camada responsável pela configuração do Spring Boot
- `src/main/resources`: Pasta onde ficam os arquivos de configuração, como `application.properties`, e os scripts de inicialização do banco de dados (Flyway)
- `src/test/test`: Pasta onde ficam os testes automatizados da aplicação

## Dependencias

- Spring Boot Starter Web: Para criar a API RESTful
- Spring Boot Starter Data JPA: Para facilitar a integração com o banco de dados
- Spring Boot Starter Test: Para facilitar a criação de testes automatizados
- PostgreSQL Driver: Para conectar a aplicação ao banco de dados PostgreSQL
- Flyway: Para gerenciar as migrações do banco de dados
- Lombok: Para reduzir a verbosidade do código, gerando getters, setters e construtores automaticamente
- Swagger: Para gerar a documentação da API de forma automática
- Mockito: Para facilitar a criação de testes unitários e de integração, permitindo a simulação de dependências e comportamentos em testes.
- JUnit: Para criar e executar os testes automatizados da aplicação
- Sl4j: Para facilitar o registro de logs na aplicação, permitindo a utilização de diferentes implementações de logging, como Logback ou Log4j, e proporcionando uma interface simples para registrar mensagens de log em diferentes níveis (info, debug, error, etc).


### Considerações Finais
- Estruturei a aplicação seguindo uma organização em camadas (controller, service, repository, etc.), separando bem responsabilidades e mantendo o código simples e coeso.
- Documentei a API com Swagger para facilitar testes e o entendimento dos endpoints.
- Implementei testes automatizados para validar os principais fluxos, utilizando Mockito para isolar dependências quando necessário.
- Padronizei o tratamento de exceções usando um GlobalExceptionHandler, garantindo respostas HTTP mais claras e consistentes.
- Para a integração externa (tarefa bônus 1), criei um Client/Fake que simula a validação de CPF, retornando resultados aleatórios conforme o enunciado.
- Pensei também em performance, principalmente na contagem de votos e no uso de constraints no banco, considerando cenários com alto volume de requisições.
- A API foi versionada via path (/api/v1), permitindo evolução futura sem quebrar contratos já existentes.
- Adicionei RabbitMQ para simular a comunicação assíncrona com o serviço de votação
- Implementei um job automático que roda a cada 5 segundos, verificando se existem sessões de votação abertas e expiradas. 
Quando encontra uma sessão que precisa ser encerrada, ele atualiza o status e publica um evento no RabbitMQ, 
permitindo que outros sistemas sejam notificados de forma assíncrona.