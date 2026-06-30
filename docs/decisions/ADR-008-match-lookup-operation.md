Decision:
Add findMatch(MatchId) as the single custom operation required by the exercise.

Reason:
Consumers that receive a MatchId from startMatch or use it for updates may need to retrieve the current state of that specific active match without reading and filtering the full summary. The operation returns Optional<MatchSummary>, so unknown and finished matches are represented without adding new error semantics or retaining finished match history.

Alternatives considered:
- get number of matches in progress
- find matches by team
- expose finished match history

Trade-offs:
The public API grows by one read-only operation. The operation remains aligned with the existing MatchId-based API and avoids persistence, history, or exposing internal Match state.
