package com.feelody.feelody_backend.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRespDto {
    private Long id;
    private String title;
    private String artist;
    private int score;
    private LocalDate createdAt; // 연습일자
}
