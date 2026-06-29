# ADR-004: Score Update Semantics

## Status

Accepted

## Context

The exercise requires the scoreboard to support updating the score of a match.

The requirement does not explicitly state whether updating the score should mean:

* replacing the current score with a new absolute value, or
* incrementing one side's score when a goal is scored.

## Decision

`updateScore` replaces the current score with the provided absolute score.

If the provided `MatchId` does not identify an active match, `updateScore` fails with `MatchNotFoundException`.

Example:

```java
scoreBoard.updateScore(matchId, 2, 1);
```

sets the match score to `2-1`.

## Reason

The exercise examples describe matches being updated with complete scores, such as `Mexico 0 - Canada 5`.

In a scoreboard library, the public state is the current score. Accepting the full score makes the operation simple and idempotent when the same update is repeated.

This approach also fits systems that receive the latest known score from an external feed rather than individual goal events.

Failing on an unknown `MatchId` gives library consumers a clear public API error instead of silently ignoring a request that did not update the scoreboard.

## Alternatives Considered

### Incremental Goal Updates

Example:

```java
scoreBoard.homeTeamScored(matchId);
```

or

```java
scoreBoard.addGoal(matchId, TeamSide.HOME);
```

This approach models scoring events more explicitly and naturally supports event-driven workflows.

However, the exercise does not require goal history, event sourcing, or replaying individual scoring events. Introducing goal-based operations would increase the public API surface and make repeated updates harder to handle in the presence of retries.

### Team-Specific Score Updates

Example:

```java
scoreBoard.updateScore(matchId, TeamSide.HOME, 2);
```

This approach allows updating one team's score independently.

However, it temporarily makes the match state dependent on previous values and requires multiple calls to represent a complete score update. It also introduces additional API complexity without addressing a requirement from the exercise.

## Trade-offs

Replacing the entire score keeps the API small, explicit, and idempotent. Repeating the same update always produces the same result, which is beneficial when score updates originate from external systems.

The trade-off is that the library models the current scoreboard state rather than individual scoring events. It does not preserve goal history or expose goal-level operations.

Unknown-match handling is explicit, but score validation such as rejecting negative values is intentionally left for a later requirement.
