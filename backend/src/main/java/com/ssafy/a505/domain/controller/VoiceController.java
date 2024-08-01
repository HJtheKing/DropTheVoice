package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.entity.VoiceType;
import com.ssafy.a505.domain.repository.VoiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-voice")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class VoiceController {

    private final VoiceRepository voiceRepository;

    @PostConstruct
    public void init() {
        for(int i=0;i<30;i++){
            Member member = new Member();
            member.setUserName("김병관");

            Voice voice = new Voice();
            voice.setMember(member);
            voice.setHeartCount(3L);
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
        List<Voice> result = voiceRepository.findAllByUserName("김병관", pageRequest);
//        List<Voice> result = voiceRepository.findAllByUserId(1L, pageRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/best-voice/{voice_id}")
    public ResponseEntity<?> getVoiceDetail(@PathVariable("voice_id") Long voiceId){
        Voice result = voiceRepository.findById(voiceId).get();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
