# User API - Focus TESTING JUnit & Mockito

API RESTful para gestão de usuários construída em Java 17 e Spring Boot 4.1.1. O projeto demonstra a aplicação de boas práticas de arquitetura, tratamento global de exceções (RFC 7807) e uma suíte completa de testes unitários e de integração com cobertura das regras de negócio.

## 🛠 Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 4.1.1
* Spring WebMVC
* Spring Data JPA
* Spring Validation


* **Banco de Dados:** H2 Database (In-Memory) com H2 Console habilitado
* **Boilerplate:** Lombok
* **Testes:** JUnit 5, Mockito, Spring MockMvc, DataJpaTest

## ⚙️ Arquitetura e Padrões Aplicados

* **Injeção de Dependências:** Realizada via construtor utilizando `@RequiredArgsConstructor` do Lombok, garantindo imutabilidade e facilitando testes unitários.
* **Validação de Dados (Bean Validation):** Anotações (`@NotBlank`, `@Email`) na camada de entidade/DTO para rejeitar payloads malformados antes de atingirem a regra de negócio.
* **Tratamento Global de Exceções:** Utilização de `@ControllerAdvice` para capturar exceções (`UserNotFoundException`, `DataIntegrityViolationException`, `MethodArgumentNotValidException`) e padronizar o retorno HTTP utilizando um `Record` (`ErrorResponse`).
* **Testes Estratificados:**
* *Unitários:* Foco no isolamento da camada `Service` utilizando Mockito (`@Mock`, `@InjectMocks` e `verify`).
* *Integração:* Foco no fluxo ponta a ponta na camada `Controller` utilizando módulos do `spring-boot-starter-webmvc-test` e `MockMvc`.
* *Repositório:* Foco na persistência de dados utilizando fatias de teste isoladas com o módulo `spring-boot-starter-data-jpa-test`.



## 🚀 Como Executar

**Pré-requisitos:** Java 17 e Maven instalados.

1. Clone o repositório.
2. Na raiz do projeto, execute o comando para iniciar a aplicação:

```bash
mvn spring-boot:run

```

3. A API estará disponível em `http://localhost:8080`. O banco H2 será recriado a cada inicialização.

## 🧪 Como Rodar os Testes e a Cobertura

Para executar a suíte completa de testes via terminal, utilize o comando:

```bash
mvn clean test

```

*Nota: A visualização da cobertura de código (linhas e classes analisadas) deve ser executada e verificada diretamente através do motor nativo da sua IDE (ex: ferramenta "Run with Coverage" do IntelliJ IDEA).*

## 📌 Endpoints da API

### Criar Usuário

* **POST** `/users`
* **Content-Type:** `application/json`
* **Payload de Sucesso:**

```json
{
  "name": "Carlos Silva",
  "email": "carlos@email.com"
}

```

* **Respostas Esperadas:**
* `201 Created`: Usuário criado com sucesso.
* `400 Bad Request`: Falha de validação (ex: e-mail inválido, nome em branco).
* `409 Conflict`: E-mail já cadastrado no banco de dados.



### Buscar Usuário por ID

* **GET** `/users/{id}`
* **Respostas Esperadas:**
* `200 OK`: Retorna os dados do usuário.
* `404 Not Found`: Usuário não encontrado no banco de dados.
