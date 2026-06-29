# ADR-005: Finish Match Semantics

## Status

Accepted

## Context

The exercise requires finishing an existing match.

The requirement defines the scoreboard summary as active matches, and finished matches should be removed from that summary.

## Decision

`finishMatch` permanently removes the match from the active scoreboard.

Finished matches are not retained with a finished state.

If the provided `MatchId` does not identify an active match, `finishMatch` fails with `MatchNotFoundException`.

## Reason

The current library models only live scoreboard state. The requirement does not ask for match history, auditing, or querying finished matches.

Permanent removal keeps `getSummary` focused on active matches and avoids introducing state transitions that are not required yet.

## Alternatives considered

### Retain finished matches with status

This would keep a full list of matches and mark finished ones with a status.

It could support future history queries, but the current requirements do not ask for history or finished-match lookup.

### Move finished matches to a history collection

This would preserve completed matches separately from active matches.

It adds storage and API questions that are outside the current exercise step.

## Trade-offs

Permanent removal keeps the implementation small and aligned with the live scoreboard requirement.

The trade-off is that finished matches cannot be queried after completion. If history becomes a requirement later, it should be introduced explicitly.
