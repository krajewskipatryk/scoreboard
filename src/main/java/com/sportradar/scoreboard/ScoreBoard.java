package com.sportradar.scoreboard;

import java.time.Instant;
import java.util.List;

public interface ScoreBoard {

    MatchId startMatch(String homeTeam, String awayTeam, Instant startedAt);

    void updateScore(MatchId matchId, int homeScore, int awayScore);

    void finishMatch(MatchId matchId);

    List<MatchSummary> getSummary();
}
