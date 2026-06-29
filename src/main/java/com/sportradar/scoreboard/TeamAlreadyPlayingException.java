package com.sportradar.scoreboard;

public class TeamAlreadyPlayingException extends RuntimeException {

    public TeamAlreadyPlayingException(String team) {
        super("Team already playing: " + team);
    }
}
