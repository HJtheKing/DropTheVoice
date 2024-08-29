package com.dtv.domain.controller;

import com.dtv.domain.dto.request.VoiceDto;
import com.dtv.domain.entity.Voice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api-record")
@CrossOrigin(origins = "http://localhost:3000")
public class RecordController {


    @PostMapping("/record")
    public ResponseEntity<?> saveRecord(@ModelAttribute VoiceDto voiceDto) throws IOException {
        // Voice 객체 테이블 DB 추가 설정 필요
        Voice voice = new Voice();
        voice.setSavePath(voiceDto.getVoiceUrl());
        voice.setVoiceType(voiceDto.getVoiceType());
        voice.setLatitude(voiceDto.getLatitude());
        voice.setLongitude(voiceDto.getLongitude());
        voice.setDateTime(LocalDateTime.now());
        log.info("voice ={}", voice);

        return new ResponseEntity<Voice>(voice, HttpStatus.OK);
    }

}
