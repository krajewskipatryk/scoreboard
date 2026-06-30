package com.sportradar.scoreboard;

/**
 * Immutable public snapshot of an active match.
 *
 * <p>The summary does not expose internal state used for ordering, such as match start time.</p>
 */
public record MatchSummary(
        MatchId matchId,
        String homeTeam,
        String awayTeam,
        int homeScore,
        int awayScore
) {
}
