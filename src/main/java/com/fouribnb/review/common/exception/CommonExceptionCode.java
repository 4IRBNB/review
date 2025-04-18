package com.fouribnb.review.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonExceptionCode implements ExceptionCode {

    REVIEW_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "리뷰가 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
