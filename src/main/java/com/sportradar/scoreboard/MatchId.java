package com.sportradar.scoreboard;

import java.util.UUID;

/**
 * Opaque identifier used to refer to a match in scoreboard operations.
 */
public record MatchId(UUID value) {
}
