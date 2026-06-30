package com.sportradar.scoreboard;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ScoreBoard {

    MatchId startMatch(String homeTeam, String awayTeam, Instant startedAt);

    void updateScore(MatchId matchId, int homeScore, int awayScore);

    void finishMatch(MatchId matchId);

    Optional<MatchSummary> findMatch(MatchId matchId);

    List<MatchSummary> getSummary();
}
