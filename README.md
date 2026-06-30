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
* Match and score history is not needed.
* Finishing a match permanently removes it from the active scoreboard instead of retaining it with a finished state.
* If two active matches have the same total score and the same `startedAt`, their relative order is not part of the public contract.
* A team can be part of only one active match at a time.
* Active team duplication is checked using exact team-name equality.

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
* `docs/decisions/ADR-007-validation-and-thread-safety.md`
* `docs/decisions/ADR-008-match-lookup-operation.md`

Use the ADRs as the primary direction point for decisions already made. The README summarizes the same direction at a higher level.

---

## Library Scope

The exercise requests a Java library rather than a standalone application. Therefore the implementation intentionally does not include:

* Spring Boot
* REST controllers
* database access
* application configuration

### Trade-off

The library remains framework-independent and can be embedded into different applications. Consumers are responsible for creating and wiring the implementation.

---

## Package Structure

The project currently uses a single package: `com.sportradar.scoreboard`.

This is intentional. The library is small enough that splitting types into `api`, `model`, `exception`, or `internal` packages would add structure without solving a current problem. Internal implementation details are protected by Java visibility where needed.

### Trade-off

A flat package is simple and easy to navigate for the current scope. If the library grows with more implementations or internal collaborators, package boundaries can be introduced when they provide clearer separation.

---

## Encapsulation

The public API does not expose mutable internal match objects. Active matches are stored as internal `Match` state and mapped to immutable `MatchSummary` values for reads.

### Trade-off

This keeps the public read model stable while allowing internal state to include data needed for update, finish, and ordering behavior.

## Public API

The public API is represented by the `ScoreBoard` interface.

`InMemoryScoreBoard` is the default implementation.

Current operations:

* `startMatch(String homeTeam, String awayTeam, Instant startedAt)`
* `updateScore(MatchId matchId, int homeScore, int awayScore)`
* `finishMatch(MatchId matchId)`
* `Optional<MatchSummary> findMatch(MatchId matchId)`
* `getSummary()`

`getSummary` returns active matches ordered by total score descending, then by most recently started match.

`updateScore` throws `MatchNotFoundException` when the provided `MatchId` does not identify an active match.

`finishMatch` throws `MatchNotFoundException` when the provided `MatchId` does not identify an active match.

`startMatch` throws `TeamAlreadyPlayingException` when either team is already in an active match.

### Reason

This separates the public contract from the implementation while keeping the implementation simple.

### Trade-off

The interface introduces one additional abstraction, but allows the library implementation to evolve without changing consumer code.

### ADR

See `docs/decisions/ADR-002-public-api.md`.

---

## Additional Operation: Match Lookup

The additional operation added to the scoreboard is:

```java
Optional<MatchSummary> findMatch(MatchId matchId)
```

This operation allows consumers to retrieve the current state of a single active match without reading the full scoreboard summary.

I chose this feature because the public API already identifies matches with `MatchId`, and consumers that start or update a match may reasonably need to query that specific match later.

Finished matches are not retained by the current implementation, so `findMatch` returns `Optional.empty()` for unknown or finished matches.

### Trade-off

This slightly expands the public API, but keeps the feature aligned with the scoreboard domain and avoids introducing persistence, history, or unrelated infrastructure.

### ADR

See `docs/decisions/ADR-008-match-lookup-operation.md`.

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
