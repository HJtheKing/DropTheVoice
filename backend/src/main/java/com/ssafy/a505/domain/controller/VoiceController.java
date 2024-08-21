package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.dto.response.RedisResponseDTO;
import com.ssafy.a505.domain.dto.response.VoiceResponseDTO;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.VoiceRepository;
import com.ssafy.a505.domain.service.RedisService;
import com.ssafy.a505.domain.service.VoiceService;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import com.ssafy.a505.global.sse.NotificationController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api-voice")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class VoiceController {

    private final SimpMessagingTemplate messagingTemplate;
    private final VoiceService voiceService;
    private final VoiceRepository voiceRepository;
    private final RedisService redisService;

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
        List<Voice> result = voiceService.getVoiceOrderByHeartCountDesc(page, size);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/best-voice/{voice_id}")
    public ResponseEntity<?> getVoiceDetail(@PathVariable("voice_id") Long voiceId, @AuthenticationPrincipal Member member) {
        Voice voice = voiceService.getVoiceById(voiceId);
        VoiceResponseDTO result = VoiceResponseDTO.fromEntity(voice, member);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all-voices")
    public ResponseEntity<?> getAllVoices() {
        return new ResponseEntity<>(voiceService.getAllVoice(), HttpStatus.OK);
    }


    @PostMapping("{voiceId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("voiceId") Long voiceId, @RequestParam(value = "latitude", required = false) Double latitude, @RequestParam(value = "longitude", required = false) Double longitude, @AuthenticationPrincipal Member member) {
        log.info("latitude: {} longitude: {}", latitude, longitude);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        /**
         * Like 전파 로직 작성
         */
        Set<String> wsMemberIds = redisService.getWsMemberIds();
        Set<RedisResponseDTO> findMembers = redisService.getMembersByRadiusV2(longitude, latitude, 10d, voiceId, 5, wsMemberIds);
        for (RedisResponseDTO findMember : findMembers) {
            
        }

        boolean isLiked = voiceService.toggleLike(voiceId, member);
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOICE));

        Map<String, Object> response = new HashMap<>();
        response.put("isLiked", isLiked);
        response.put("likeCount", voice.getHeartCount());  // 현재의 heartCount 반환
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{voiceId}/pick")
    public ResponseEntity<?> togglePick(@PathVariable("voiceId") Long voiceId, @AuthenticationPrincipal Member member) {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
        boolean isPicked = voiceService.togglePick(voiceId, member);
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VOICE));

        Map<String, Object> response = new HashMap<>();
        response.put("isPicked", isPicked);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyVoices(@RequestParam("latitude") double latitude,
                                             @RequestParam("longitude") double longitude,
                                             @RequestParam("radius") double radius,
                                             @AuthenticationPrincipal Member member) {
        List<VoiceResponseDTO> voices = voiceService.getNearbyVoices(latitude, longitude, radius, member);
        return ResponseEntity.ok(voices);
    }
}
