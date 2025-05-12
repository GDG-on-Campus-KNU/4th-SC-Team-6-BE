package com.feelody.feelody_backend.controller;

import com.feelody.feelody_backend.dto.MusicUploadRequestDto;
import com.feelody.feelody_backend.service.MusicService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadMusic(@ModelAttribute MusicUploadRequestDto request) {
        Long id = musicService.uploadMusic(request);
        return ResponseEntity.created(URI.create("/api/music/" + id)).build();
    }
}
