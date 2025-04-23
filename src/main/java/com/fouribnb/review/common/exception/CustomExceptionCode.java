package com.fouribnb.review.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionCode implements ExceptionCode {

    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN,"권한이 없는 유저 입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND,"사용 완료된 예약이 존재하지 않습니다."),
    REVIEW_EXIST(HttpStatus.CONFLICT,"이미 작성된 리뷰가 존재합니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
