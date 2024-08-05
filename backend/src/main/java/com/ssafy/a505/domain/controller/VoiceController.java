package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.service.VoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-voice")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class VoiceController {

    private final VoiceService voiceService;

    @GetMapping("/best-voice/{page}/{size}")
    public ResponseEntity<?> getBestVoicesV2(@PathVariable("page") int page, @PathVariable("size") int size) {
        log.info("getBestVoicesV2");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<Voice> result = voiceService.findALlByTitle("나는 두부를 좋아함", pageRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/best-voice/{voice_id}")
    public ResponseEntity<?> getVoiceDetail(@PathVariable("voice_id") Long voiceId){
        Voice result = voiceService.findById(voiceId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
