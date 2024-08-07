package com.ssafy.a505.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.a505.domain.dto.request.VoiceCreateRequestDTO;
import com.ssafy.a505.domain.entity.ProcessedVoice;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.service.MemberService;
import com.ssafy.a505.domain.service.VoiceUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api-upload")
@RequiredArgsConstructor
public class UploadController {

    private final VoiceUploadService voiceUploadService;
    private final MemberService memberService;

    /**
     * Flask로 데이터 전송
     */
    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadVoice(@RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
                                         @RequestPart(value = "title") String title, @RequestParam(value = "latitude", required = false) Double latitude, @RequestParam(value = "longitude", required = false) Double longitude, @RequestParam(value = "voiceType") String voiceType,
                                         @RequestParam("pitchShift") float pitchShift) throws JsonProcessingException {
        VoiceCreateRequestDTO voiceCreateRequestDTO = new VoiceCreateRequestDTO(title, audioFile);
        Voice voice = voiceUploadService.uploadAndSendVoice(voiceCreateRequestDTO, pitchShift);
        log.info("latitude: {}, longitude: {}, voiceType: {}", latitude, longitude, voiceType);
        return new ResponseEntity<>(voice, HttpStatus.CREATED);
    }

    /**
     * Flask에서 처리된 데이터를 수신
     */
    @PostMapping("/receive_processed_audio")
    public String receiveProcessedAudio(@RequestBody ProcessedVoice dto) {
        // 처리된 데이터 출력
        System.out.println("Where is processed Audio? : " + dto.getProcessedPath());

        return "";
    }

    @PostMapping("/change")
    public String changeAudioFileAndSpread(
            @ModelAttribute VoiceCreateRequestDTO audioInput) {
        VoiceCreateRequestDTO newVoiceInput = changeVoice(audioInput);
        return "" + newVoiceInput;
    }

    private VoiceCreateRequestDTO changeVoice(VoiceCreateRequestDTO audioInput){
        return audioInput;
    }

    @GetMapping("/remain-voice-change/{member_id}")
    public ResponseEntity<?> getRemainVoiceChangeOpportunity(@PathVariable("member_id") Long memberId) {
        int remainOpportunity = memberService.findRemainChangeCount(memberId);
        return ResponseEntity.ok(remainOpportunity);
    }
}
