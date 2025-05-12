package com.feelody.feelody_backend.dto;

import org.springframework.web.multipart.MultipartFile;

public class MusicUploadRequestDto {

    private String title;
    private MultipartFile musicFile;
    private MultipartFile videoFile;

}
