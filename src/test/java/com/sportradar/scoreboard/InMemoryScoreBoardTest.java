package com.sportradar.scoreboard;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

    @Test
    void failsWhenStartingMatchWithTeamAlreadyPlayingAtHome() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        scoreBoard.startMatch("Mexico", "Canada", startedAt(0));

        assertThatThrownBy(() -> scoreBoard.startMatch("Mexico", "Brazil", startedAt(1)))
                .isInstanceOf(TeamAlreadyPlayingException.class);
    }

    @Test
    void failsWhenStartingMatchWithTeamAlreadyPlayingAway() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        scoreBoard.startMatch("Mexico", "Canada", startedAt(0));

        assertThatThrownBy(() -> scoreBoard.startMatch("Brazil", "Canada", startedAt(1)))
                .isInstanceOf(TeamAlreadyPlayingException.class);
    }

    @Test
    void startsOnlyOneMatchWhenConcurrentRequestsUseTheSameTeam() throws Exception {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        int requestCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(requestCount);
        CountDownLatch ready = new CountDownLatch(requestCount);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();

        for (int index = 0; index < requestCount; index++) {
            int matchNumber = index;
            futures.add(executor.submit(() -> {
                ready.countDown();
                start.await(1, TimeUnit.SECONDS);
                scoreBoard.startMatch("Argentina", "Opponent " + matchNumber, startedAt(matchNumber));
                return null;
            }));
        }

        ready.await(1, TimeUnit.SECONDS);
        start.countDown();

        int startedMatches = 0;
        int rejectedMatches = 0;
        for (Future<?> future : futures) {
            try {
                future.get();
                startedMatches++;
            } catch (Exception exception) {
                if (exception.getCause() instanceof TeamAlreadyPlayingException) {
                    rejectedMatches++;
                } else {
                    throw exception;
                }
            }
        }
        executor.shutdownNow();

        assertThat(startedMatches).isEqualTo(1);
        assertThat(rejectedMatches).isEqualTo(requestCount - 1);
        assertThat(scoreBoard.getSummary()).hasSize(1);
    }

    @Test
    void findsActiveMatchById() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        MatchId matchId = scoreBoard.startMatch("Mexico", "Canada", startedAt(0));

        assertThat(scoreBoard.findMatch(matchId))
                .contains(new MatchSummary(matchId, "Mexico", "Canada", 0, 0));
    }

    @Test
    void returnsEmptyWhenFindingUnknownMatch() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        MatchId unknownMatchId = new MatchId(UUID.randomUUID());

        assertThat(scoreBoard.findMatch(unknownMatchId)).isEmpty();
    }

    @Test
    void returnsEmptyWhenFindingFinishedMatch() {
        ScoreBoard scoreBoard = new InMemoryScoreBoard();
        MatchId matchId = scoreBoard.startMatch("Mexico", "Canada", startedAt(0));

        scoreBoard.finishMatch(matchId);

        assertThat(scoreBoard.findMatch(matchId)).isEmpty();
    }

    private static Instant startedAt(int minutesAfterStart) {
        return START_TIME.plusSeconds(minutesAfterStart * 60L);
    }
}
