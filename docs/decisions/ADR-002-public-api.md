Decision:
Expose a small public API through a ScoreBoard interface with InMemoryScoreBoard as the default implementation.

startMatch returns MatchId.

updateScore identifies an active match by MatchId and replaces the current score with the provided absolute score. If the MatchId does not identify an active match, updateScore throws MatchNotFoundException.

finishMatch identifies an active match by MatchId and permanently removes it from the summary. If the MatchId does not identify an active match, finishMatch throws MatchNotFoundException.

getSummary returns immutable MatchSummary values.

Reason:
The project is a library, so consumers should depend on a stable contract rather than internal implementation details. MatchId allows later operations to refer to a match without exposing mutable Match objects.

Alternatives considered:
- Single public ScoreBoard class
- Returning internal Match objects
- Returning UUID/String directly instead of MatchId
- Using incremental score events instead of absolute score replacement

Trade-offs:
This adds a few small types, but keeps the API explicit, safer and easier to evolve.
