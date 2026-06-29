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

### Update score implementation direction

```text
Read and follow ENGINEERING_PRINCIPLES.md.

We are continuing the Live Football World Cup Scoreboard library using TDD.

Current step / commit:
02-update-score

Current state:
- ScoreBoard has startMatch(String homeTeam, String awayTeam)
- startMatch returns MatchId
- getSummary() returns List<MatchSummary>
- InMemoryScoreBoard currently stores MatchSummary values in memory
- Do not redesign the whole solution upfront

Requirement for this step:
Implement updating the score of an existing active match.

Expected behavior:
- ScoreBoard should expose an updateScore operation.
- updateScore should identify the match by MatchId.
- updateScore should replace the current score with the provided absolute score.
- getSummary() should reflect the updated score.
- Do not implement finish match yet.
- Do not implement summary ordering beyond what is required by current tests.
- Do not implement validation beyond what this step requires.
- Do not implement concurrency changes yet unless the current design makes the update unsafe or impossible.

Work in TDD:
1. Add one failing test for updating an existing match score.
2. Propose the minimal public API change needed.
3. Add the smallest production code needed to pass the test.
4. Refactor only after tests are green.
5. If you propose replacing List<MatchSummary> with another internal structure, explain the decision using:
   Decision / Reason / Alternatives considered / Trade-offs.

Important design constraint:
Do not expose mutable internal Match objects from the public API.
```

### Unknown match update behavior

```text
Update the 02-update-score step.

In addition to updating an existing match, include one failing test for updating an unknown MatchId.

Expected behavior:
- updateScore replaces the score for an existing active match.
- updateScore fails when the provided MatchId does not exist.
- Prefer a clear public API error, e.g. MatchNotFoundException.
- Do not add negative score validation yet unless it is required by the current test.
- Do not implement finish match, ordering, concurrency, or additional operation yet.

Use TDD: add failing tests first, then minimal implementation.
```

### Finish match implementation direction

```text
Current step / commit:
03-finish-match

Current state:
- ScoreBoard has startMatch(String homeTeam, String awayTeam)
- startMatch returns MatchId
- getSummary() returns List<MatchSummary>
- InMemoryScoreBoard currently stores MatchSummary values in memory
- Do not redesign the whole solution upfront
- updateScore updates match score

Requirement for this step:
Implement finish of existing match

Expected behavior:
- ScoreBoard exposes finishMatch(MatchId matchId).
- Finished match is removed from getSummary().
- Do not retain finished matches.
- finishMatch() should throw an exception when match is not found.
- Do not implement summary ordering beyond what is required by current tests.
- Do not implement validation beyond what this step requires.
- Do not implement concurrency changes yet unless the current design makes the update unsafe or impossible.

Additional requirements:
- Add documentation entry that removal is permanent instead of changing state as an assumption of the expected flow requirements

Work in TDD:
1. Add one failing test for finishing an existing match.
2. Add one failing test for finishing a non-existing match.
3. Propose the minimal public API change needed.
4. Add the smallest production code needed to pass the test.
5. Refactor only after tests are green.
6. If you propose replacing List<MatchSummary> with another internal structure, explain the decision using:
Decision / Reason / Alternatives considered / Trade-offs.
```

### Summary ordering implementation direction

