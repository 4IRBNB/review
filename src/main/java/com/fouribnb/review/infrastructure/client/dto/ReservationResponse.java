package com.fouribnb.review.infrastructure.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
    UUID id,
    Long userId,
    UUID lodeId, // lodgeId 로 변경
    Long price,
    LocalDateTime checkInDate,
    LocalDateTime checkOutDate,
    String reservationStatus
) {

}
