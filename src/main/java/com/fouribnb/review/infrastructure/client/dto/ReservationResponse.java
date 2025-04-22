package com.fouribnb.review.infrastructure.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
    UUID id,
    Long userId,
    UUID lodgeId,
    Long price,
    LocalDateTime checkInDate,
    LocalDateTime checkOutDate,
    String reservationStatus
) {

}
