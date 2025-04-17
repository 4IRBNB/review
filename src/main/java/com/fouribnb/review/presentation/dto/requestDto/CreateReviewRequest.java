package com.fouribnb.review.presentation.dto.requestDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;


public record CreateReviewRequest(
    @NotNull(message = "유저 아이디는 필수 사항 입니다.")
    Long userId,

    @NotNull(message = "숙소 아이디는 필수 사항 입니다.")
    UUID lodgeId,

    @NotNull(message = "리뷰 내용을 입력해 주세요.")
    String content,

    @NotNull
    @Min(value = 1, message = "최소 별점은 1점 입니다.")
    @Max(value = 5, message = "최대 별점은 5점 입니다.")
    Long rating
) {

}
