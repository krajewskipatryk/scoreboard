package com.sportradar.scoreboard;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryScoreBoard implements ScoreBoard {

    private final Map<MatchId, Match> matches = new HashMap<>();

    @Override
    public synchronized MatchId startMatch(String homeTeam, String awayTeam, Instant startedAt) {
        if (isTeamPlaying(homeTeam)) {
            throw new TeamAlreadyPlayingException(homeTeam);
        }
        if (isTeamPlaying(awayTeam)) {
            throw new TeamAlreadyPlayingException(awayTeam);
        }
        MatchId matchId = new MatchId(UUID.randomUUID());
        matches.put(matchId, new Match(matchId, homeTeam, awayTeam, 0, 0, startedAt));
        return matchId;
    }

    @Override
    public synchronized void updateScore(MatchId matchId, int homeScore, int awayScore) {
        Match match = matches.get(matchId);
        if (match == null) {
            throw new MatchNotFoundException(matchId);
        }
        matches.put(matchId, match.withScore(homeScore, awayScore));
    }

    @Override
    public synchronized void finishMatch(MatchId matchId) {
        if (matches.remove(matchId) == null) {
            throw new MatchNotFoundException(matchId);
        }
    }

    @Override
    public synchronized List<MatchSummary> getSummary() {
        return matches.values().stream()
                .sorted(Comparator.comparingInt(Match::totalScore).reversed()
                        .thenComparing(Match::startedAt, Comparator.reverseOrder()))
                .map(Match::toSummary)
                .toList();
    }

    private boolean isTeamPlaying(String team) {
        return matches.values().stream()
                .anyMatch(match -> match.homeTeam().equals(team) || match.awayTeam().equals(team));
    }
}
