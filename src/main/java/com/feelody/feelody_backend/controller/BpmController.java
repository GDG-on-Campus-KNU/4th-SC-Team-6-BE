package com.feelody.feelody_backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bpm")
@AllArgsConstructor
public class BpmController {
    private final SimpMessagingTemplate messagingTemplate;
    private final String destination = "/api/bpm/wearable";

    @PostMapping()
    public String doMetronome(@RequestBody Integer bpm) {
        messagingTemplate.convertAndSend(destination, bpm);
        return "BPM data";
    }
}
