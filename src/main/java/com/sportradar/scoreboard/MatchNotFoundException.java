package com.sportradar.scoreboard;

public class MatchNotFoundException extends RuntimeException {

    public MatchNotFoundException(MatchId matchId) {
        super("Match not found: " + matchId.value());
    }
}
