package com.ssafy.a505.controller;

import com.ssafy.a505.domain.voice.InputVoiceDTO;
import com.ssafy.a505.domain.voice.Voice;
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
@CrossOrigin(origins = "*")
public class SpreadController {

    public Voice voice;
    public List<Voice> voices;
    public String uploadDir;

    @PostMapping("/spread")
    public String uploadAudioFileAndSpread(
            @ModelAttribute InputVoiceDTO audioInput) {
        MultipartFile file = audioInput.getAudioFile();

        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            return "파일이 비어 있습니다.";
        }

        // 파일명 클린업
        String fileName = UUID.randomUUID()+StringUtils.cleanPath(file.getOriginalFilename());
        uploadDir = "./upload/";
        try {
            // 파일 저장 경로 설정
            Path path = Paths.get(uploadDir + fileName);

            // 디렉토리가 존재하지 않으면 생성
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 파일 저장
            Files.copy(file.getInputStream(), path);

            // 파일 접근 경로 반환
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/audio/download/")
                    .path(fileName)
                    .toUriString();

            return "파일 업로드 성공: " + fileDownloadUri + " Title: " + audioInput.getTitle();

        } catch (IOException ex) {
            ex.printStackTrace();
            return "파일 업로드 중 오류가 발생했습니다.";
        }
    }

    @PostMapping("/change")
    public String changeAudioFileAndSpread(
            @ModelAttribute InputVoiceDTO audioInput) {
        InputVoiceDTO newVoiceInput = changeVoice(audioInput);
        return uploadAudioFileAndSpread(newVoiceInput);
    }

    private InputVoiceDTO changeVoice(InputVoiceDTO audioInput){
        return audioInput;
    }

    @GetMapping("/remain-voice-change/{user_id}")
    public int getRemainVoiceChangeOpportunity(@PathVariable("user_id") int userId){
        return 3;
    }
}
