Decision:
Expose a small public API through a ScoreBoard interface with InMemoryScoreBoard as the default implementation.

startMatch accepts home team, away team and explicit startedAt Instant, then returns MatchId. If either team is already in an active match, startMatch throws TeamAlreadyPlayingException.

updateScore identifies an active match by MatchId and replaces the current score with the provided absolute score. If the MatchId does not identify an active match, updateScore throws MatchNotFoundException.

finishMatch identifies an active match by MatchId and permanently removes it from the summary. If the MatchId does not identify an active match, finishMatch throws MatchNotFoundException.

findMatch identifies an active match by MatchId and returns an Optional containing its immutable MatchSummary. It returns Optional.empty() when the match is unknown or has already been finished.

getSummary returns immutable MatchSummary values.

Reason:
The project is a library, so consumers should depend on a stable contract rather than internal implementation details. MatchId allows later operations to refer to a match without exposing mutable Match objects.

Alternatives considered:
- Single public ScoreBoard class
- Returning internal Match objects
- Returning UUID/String directly instead of MatchId
- Letting the scoreboard assign start time internally
- Using incremental score events instead of absolute score replacement
- Requiring consumers to read and filter the full summary for single-match lookup

Trade-offs:
This adds a few small types, but keeps the API explicit, safer and easier to evolve.
