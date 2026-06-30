package com.sportradar.scoreboard;

/**
 * Thrown when starting a match would put a team into more than one active match.
 */
public class TeamAlreadyPlayingException extends RuntimeException {

    public TeamAlreadyPlayingException(String team) {
        super("Team already playing: " + team);
    }
}
