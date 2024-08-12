package com.ssafy.a505.global.sse;

import com.ssafy.a505.global.sse.EmitterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
public class NotificationService {

    private final EmitterRepository emitterRepository;

    public NotificationService(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
    }

    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitterRepository.save(memberId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(memberId));
        emitter.onTimeout(() -> emitterRepository.deleteById(memberId));

        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("Connected!"));
        } catch (IOException e) {
            emitterRepository.deleteById(memberId);
        }

        return emitter;
    }

    public void sendNotification(Long memberId, String message) {
        SseEmitter emitter = emitterRepository.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("MESSAGE")
                        .data(message));
            } catch (IOException e) {
                emitterRepository.deleteById(memberId);
            }
        }
    }
}
