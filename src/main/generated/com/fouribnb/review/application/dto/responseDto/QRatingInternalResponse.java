package com.fouribnb.review.application.dto.responseDto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.fouribnb.review.application.dto.responseDto.QRatingInternalResponse is a Querydsl Projection type for RatingInternalResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QRatingInternalResponse extends ConstructorExpression<RatingInternalResponse> {

    private static final long serialVersionUID = -1236128960L;

    public QRatingInternalResponse(com.querydsl.core.types.Expression<Long> rating, com.querydsl.core.types.Expression<Long> count) {
        super(RatingInternalResponse.class, new Class<?>[]{long.class, long.class}, rating, count);
    }

}

