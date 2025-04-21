package com.fouribnb.review.infrastructure.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
    UUID id,
    Long userId,
    UUID lodeId,
    Long price,
    LocalDateTime checkInDate,
    LocalDateTime checkOutDate,
    String reservationStatus
) {

}
