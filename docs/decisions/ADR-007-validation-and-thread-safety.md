# ADR-007: Validation and Thread Safety

## Status

Accepted

## Context

The scoreboard must reject starting a new match when either team is already playing in an active match.

The check and match creation must be safe when multiple callers attempt to start matches concurrently.

## Decision

Reject duplicate active teams with `TeamAlreadyPlayingException`.

Use exact team-name equality for the current validation rule.

Synchronize public `InMemoryScoreBoard` operations to make validation, mutation and summary reads atomic for this in-memory implementation.

## Reason

The current requirement only defines duplicate active-team validation. It does not define blank names, case-insensitive comparison, aliases, negative scores or team normalization.

`synchronized` is the smallest thread-safety mechanism that protects the check-then-create flow and gives `getSummary` a consistent snapshot while sorting.

## Alternatives considered

### ConcurrentHashMap

Concurrent maps protect individual map operations, but duplicate-team validation requires checking all active matches before inserting a new one.

That check-and-insert flow would still need additional coordination.

### ReentrantReadWriteLock

This would allow multiple concurrent summary reads, but adds more lock management than the current requirements justify.

### Document InMemoryScoreBoard as not thread-safe

This is simpler, but an internal library may reasonably be shared by multiple callers. The current validation requirement makes unsafe concurrent starts visible.

### Team value object

This may become useful when normalization or richer team validation is required, but current validation uses exact names only.

## Trade-offs

`synchronized` serializes scoreboard operations, which is not the most scalable approach.

It keeps the implementation simple and correct for the current in-memory library.
