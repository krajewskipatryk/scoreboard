package com.sportradar.scoreboard;

import java.time.Instant;

record Match(
        MatchId matchId,
        String homeTeam,
        String awayTeam,
        int homeScore,
        int awayScore,
        Instant startedAt
) {

    Match withScore(int newHomeScore, int newAwayScore) {
        return new Match(matchId, homeTeam, awayTeam, newHomeScore, newAwayScore, startedAt);
    }

    int totalScore() {
        return homeScore + awayScore;
    }

    MatchSummary toSummary() {
        return new MatchSummary(matchId, homeTeam, awayTeam, homeScore, awayScore);
    }
}
