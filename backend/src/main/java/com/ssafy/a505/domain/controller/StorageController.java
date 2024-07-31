package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.service.VoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-storage")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class StorageController {

    private final VoiceService voiceService;

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        List<Voice> result = voiceService.findAllVoice();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/like")
    public ResponseEntity<?> getLikedVoices(@RequestParam("memberId") Long memberId) {
        List<Voice> heartedVoices = voiceService.findHeartedByMember(memberId);
        if (heartedVoices.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(heartedVoices, HttpStatus.OK);
    }
}
