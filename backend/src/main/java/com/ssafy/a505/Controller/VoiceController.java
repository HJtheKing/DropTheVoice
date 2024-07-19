package com.ssafy.a505.Controller;

import com.ssafy.a505.Domain.Voice.InputVoiceDTO;
import com.ssafy.a505.Domain.Voice.Voice;
import com.ssafy.a505.Domain.Voice.VoiceType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api-voice")
@CrossOrigin(origins = "http://localhost:3000")
public class VoiceController {
    public Voice voice;
    public List<Voice> voices;

    public VoiceController(){
        voice = new Voice();
        voice.setId(1);
        voice.setUserId(3);
        voice.setLike(3);
        voice.setUserName("김병관");
        voice.setListenCount(Math.round(Math.random()*100000));
        voice.setLatitude(50);
        voice.setLongitude(50);
        voice.setTitle("나는 두부를 좋아함");
        voice.setType(VoiceType.EggVoice);
        voice.setDateTime(LocalDateTime.now());
        voice.setVoiceUrl("https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/LocalDateTime_%EC%82%AC%EC%9A%A9%EB%B2%95_%EC%A0%95%EB%A6%AC/img/cover.png?raw=true");
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
