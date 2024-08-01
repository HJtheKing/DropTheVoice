package com.ssafy.a505.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.a505.domain.dto.request.VoiceCreateRequestDTO;
import com.ssafy.a505.domain.entity.ProcessedVoice;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-flask")
public class UploadController {

    private final UploadService uploadService;

    /**
     * Flask로 데이터 전송
     */
    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadVoice(@RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
                                         @RequestPart(value = "title") String title,
                                         @RequestParam("pitchShift") float pitchShift) throws JsonProcessingException {
        VoiceCreateRequestDTO voiceCreateRequestDTO = new VoiceCreateRequestDTO(title, audioFile);
        Voice voice = uploadService.uploadAndSendVoice(voiceCreateRequestDTO, pitchShift);
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
}