package com.feelody.feelody_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class MusicUploadRequestDto {

    private String title;
    private MultipartFile musicFile;
    private MultipartFile videoFile;
}
