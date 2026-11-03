# Financial Assistant

Spring Boot REST API for managing financial advice.

## Stack

- Java 21
- Spring Boot 4.0.0 (Web, Data JPA)
- H2 (in-memory database) + H2 console

## Running the application

```bash
./mvnw spring-boot:run
```

The application starts on http://localhost:8080.

## Endpoints

| Method | URL                     | Description              |
|--------|-------------------------|--------------------------|
| GET    | `/api/financial/advice` | List all advice          |
| POST   | `/api/financial/advice` | Add a new advice (JSON)  |

Example JSON body:

```json
{
  "category": "Savings",
  "advice": "Save 20% of your income",
  "priority": "HIGH"
}
```

Send the sample file `advice.json`:

```bash
curl -X POST http://localhost:8080/api/financial/advice -H "Content-Type: application/json" --data-binary @advice.json
```

> On Windows, use `--data-binary @file.json` instead of `-d '...'` when the JSON contains non-ASCII characters (e.g. accents): the command line does not send them as UTF-8 and the server responds with `400 Bad Request`.

## H2 console

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:financialdb`
- Username: `sa`
- Password: *(empty)*

The database is in-memory (`create-drop`): all data is lost on every restart.
