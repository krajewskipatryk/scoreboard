# Live Football World Cup Scoreboard

## Overview

This project is a Java library implementing a live football scoreboard.

The implementation is intentionally framework-independent and focuses on providing a small public API that can be embedded into another application.

The project is developed incrementally using Test-Driven Development (TDD).

---

# Building and Running

Run the test suite:

```bash
mvn test
```

---

# Assumptions

At the current stage of development, the following assumptions have been made:

* The scoreboard is implemented as an in-memory library.
* The project does not assume any existing persistence model.
* No external infrastructure (database, REST API, messaging, etc.) is required by the exercise.
* A match is identified by a dedicated `MatchId` value object returned when the match is started.

Additional assumptions will be documented as new requirements are implemented.

---

# Design Decisions

Detailed decision records are kept in `docs/decisions/`.

Current ADRs:

* `docs/decisions/ADR-001-library-scope.md`
* `docs/decisions/ADR-002-public-api.md`
* `docs/decisions/ADR-003-testing-approach.md`

Use the ADRs as the primary direction point for decisions already made. The README summarizes the same direction at a higher level.

---

## Library Scope

The exercise requests a Java library rather than a standalone application.

Therefore the implementation intentionally does not include:

* Spring Boot
* REST controllers
* database access
* application configuration

### Trade-off

The library remains framework-independent and can be embedded into different applications. Consumers are responsible for creating and wiring the implementation.

---

## Encapsulation

The public API does not expose mutable internal match objects.

At the current implementation step, starting a match stores enough in-memory state to return an immutable `MatchSummary`.

More explicit internal domain objects should be introduced only when a later requirement needs behavior that justifies them.

### Trade-off

This keeps the first implementation small and focused. The trade-off is that future score update or finish-match behavior may require introducing an internal `Match` model later.

### ADR

See `docs/decisions/ADR-002-public-api.md`.

---

## Public API

The public API is represented by the `ScoreBoard` interface.

`InMemoryScoreBoard` is the default implementation.

### Reason

This separates the public contract from the implementation while keeping the implementation simple.

### Trade-off

The interface introduces one additional abstraction, but allows the library implementation to evolve without changing consumer code.

### ADR

See `docs/decisions/ADR-002-public-api.md`.

---

## Testing

The project follows Test-Driven Development.

JUnit 5 with AssertJ was selected.

### Alternatives considered

* Spock

### Trade-off

JUnit 5 keeps the project fully Java-based and avoids introducing Groovy into a relatively small library.

### ADR

See `docs/decisions/ADR-003-testing-approach.md`.

---

# Future Decisions

Some design decisions intentionally remain open because they depend on later implementation stages.

Examples include:

* summary ordering strategy
* concurrency strategy
* validation rules
* persistence integration
* additional custom operation

These decisions will be documented once they become part of the implementation.
