package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.dto.response.VoiceResponseDTO;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.service.VoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        List<Voice> result = voiceService.findAllByTitle("나는 두부를 좋아함", pageRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/{page}/{size}")
    public ResponseEntity<?> searchVoices(@RequestParam String keyword,
                                          @RequestParam String sort,
                                          @PathVariable int page,
                                          @PathVariable int size) {
        Page<VoiceResponseDTO> responseDTOS = voiceService.searchVoices(keyword, page - 1, size, sort);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @GetMapping("/best-heart-voice/{page}/{size}")
    public ResponseEntity<?> getHeartsVoices(@PathVariable("page") int page, @PathVariable("size") int size) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Voice> result = voiceService.getVoiceOrderByHeartCountDesc(page, size);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/best-voice/{voice_id}")
    public ResponseEntity<?> getVoiceDetail(@PathVariable("voice_id") Long voiceId) {
        Voice result = voiceService.findById(voiceId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all-voices")
    public ResponseEntity<?> getAllVoices() {
        return new ResponseEntity<>(voiceService.getAllVoice(), HttpStatus.OK);
    }
}
