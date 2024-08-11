package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.dto.response.VoiceResponseDTO;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.repository.VoiceRepository;
import com.ssafy.a505.domain.service.VoiceService;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.metal.MetalMenuBarUI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api-voice")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class VoiceController {

    private final VoiceService voiceService;
    private final VoiceRepository voiceRepository;
    private final SseController sseController;

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
    public ResponseEntity<?> getVoiceDetail(@PathVariable("voice_id") Long voiceId, @AuthenticationPrincipal Member member) {
        Voice voice = voiceService.getVoiceById(voiceId); // voiceService에서 Voice 객체 가져오기
        VoiceResponseDTO result = VoiceResponseDTO.fromEntity(voice, member);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/all-voices")
    public ResponseEntity<?> getAllVoices() {
        return new ResponseEntity<>(voiceService.getAllVoice(), HttpStatus.OK);
    }

    @PostMapping("{voiceId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("voiceId") Long voiceId, @AuthenticationPrincipal Member member) {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        boolean isLiked = voiceService.toggleLike(voiceId, member);
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOICE));

        Map<String, Object> response = new HashMap<>();
        response.put("isLiked", isLiked);
        response.put("likeCount", voice.getHeartCount());  // 현재의 heartCount 반환
        // SSE 알림 전송
        String message = "새로운 좋아요";
        sseController.sendNotification(message);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyVoices(@RequestParam("latitude") double latitude,
                                             @RequestParam("longitude") double longitude,
                                             @RequestParam("radius") double radius) {
        List<VoiceResponseDTO> voices = voiceService.getNearbyVoices(latitude, longitude, radius);
        return ResponseEntity.ok(voices);
    }
}
