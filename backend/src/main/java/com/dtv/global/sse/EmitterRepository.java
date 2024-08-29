package com.dtv.global.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(Long id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    public void deleteById(Long memberId) {
        emitters.remove(memberId);
    }

    public SseEmitter get(Long memberId) {
        return emitters.get(memberId);
    }
}
