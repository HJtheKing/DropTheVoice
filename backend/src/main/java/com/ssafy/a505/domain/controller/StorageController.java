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
        Voice voice1 = new Voice();
        voice1.setTitle("tst1");
        Voice voice2 = new Voice();
        voice2.setTitle("tst2");
        Voice voice3 = new Voice();
        voice3.setTitle("tst3");
        Voice voice4 = new Voice();
        voice4.setTitle("tst4");
        Voice voice5 = new Voice();
        voice5.setTitle("tst5");
        Voice voice6 = new Voice();
        voice6.setTitle("tst6");

        List<Voice> result = new ArrayList<>();
        result.add(voice1);
        result.add(voice2);
        result.add(voice3);
        result.add(voice4);
        result.add(voice5);
        result.add(voice6);

        return new ResponseEntity<List<Voice>>(result, HttpStatus.OK);
    }

    @GetMapping("/like")
    public ResponseEntity<?> findLike(){
        log.info("findLike");

        Voice voice1 = new Voice();
        voice1.setTitle("like1");
        Voice voice2 = new Voice();
        voice2.setTitle("like2");
        Voice voice3 = new Voice();
        voice3.setTitle("like3");
        Voice voice4 = new Voice();
        voice4.setTitle("like4");
        Voice voice5 = new Voice();
        voice5.setTitle("like5");
        Voice voice6 = new Voice();
        voice6.setTitle("like6");

        List<Voice> result = new ArrayList<>();
        result.add(voice1);
        result.add(voice2);
        result.add(voice3);
        result.add(voice4);
        result.add(voice5);
        result.add(voice6);

        return new ResponseEntity<List<Voice>>(result, HttpStatus.OK);
    }
}
