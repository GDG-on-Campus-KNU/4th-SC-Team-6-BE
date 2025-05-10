package com.feelody.feelody_backend.dto;

import java.time.LocalDate;

public record FeedbackRespDto(
        Long id,
        String title,
        String artist,
        int score,
        LocalDate createdAt // 연습일자
) {
}
