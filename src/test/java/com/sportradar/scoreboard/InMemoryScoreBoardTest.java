package com.sportradar.scoreboard;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryScoreBoardTest {

    @Test
    void startsMatchWithInitialScoreAndIncludesItInSummary() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();

        MatchId matchId = scoreBoard.startMatch("Mexico", "Canada");

        assertThat(scoreBoard.getSummary())
                .containsExactly(new MatchSummary(matchId, "Mexico", "Canada", 0, 0));
    }

    @Test
    void updatesExistingMatchScore() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        MatchId matchId = scoreBoard.startMatch("Mexico", "Canada");

        scoreBoard.updateScore(matchId, 2, 1);

        assertThat(scoreBoard.getSummary())
                .containsExactly(new MatchSummary(matchId, "Mexico", "Canada", 2, 1));
    }

    @Test
    void failsWhenUpdatingUnknownMatchScore() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        MatchId unknownMatchId = new MatchId(UUID.randomUUID());

        assertThatThrownBy(() -> scoreBoard.updateScore(unknownMatchId, 2, 1))
                .isInstanceOf(MatchNotFoundException.class);
    }

    @Test
    void finishesExistingMatchAndRemovesItFromSummary() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        MatchId matchId = scoreBoard.startMatch("Mexico", "Canada");

        scoreBoard.finishMatch(matchId);

        assertThat(scoreBoard.getSummary()).isEmpty();
    }

    @Test
    void failsWhenFinishingUnknownMatch() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        MatchId unknownMatchId = new MatchId(UUID.randomUUID());

        assertThatThrownBy(() -> scoreBoard.finishMatch(unknownMatchId))
                .isInstanceOf(MatchNotFoundException.class);
    }
}
