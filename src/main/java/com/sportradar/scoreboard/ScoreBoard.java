package com.sportradar.scoreboard;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Manages live football matches currently present on a scoreboard.
 */
public interface ScoreBoard {

    /**
     * Starts a new active match with an initial score of 0-0.
     *
     * @param homeTeam the home team name
     * @param awayTeam the away team name
     * @param startedAt the domain start time used for summary ordering
     * @return the identifier assigned to the started match
     * @throws TeamAlreadyPlayingException when either team is already in an active match
     */
    MatchId startMatch(String homeTeam, String awayTeam, Instant startedAt);

    /**
     * Replaces the current score of an active match with the provided absolute score.
     *
     * @param matchId the active match identifier
     * @param homeScore the new home team score
     * @param awayScore the new away team score
     * @throws MatchNotFoundException when the identifier does not refer to an active match
     */
    void updateScore(MatchId matchId, int homeScore, int awayScore);

    /**
     * Finishes an active match and removes it permanently from the active scoreboard.
     *
     * @param matchId the active match identifier
     * @throws MatchNotFoundException when the identifier does not refer to an active match
     */
    void finishMatch(MatchId matchId);

    /**
     * Finds the current public state of a single active match.
     *
     * @param matchId the match identifier
     * @return the match summary, or an empty value when the match is unknown or already finished
     */
    Optional<MatchSummary> findMatch(MatchId matchId);

    /**
     * Returns active matches ordered by total score descending, then by most recently started match.
     *
     * @return immutable match summary values for active matches
     */
    List<MatchSummary> getSummary();
}
