package com.sportradar.scoreboard;

/**
 * Thrown when an operation requires an active match but the provided identifier is unknown or no longer active.
 */
public class MatchNotFoundException extends RuntimeException {

    public MatchNotFoundException(MatchId matchId) {
        super("Match not found: " + matchId.value());
    }
}
