# SkyBank-Core

SkyBank-Core is a small Java (Maven) sample application implementing core banking domain logic: accounts, transactions and simple authentication. It is intended as a minimal, testable core module (console-based) demonstrating service, domain model and mapping layers.

**Key facts**
- Java: 17
- Build: Maven
- Main class: `org.skybank.core.App` (console entrypoint)
- Mapper: MapStruct (annotation-processed generated code under `target/generated-sources`)
- Lombok used for model boilerplate

**Quick Start**
- Build:

```bash
mvn clean package
```

- Run (from IDE or using the JAR produced by `mvn package`):

```bash
java -cp target/SkyBank-Core-1.0-SNAPSHOT.jar org.skybank.core.App
```

Note: Running from IDE (or `mvn exec:java`) is often easier while developing because annotation processors and generated sources are automatically configured.

- Tests:

```bash
mvn test
```

**Project Structure (important packages)**
- `org.skybank.core.application.dto` — request/response DTOs used by presentation layer
- `org.skybank.core.application.mapper` — MapStruct mappers (generated implementation in `target/generated-sources`)
- `org.skybank.core.domain.model` — `Account`, `Transaction` domain objects
- `org.skybank.core.domain.context` — contexts used across domain
- `org.skybank.core.domain.service` — core service interfaces (`AccountService`, `AuthService`)
- `org.skybank.core.domain.service.implemantation` — service implementations
- `org.skybank.core.presentation` — `ConsolePresenter` that starts the console UI

**Notable classes**
- `org.skybank.core.App` — application entrypoint, wires services and presenter
- `org.skybank.core.presentation.ConsolePresenter` — simple console-based UI
- `org.skybank.core.domain.service.AccountService` and `AuthService` — core service APIs

**Development notes**
- MapStruct and Lombok are configured as annotation processors in `pom.xml`. If you see compilation issues related to generated mappers, run a full `mvn clean compile` to trigger generation.
- Tests use JUnit (4 & Jupiter mix) and Mockito. Use `mvn test` to run them.
