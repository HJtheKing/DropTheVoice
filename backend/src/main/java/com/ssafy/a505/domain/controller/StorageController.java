package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.repository.VoiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-storage")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class StorageController {

    private final VoiceRepository voiceRepository;

    @PostConstruct
    public void init() {
        for(int i = 1; i <= 43; i++){
            Voice voice = new Voice();
            voice.setTitle("spread" + i);
            voiceRepository.save(voice);
        }
        for(int i = 1; i <= 43; i++){
            Voice voice = new Voice();
            voice.setTitle("like" + i);
            voiceRepository.save(voice);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        log.info("findAll");
        List<Voice> spreadList = voiceRepository.findByTitleContaining("spread");
        return new ResponseEntity<>(spreadList, HttpStatus.OK);
    }
    @GetMapping("all/{page}/{size}")
    public ResponseEntity<?> findAllWithPage(@PathVariable("page") int page, @PathVariable("size") int size){

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        log.info("findAllWithPage page={}", page - 1);
        List<Voice> spreadList = voiceRepository.findByTitleContaining("spread", pageRequest);

        return new ResponseEntity<>(spreadList, HttpStatus.OK);
    }


    @GetMapping("/like")
    public ResponseEntity<?> findLike(){
        log.info("findLike");
        List<Voice> likeList = voiceRepository.findByTitleContaining("like");
        return new ResponseEntity<>(likeList, HttpStatus.OK);
    }
    @GetMapping("like/{page}/{size}")
    public ResponseEntity<?> findLikeWithPage(@PathVariable("page") int page, @PathVariable("size") int size){
        log.info("findLikeWithPage");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<Voice> spreadList = voiceRepository.findByTitleContaining("like", pageRequest);

        return new ResponseEntity<>(spreadList, HttpStatus.OK);
    }
}
