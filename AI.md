# AI Usage

## Summary

AI was used as an engineering assistant during the early design phase of the project.

The collaboration focused on:

* analysing the exercise requirements,
* identifying ambiguities,
* comparing architectural alternatives,
* defining engineering principles,
* planning the TDD workflow,
* reviewing design decisions.

Human judgment was used to evaluate and either accept or reject AI suggestions.

---

## Tools Used

* ChatGPT
* Codex (IntelliJ IDEA)

---

## Prompt History

Only prompts that materially shaped the implementation or review process are included.

### Initial implementation direction

```text
Read and follow ENGINEERING_PRINCIPLES.md.

We are implementing a Java Maven library for the Live Football World Cup Scoreboard coding exercise.

Work incrementally using TDD.

Important rules:
- Do not generate the whole solution at once.
- Implement only the next requirement.
- Start with a failing test.
- Add the smallest production code needed to pass it.
- Refactor only after tests are green.
- Do not introduce Spring Boot, REST API, database, JPA, DAO, repository layer, event sourcing, CQRS, or unnecessary architecture.
- Do not silently make assumptions. If a requirement is ambiguous, state the assumption before implementing.
- For non-trivial decisions, provide a short rationale using:
  Decision / Reason / Alternatives considered / Trade-offs.
- Challenge unnecessary complexity.

Current implementation step:

Branch/commit: 01-start-match

Requirement:
Implement starting a new match.

Expected behavior:
- A new match can be started.
- A started match appears in the scoreboard summary.
- A newly started match has score 0-0.
- The match can be identified later for score updates and finishing.
- Do not implement score update yet.
- Do not implement finish match yet.
- Do not implement ordering beyond what is needed for this first behavior.
- Do not implement the additional custom operation yet.

Before writing code:
1. Propose the first failing test.
2. Explain any assumption needed for this step.
3. Wait for confirmation if the assumption affects public API.

Preferred project direction, unless you identify a better alternative:
- Plain Java Maven library.
- JUnit 5 + AssertJ.
- Public API should be small and library-oriented.
- Consider whether `ScoreBoard` should be an interface and `InMemoryScoreBoard` the default implementation, but do not over-engineer.
- Avoid exposing mutable internal state.
```

### Confirmed public API and TDD scope

```text
Confirmed, with one naming change.

Use this public API shape:

- `ScoreBoard` as the public interface.
- `InMemoryScoreBoard` as the default implementation.
- `startMatch(String homeTeam, String awayTeam)` returns a `MatchId`.
- `getSummary()` returns immutable `MatchSummary` values.
- `MatchSummary` should include match id, home team, away team, home score and away score.
- Do not expose mutable internal `Match` objects.

Please use `getSummary()` instead of `summary()` to align with the wording of the exercise and common Java API conventions.

Proceed with TDD for branch/commit `01-start-match`:
1. Add the failing test for starting a match with initial score 0-0.
2. Add the smallest production code needed to make it pass.
3. Do not implement score update, finish match, ordering beyond this single match case, validation, concurrency, or the additional operation yet.
4. Refactor only after tests are green.
```

### Documentation review requirements

```text
Following instructions from ENGINEERING_PRINCIPLES.md, create a code review for existing changes.

Remember to check previous architectural decisions.

Review created AI.md, and README.md if they follow below requirements:

Create a README.md documenting:
• Your assumptions
• Your reasoning
• Trade-offs made
Create a AI.md that includes:
• Short summary of how AI tools were used
• Include your prompt history and other contextual information
• Any artifact that guided the implementation

Review created ADR files and check against existing solution decisions made.
```

### Documentation update request

```text
Review code as well, not only parts added manually. Apply suggested changes:
1. Include information about ADR files in documentation as a direction point to decisioning made
2. Add prompt history to the AI.md file, but only important ones. Please include full prompt, not summarized version
```

---

## Artifacts That Guided the Implementation

* ENGINEERING_PRINCIPLES.md
* README.md
* docs/decisions/ADR-001-library-scope.md
* docs/decisions/ADR-002-public-api.md
* docs/decisions/ADR-003-testing-approach.md
* The current Maven source tree under `src/main/java` and `src/test/java`

---

## Contextual Information

The implementation step reviewed here is `01-start-match`.

The public API was confirmed by the human reviewer before implementation:

* `ScoreBoard` is the public interface.
* `InMemoryScoreBoard` is the default implementation.
* `startMatch(String homeTeam, String awayTeam)` returns `MatchId`.
* `getSummary()` returns immutable `MatchSummary` values.
* Score update, finish match, ordering, validation, concurrency and the additional custom operation are intentionally out of scope for this step.
