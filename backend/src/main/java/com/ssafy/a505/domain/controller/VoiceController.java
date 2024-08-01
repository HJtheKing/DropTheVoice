package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.entity.VoiceType;
import com.ssafy.a505.repository.VoiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api-voice")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VoiceController {

    private final VoiceRepository voiceRepository;

    @PostConstruct
    public void init() {
        for(int i=0;i<30;i++){
            Voice voice = new Voice();
            voice.setUserId(1);
            voice.setHeart(3);
            voice.setUserName("김병관");
            voice.setListenCount(Math.round(Math.random()*100000));
            voice.setLatitude(50);
            voice.setLongitude(50);
            voice.setTitle("나는 두부를 좋아함");
            voice.setVoiceType(VoiceType.NormalVoice);
            voice.setDateTime(LocalDateTime.now());
            voice.setSavePath("https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/LocalDateTime_%EC%82%AC%EC%9A%A9%EB%B2%95_%EC%A0%95%EB%A6%AC/img/cover.png?raw=true");
            voice.setImageUrl("https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/LocalDateTime_%EC%82%AC%EC%9A%A9%EB%B2%95_%EC%A0%95%EB%A6%AC/img/cover.png?raw=true");

            voiceRepository.save(voice);
        }
    }

    @GetMapping("/best-voice")
    public ResponseEntity<?> getBestVoices() {
        log.info("getBestVoices");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Voice> result = voiceRepository.findAllByUserId(1L, pageRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/best-voice/{page}/{size}")
    public ResponseEntity<?> getBestVoicesV2(@PathVariable("page") int page, @PathVariable("size") int size) {
        log.info("getBestVoicesV2");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<Voice> result = voiceRepository.findAllByUserId(1L, pageRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/best-voice/{voice_id}")
    public ResponseEntity<?> getVoiceDetail(@PathVariable("voice_id") Long voiceId){
        Voice result = voiceRepository.findById(voiceId).get();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
