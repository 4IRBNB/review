package com.fouribnb.review.presentation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("POST /api/reviews - Controller 연결 테스트")
  void createReview() throws Exception {
    mockMvc.perform(post("/api/reviews"))
        .andExpect(status().isOk());
  }
}