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
    public ResponseEntity<?> findAllwithPage(@PathVariable("page") int page, @PathVariable("size") int size){
        log.info("findAllwithPage");
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<Voice> spreadList = voiceRepository.findByTitleContaining("spread", pageRequest);


//        if(spreadList.size() < (page - 1) * size){
//            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
//        }
//        int cnt = page * size > spreadList.size() ? spreadList.size() - (page - 1) * size : size;
//
//        List<Voice> result = spreadList.subList((page - 1) * size, (page - 1) * size + cnt);

        return new ResponseEntity<>(spreadList, HttpStatus.OK);
    }


    @GetMapping("/like")
    public ResponseEntity<?> findLike(){
        log.info("findLike");
        List<Voice> likeList = voiceRepository.findByTitleContaining("like");
        return new ResponseEntity<>(likeList, HttpStatus.OK);
    }

//    public List<Voice> makeDummy(String str){
//        List<Voice> result = new ArrayList<>();
//        for(int i = 1; i <= 100; i++){
//            Voice voice = new Voice();
//            voice.setTitle(str + i);
//            result.add(voice);
//        }
//        return result;
//    }

}
