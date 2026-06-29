package com.sportradar.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryScoreBoard implements ScoreBoard {

    private final List<MatchSummary> matches = new ArrayList<>();

    @Override
    public MatchId startMatch(String homeTeam, String awayTeam) {
        MatchId matchId = new MatchId(UUID.randomUUID());
        matches.add(new MatchSummary(matchId, homeTeam, awayTeam, 0, 0));
        return matchId;
    }

    @Override
    public List<MatchSummary> getSummary() {
        return List.copyOf(matches);
    }
}
