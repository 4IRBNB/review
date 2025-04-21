package com.fouribnb.review.infrastructure.client;

import com.fouribnb.review.infrastructure.client.dto.ReservationResponse;
import com.fourirbnb.common.response.BaseResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reservation-service", url = "http://localhost:19095")
public interface ReservationClient {

    @GetMapping("/internal/reservations/me")
    BaseResponse<List<ReservationResponse>> getReservationById(@RequestParam Long userId);
}
