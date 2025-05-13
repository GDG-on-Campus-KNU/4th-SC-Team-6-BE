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
    private final String destination = "/api/bpm/wearable/notification";

    @PostMapping("/start")
    public String doMetronome(@RequestBody Integer bpm) {
        //신호를 받아서 BPM를 워치에 전달한다.

        messagingTemplate.convertAndSend(destination, bpm);

        return "BPM data";
    }

    @PostMapping("/stop")
    public void sendNotification() {
        messagingTemplate.convertAndSend(destination, 0);
    }
}
