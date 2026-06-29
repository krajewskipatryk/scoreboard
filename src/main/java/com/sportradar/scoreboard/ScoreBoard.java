package com.sportradar.scoreboard;

import java.util.List;

public interface ScoreBoard {

    MatchId startMatch(String homeTeam, String awayTeam);

    void updateScore(MatchId matchId, int homeScore, int awayScore);

    List<MatchSummary> getSummary();
}
