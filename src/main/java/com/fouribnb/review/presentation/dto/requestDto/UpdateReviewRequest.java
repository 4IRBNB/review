package com.fouribnb.review.presentation.dto.requestDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateReviewRequest(
    @NotNull(message = "리뷰 내용을 입력해 주세요.")
    String content,

    @NotNull
    @Min(value = 1, message = "최소 별점은 1점 입니다.")
    @Max(value = 5, message = "최대 별점은 5점 입니다.")
    Long rating
) {

}
