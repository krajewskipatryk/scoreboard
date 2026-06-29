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
* Match start time is provided explicitly as `Instant startedAt`; the scoreboard does not use processing time.
* Updating a match score uses absolute home and away scores, not score deltas or events.
* Updating a score for an unknown `MatchId` fails with `MatchNotFoundException`.
* Match and score history is not needed
* Finishing a match permanently removes it from the active scoreboard instead of retaining it with a finished state.
* If two active matches have the same total score and the same `startedAt`, their relative order is not part of the public contract.

Additional assumptions will be documented as new requirements are implemented.

---

# Design Decisions

Detailed decision records are kept in `docs/decisions/`.

Current ADRs:

* `docs/decisions/ADR-001-library-scope.md`
* `docs/decisions/ADR-002-public-api.md`
* `docs/decisions/ADR-003-testing-approach.md`
* `docs/decisions/ADR-004-score-update-semantics.md`
* `docs/decisions/ADR-005-finish-match-semantics.md`
* `docs/decisions/ADR-006-summary-ordering.md`

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

At the current implementation step, the scoreboard stores active matches as internal `Match` state and maps them to immutable `MatchSummary` values when returning a summary.

The public API still does not expose mutable internal match state.

### Trade-off

This keeps the public read model stable while allowing internal state to include data needed for update, finish, and ordering behavior.

### ADR

See `docs/decisions/ADR-002-public-api.md`.

Score update semantics are covered by `docs/decisions/ADR-004-score-update-semantics.md`.

Finish match semantics are covered by `docs/decisions/ADR-005-finish-match-semantics.md`.

Summary ordering and explicit start time semantics are covered by `docs/decisions/ADR-006-summary-ordering.md`.

---

## Public API

The public API is represented by the `ScoreBoard` interface.

`InMemoryScoreBoard` is the default implementation.

Current operations:

* `startMatch(String homeTeam, String awayTeam, Instant startedAt)`
* `updateScore(MatchId matchId, int homeScore, int awayScore)`
* `finishMatch(MatchId matchId)`
* `getSummary()`

`getSummary` returns active matches ordered by total score descending, then by most recently started match.

`updateScore` throws `MatchNotFoundException` when the provided `MatchId` does not identify an active match.

`finishMatch` throws `MatchNotFoundException` when the provided `MatchId` does not identify an active match.

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

* concurrency strategy
* validation rules
* persistence integration
* additional custom operation

These decisions will be documented once they become part of the implementation.
