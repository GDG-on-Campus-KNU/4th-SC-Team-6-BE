package com.feelody.feelody_backend.service;

import com.feelody.feelody_backend.dto.MusicUploadRequestDto;
import com.feelody.feelody_backend.entity.Music;
import com.feelody.feelody_backend.global.common.GcsFileManager;
import com.feelody.feelody_backend.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final GcsFileManager gcsFileManager;

    @Transactional
    public Long uploadMusic(MusicUploadRequestDto request) {
        MultipartFile musicFile = request.getMusicFile();
        String musicUrl = gcsFileManager.uploadFile(musicFile);
        String videoUrl = "";
        MultipartFile videoFile = request.getVideoFile();
        if (videoFile != null && !videoFile.isEmpty()) {
            videoUrl = gcsFileManager.uploadFile(videoFile);
        }

        Music music = new Music(request.getTitle(), musicUrl, videoUrl, null);
        Music savedMusic = musicRepository.save(music);
        return savedMusic.getId();
    }
}
