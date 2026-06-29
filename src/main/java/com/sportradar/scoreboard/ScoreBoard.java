package com.sportradar.scoreboard;

import java.util.List;

public interface ScoreBoard {

    MatchId startMatch(String homeTeam, String awayTeam);

    List<MatchSummary> getSummary();
}
