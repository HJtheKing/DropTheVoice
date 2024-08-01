package com.ssafy.a505.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.a505.domain.dto.request.VoiceCreateRequestDTO;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.service.S3FileService;
import com.ssafy.a505.domain.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api-spread")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class SpreadController {

    public Voice voice;
    public String uploadDir;
    private UploadService uploadService;
    private S3FileService s3FileService;

    @PostMapping("/spread")
    public ResponseEntity<?> uploadAudioFileAndSpread(@ModelAttribute VoiceCreateRequestDTO audioInput, @RequestParam float pitchShift) throws JsonProcessingException {
        Voice voice = uploadService.uploadAndSendVoice(audioInput, pitchShift);
        return ResponseEntity.ok("파일 업로드 성공" + voice.getSavePath());
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
    public int getRemainVoiceChangeOpportunity(@PathVariable("member_id") int memberId){
        return 3;
    }
}
