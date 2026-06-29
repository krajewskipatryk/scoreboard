package com.sportradar.scoreboard;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryScoreBoardTest {

    @Test
    void startsMatchWithInitialScoreAndIncludesItInSummary() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();

        MatchId matchId = scoreBoard.startMatch("Mexico", "Canada");

        assertThat(scoreBoard.getSummary())
                .containsExactly(new MatchSummary(matchId, "Mexico", "Canada", 0, 0));
    }
}
