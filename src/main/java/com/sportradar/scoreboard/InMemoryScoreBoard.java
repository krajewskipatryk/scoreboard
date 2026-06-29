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
    public void updateScore(MatchId matchId, int homeScore, int awayScore) {
        for (int index = 0; index < matches.size(); index++) {
            MatchSummary match = matches.get(index);
            if (match.matchId().equals(matchId)) {
                matches.set(index, new MatchSummary(matchId, match.homeTeam(), match.awayTeam(), homeScore, awayScore));
                return;
            }
        }
        throw new MatchNotFoundException(matchId);
    }

    @Override
    public List<MatchSummary> getSummary() {
        return List.copyOf(matches);
    }
}
