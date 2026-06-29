package com.sportradar.scoreboard;

public record MatchSummary(
        MatchId matchId,
        String homeTeam,
        String awayTeam,
        int homeScore,
        int awayScore
) {
}