```text
Read and follow ENGINEERING_PRINCIPLES.md.

We are continuing the Live Football World Cup Scoreboard library using TDD.

Project context:
- This is a plain Java Maven library.
- The implementation is developed incrementally.
- Each commit should represent one completed behavior.
- Architectural decisions should be documented separately as ADRs.
- Keep the solution simple, explicit, and justified by current requirements.

Current step / commit:
04-get-summary-ordering

Current requirement:
Implement getSummary ordering for matches in progress.

Ordering rules:
1. Total score descending.
2. If total score is equal, most recently started match first.

Additional implementation decisions for this step:
1. Match start time should be provided explicitly when starting a match.
   Do not use Clock or Instant.now() inside the scoreboard.

2. Refactor internal storage from a list-based representation to:
   Map<MatchId, Match>

   Match should be an internal/domain state object.
   MatchSummary should remain an immutable public read model returned by getSummary().

Expected public API change:
Prefer this startMatch signature:

    MatchId startMatch(String homeTeam, String awayTeam, Instant startedAt);

If you think long startedAtEpochMillis is better than Instant, challenge the decision before implementing.

Rationale:
- Match start time is domain data.
- In an integrated system, the start time may come from an external feed, scheduler, or admin workflow.
- The scoreboard should not silently replace domain start time with processing time.
- Tests should remain deterministic.

Internal model direction:
- Use Map<MatchId, Match> for active matches.
- Match should contain:
  - MatchId
  - home team
  - away team
  - current score
  - startedAt
- MatchSummary should be created from Match when getSummary() is called.
- Do not store MatchSummary as internal mutable state.

TDD workflow:
1. First update existing tests to use the new startMatch signature.
2. Add a failing test for the official summary ordering example.
3. Implement the smallest production code needed.
4. Refactor only after tests are green.

Constraints:
- Do not implement the additional custom operation.
- Do not introduce persistence.
- Do not introduce Spring Boot, REST, JPA, DAO, Repository, CQRS, or event sourcing.
- Do not implement unrelated validation unless required by current tests.
- Do not introduce concurrency changes in this step unless the refactor makes them necessary for correctness.

Decision documentation:
Before implementation, briefly document the decision using this format:

Decision:
Use explicitly provided match start time and internal Map<MatchId, Match> storage.

Reason:
...

Alternatives considered:
- internal start sequence
- Clock / Instant.now()
- List<MatchSummary>
- MatchSummary plus start order metadata

Trade-offs:
...

After documenting the decision, proceed with implementation.
```

### Validation implementation direction

```text
Read and follow ENGINEERING_PRINCIPLES.md.

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
- Challenge unnecessary complexity

Current step: 05-add-validation

Current state:
- ScoreBoard has startMatch(String homeTeam, String awayTeam)
- startMatch returns MatchId
- getSummary() returns List<MatchSummary>
- InMemoryScoreBoard currently stores MatchSummary values in memory
- Do not redesign the whole solution upfront
- updateScore updates match score
- finishMatch removes match from getSummary()
- getSummary() return list of in-play matches sorted by totalScore and startedAt

Requirement for this step:
Implement additional validation to ScoreBoard

Expected behavior:
- startMatch(String homeTeam, String awayTeam) should not allow to create new match with duplicated any of the teams.

Additionally:
- Suggest any validation that can be added to the project.

Work in TDD:
1. Add tests to verify validation failures
2. Create tests failing the thread safety
3. Propose the minimal public API change needed.
4. Add the smallest production code needed to pass the test.
5. Refactor only after tests are green.
```

---

## Artifacts That Guided the Implementation

* ENGINEERING_PRINCIPLES.md
* README.md
* docs/decisions/ADR-001-library-scope.md
* docs/decisions/ADR-002-public-api.md
* docs/decisions/ADR-003-testing-approach.md
* docs/decisions/ADR-004-score-update-semantics.md
* docs/decisions/ADR-005-finish-match-semantics.md
* docs/decisions/ADR-006-summary-ordering.md
* docs/decisions/ADR-007-validation-and-thread-safety.md
* The current Maven source tree under `src/main/java` and `src/test/java`

---

## Contextual Information

The implementation steps reviewed here are `01-start-match`, `02-update-score`, `03-finish-match`, `04-get-summary-ordering`, and `05-add-validation`.

The public API was confirmed by the human reviewer before implementation:

* `ScoreBoard` is the public interface.
* `InMemoryScoreBoard` is the default implementation.
* `startMatch(String homeTeam, String awayTeam, Instant startedAt)` returns `MatchId`.
* `updateScore(MatchId matchId, int homeScore, int awayScore)` replaces the current score with absolute values.
* `updateScore` throws `MatchNotFoundException` when the provided `MatchId` does not identify an active match.
* `finishMatch(MatchId matchId)` permanently removes an active match from the scoreboard.
* `finishMatch` throws `MatchNotFoundException` when the provided `MatchId` does not identify an active match.
* `getSummary()` returns immutable `MatchSummary` values ordered by total score descending, then `startedAt` descending.
* `startMatch` throws `TeamAlreadyPlayingException` when either team is already in an active match.
* `InMemoryScoreBoard` synchronizes public operations to make duplicate-team validation atomic for concurrent callers.
* Additional validation and the additional custom operation are intentionally out of scope for the current steps.
