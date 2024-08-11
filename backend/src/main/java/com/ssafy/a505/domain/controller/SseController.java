package com.ssafy.a505.domain.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api-sse")
@CrossOrigin(origins = "http://localhost:3000")
public class SseController {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/sse/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            System.out.println("SSE completed and removed: " + emitter);
        });
        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            System.out.println("SSE timeout and removed: " + emitter);
        });
        emitter.onError(e -> {
            emitters.remove(emitter);
            System.out.println("SSE error and removed: " + emitter);
        });
        System.out.println("SSE subscription added: " + emitter);
        return emitter;
    }

    // 새로운 알림을 전송하는 메서드
    public void sendNotification(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                System.out.println("Sending SSE message: " + message);
                emitter.send(SseEmitter.event().name("newData").data(message));
            } catch (IOException e) {
                deadEmitters.add(emitter);
                System.out.println("Error sending SSE message: " + e.getMessage());
            }
        });

        emitters.removeAll(deadEmitters);
    }
}
