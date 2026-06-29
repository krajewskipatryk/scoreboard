package com.sportradar.scoreboard;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryScoreBoardTest {

    private static final Instant START_TIME = Instant.parse("2026-06-29T10:00:00Z");

    @Test
    void startsMatchWithInitialScoreAndIncludesItInSummary() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();

        MatchId matchId = scoreBoard.startMatch("Mexico", "Canada", startedAt(0));

        assertThat(scoreBoard.getSummary())
                .containsExactly(new MatchSummary(matchId, "Mexico", "Canada", 0, 0));
    }

    @Test
    void updatesExistingMatchScore() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        MatchId matchId = scoreBoard.startMatch("Mexico", "Canada", startedAt(0));

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
        MatchId matchId = scoreBoard.startMatch("Mexico", "Canada", startedAt(0));

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

    @Test
    void returnsSummaryOrderedByTotalScoreThenMostRecentlyStartedMatch() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();

        MatchId mexicoCanada = scoreBoard.startMatch("Mexico", "Canada", startedAt(0));
        MatchId spainBrazil = scoreBoard.startMatch("Spain", "Brazil", startedAt(1));
        MatchId germanyFrance = scoreBoard.startMatch("Germany", "France", startedAt(2));
        MatchId uruguayItaly = scoreBoard.startMatch("Uruguay", "Italy", startedAt(3));
        MatchId argentinaAustralia = scoreBoard.startMatch("Argentina", "Australia", startedAt(4));

        scoreBoard.updateScore(mexicoCanada, 0, 5);
        scoreBoard.updateScore(spainBrazil, 10, 2);
        scoreBoard.updateScore(germanyFrance, 2, 2);
        scoreBoard.updateScore(uruguayItaly, 6, 6);
        scoreBoard.updateScore(argentinaAustralia, 3, 1);

        assertThat(scoreBoard.getSummary()).containsExactly(
                new MatchSummary(uruguayItaly, "Uruguay", "Italy", 6, 6),
                new MatchSummary(spainBrazil, "Spain", "Brazil", 10, 2),
                new MatchSummary(mexicoCanada, "Mexico", "Canada", 0, 5),
                new MatchSummary(argentinaAustralia, "Argentina", "Australia", 3, 1),
                new MatchSummary(germanyFrance, "Germany", "France", 2, 2)
        );
    }

    private static Instant startedAt(int minutesAfterStart) {
        return START_TIME.plusSeconds(minutesAfterStart * 60L);
    }
}
