package com.feelody.feelody_backend.controller;

import com.feelody.feelody_backend.dto.FeedbackRespDto;
import com.feelody.feelody_backend.service.FeedbackService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/feedback")
@AllArgsConstructor
public class FeedbackController {
    private FeedbackService feedbackService;

    @PostMapping
    public FeedbackRespDto uploadAudio(@RequestParam("id") Long id,
                                       @RequestBody MultipartFile audio) throws IOException {
        return feedbackService.geminiAnalyze(audio);
    }
}
