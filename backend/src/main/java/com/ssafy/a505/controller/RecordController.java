package com.ssafy.a505.controller;

import com.ssafy.a505.domain.voice.Voice;
import com.ssafy.a505.domain.voice.VoiceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api-record")
@CrossOrigin(origins = "http://localhost:3000")
public class RecordController {

//    @Autowired
//    RedisUtils redisUtils;

    @PostMapping("/record")
    public ResponseEntity<?> saveRecord(@ModelAttribute VoiceDto voiceDto) throws IOException {
        // Voice 객체 테이블 DB 추가 설정 필요
        Voice voice = new Voice();
        voice.setVoiceUrl(voiceDto.getVoiceUrl());
        voice.setType(voiceDto.getVoiceType());
        voice.setLatitude(voiceDto.getLatitude());
        voice.setLongitude(voiceDto.getLongitude());
        voice.setDateTime(LocalDateTime.now());
        log.info("voice ={}", voice);

        // 음성 제대로 전달되었나 Test
        MultipartFile voiceFile = voiceDto.getVoiceFile();
        File file = new File("C:/Users/SSAFY/Desktop/file.wav");
        file.getParentFile().mkdirs();
        voiceFile.transferTo(file);

        // S3에 음성 파일 저장

        // RDB 저장 로직

        // Redis 저장 로직
//        redisUtils.addLocation("TP_KEY", voiceDto.getLatitude(), voiceDto.getLongitude());
        return new ResponseEntity<Voice>(voice, HttpStatus.OK);
    }

}
