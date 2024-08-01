package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.entity.VoiceType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api-voice")
@CrossOrigin(origins = "http://localhost:3000")
public class VoiceController {
    public Voice voice;
    public List<Voice> voices;

    public VoiceController(){
        Member member = Member.builder()
                .memberId(1L)
                .userEmail("user1@example.com")
                .userName("김병관")
                .userPassword("")
                .profileImgUrl("")
                .remainChangeCount(5)
                .totalSpreadCount(10)
                .totalUploadCount(15)
                .build();

        voice = new Voice();
        voice.setVoiceId(1);
        voice.setMember(member);
        voice.setMember(member);
        voice.setListenCount(Math.round(Math.random()*100000));
        voice.setLatitude(50);
        voice.setLongitude(50);
        voice.setTitle("나는 두부를 좋아함");
        voice.setVoiceType(VoiceType.NormalVoice);
        voice.setDateTime(LocalDateTime.now());
        voice.setSavePath("https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/LocalDateTime_%EC%82%AC%EC%9A%A9%EB%B2%95_%EC%A0%95%EB%A6%AC/img/cover.png?raw=true");
        voice.setImageUrl("https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/LocalDateTime_%EC%82%AC%EC%9A%A9%EB%B2%95_%EC%A0%95%EB%A6%AC/img/cover.png?raw=true");

        voices = new LinkedList<>();
        for(int i=0;i<10;i++){
            voices.add(voice);
        }
    }

    @GetMapping("/best-voice")
    public List<Voice> getBestVoices() {
        return voices;
    }

    @GetMapping("/best-voice/{voice_id}")
    public Voice getVoiceDetail(@PathVariable("voice_id") int voiceId){
        return voice;
    }
}
