package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Voice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-storage")
@CrossOrigin(origins = "http://localhost:3000")
public class StorageController {

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        log.info("findAll");

        List<Voice> result = makeDummy("spread");

        return new ResponseEntity<List<Voice>>(result, HttpStatus.OK);
    }

    @GetMapping("/like")
    public ResponseEntity<?> findLike(){
        log.info("findLike");

        List<Voice> result = makeDummy("like");

        return new ResponseEntity<List<Voice>>(result, HttpStatus.OK);
    }

    public List<Voice> makeDummy(String str){
        List<Voice> result = new ArrayList<>();
        for(int i = 1; i <= 20; i++){
            Voice voice = new Voice();
            voice.setTitle(str + i);
            result.add(voice);
        }
        return result;
    }

}
