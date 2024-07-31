package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Voice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        Voice voice1 = new Voice();
        voice1.setVoiceId(1L);
        voice1.setUserId(1L);
        voice1.setTitle("프로님들 10층 욕심 이유 찾았다");
        voice1.setImageUrl("https://png.pngtree.com/thumb_back/fw800/background/20231008/pngtree-d-render-of-a-golden-tooth-exploring-dental-and-medical-concepts-image_13574408.png");

        Voice voice2 = new Voice();
        voice2.setVoiceId(2L);
        voice2.setUserId(2L);
        voice2.setTitle("재용이햄 애플 런칭행사 줄서있는거 봄");
        voice2.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXobLcA4-7fiJkSmqBaQnd2QL8gY8EFIx4pQ&s");

        result.add(voice1);
        result.add(voice2);

        return result;
    }
}
