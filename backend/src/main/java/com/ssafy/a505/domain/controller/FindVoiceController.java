package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.entity.VoiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-virus")
@CrossOrigin(origins = "http://localhost:3000")
public class FindVoiceController {

    @GetMapping("/virus")
    public ResponseEntity<?> bestVoiceList(){
        log.info("Best Voice List");

        List<Voice> result = makeVoiceDummy();

        return new ResponseEntity<List<Voice>>(result, HttpStatus.OK);
    }

    @GetMapping("/virus/{id}")
    public ResponseEntity<?> findVoiceById(@PathVariable("id") long id){
        List<Voice> lst = makeVoiceDummy();
        Voice result = null;
        for (Voice voice : lst) {
            if(voice.getVoiceId() == id){
                result = voice;
            }
        }
        if(result == null) return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Voice>(result, HttpStatus.OK);

    }

    public List<Voice> makeVoiceDummy(){

        List<Voice> result = new ArrayList<>();

        Member member1 = Member.builder()
                .memberId(1L)
                .userEmail("user1@example.com")
                .userName("User1")
                .userPassword("password1")
                .profileImgUrl("")
                .remainChangeCount(5)
                .totalSpreadCount(10)
                .totalUploadCount(15)
                .build();

        Member member2 = Member.builder()
                .memberId(2L)
                .userEmail("user2@example.com")
                .userName("User2")
                .userPassword("password2")
                .profileImgUrl("")
                .remainChangeCount(3)
                .totalSpreadCount(8)
                .totalUploadCount(12)
                .build();

        Voice voice1 = Voice.builder()
                .voiceId(1L)
                .member(member1)
                .listenCount(200L)
                .title("프로님들 10층 욕심 이유 찾았다")
                .imageUrl("https://png.pngtree.com/thumb_back/fw800/background/20231008/pngtree-d-render-of-a-golden-tooth-exploring-dental-and-medical-concepts-image_13574408.png")
                .originalName("voice1.mp3")
                .savePath("/voices/voice1.mp3")
                .saveFolder("/voices/")
                .latitude(37.5665)
                .longitude(126.9780)
                .dateTime(LocalDateTime.now())
                .voiceType(VoiceType.Processed)
                .build();

        Voice voice2 = Voice.builder()
                .voiceId(2L)
                .member(member2)
                .listenCount(300L)
                .title("재용이햄 애플 런칭행사 줄서있는거 봄")
                .imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXobLcA4-7fiJkSmqBaQnd2QL8gY8EFIx4pQ&s")
                .originalName("voice2.mp3")
                .savePath("/voices/voice2.mp3")
                .saveFolder("/voices/")
                .latitude(37.7749)
                .longitude(-122.4194)
                .dateTime(LocalDateTime.now())
                .voiceType(VoiceType.NormalVoice)
                .build();

        result.add(voice1);
        result.add(voice2);

        return result;
    }
}
