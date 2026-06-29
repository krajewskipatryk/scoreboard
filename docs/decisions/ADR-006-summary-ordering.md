# ADR-006: Summary Ordering

## Status

Accepted

## Context

The exercise requires the summary of matches in progress to be ordered by total score descending.

When total score is equal, the most recently started match must appear first.

## Decision

Use explicitly provided match start time and internal `Map<MatchId, Match>` storage.

`startMatch` accepts `Instant startedAt`.

`getSummary` sorts active matches by total score descending, then by `startedAt` descending.

## Reason

Match start time is domain data and may come from an external feed, schedule, or admin workflow.

The scoreboard should not silently replace that domain value with processing time.

`MatchSummary` remains a public immutable read model. Internal `Match` state keeps the data needed for updates, finishing, and ordering.

## Alternatives considered

### Internal start sequence

This is deterministic, but it is not needed for the current requirement once start time is provided explicitly.

### Clock or Instant.now

This avoids changing the public API, but it hides the source of start time and makes tests and integrations depend on processing time.

### List<MatchSummary>

This kept the first steps simple, but `MatchSummary` does not contain `startedAt`, so it no longer represents enough internal state for ordering.

### MatchSummary plus start order metadata

This would preserve the previous internal shape, but it mixes public read models with internal state and makes the model less explicit.

## Trade-offs

The public `startMatch` API changes and callers must provide `startedAt`.

The implementation gains a small internal `Match` state object and map storage.

In exchange, ordering is based on explicit domain data, tests remain deterministic, and the public summary API stays unchanged.

Additionally, it does not support scenario when total score and `startedAt` are the same.
