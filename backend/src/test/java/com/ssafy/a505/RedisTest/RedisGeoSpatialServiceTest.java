package com.ssafy.a505.RedisTest;

import com.ssafy.a505.domain.dto.response.RedisResponseDTO;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.service.RedisService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.util.List;

import static com.ssafy.a505.domain.service.RedisService.*;

@SpringBootTest
public class RedisGeoSpatialServiceTest {

    @Autowired
    private RedisService redisService;

    public double TestLatitude = 50;
    public double TestLongitude = 50;

    @Test
    public void addTestV3(){
        redisService.addLocation(MEMBER_KEY, MEMBER_TIME_KEY, 1L,  TestLongitude + 0.0005, TestLatitude + 0.0005,1);
        redisService.addLocation(MEMBER_KEY, MEMBER_TIME_KEY, 2L,  TestLongitude + 0.00051,TestLatitude + 0.00051, 1);
        redisService.addLocation(MEMBER_KEY, MEMBER_TIME_KEY, 3L,  TestLongitude + 0.00052,TestLatitude + 0.00052, 1);
        redisService.addLocation(MEMBER_KEY, MEMBER_TIME_KEY, 4L,  TestLongitude + 0.00053,TestLatitude + 0.00053, 1);
        redisService.addLocation(MEMBER_KEY, MEMBER_TIME_KEY, 5L,  TestLongitude + 0.00054,TestLatitude + 0.00054, 1);
        redisService.addLocation(MEMBER_KEY, MEMBER_TIME_KEY, 6L,  TestLongitude + 0.00055,TestLatitude + 0.00055, 1);
        redisService.addLocation(MEMBER_KEY, MEMBER_TIME_KEY, 7L,  TestLongitude + 0.00056,TestLatitude + 0.00056, 1);
        redisService.addLocation(MEMBER_KEY, MEMBER_TIME_KEY, 8L,  TestLongitude + 0.00057,TestLatitude + 0.00057, 1);
        redisService.addLocation(MEMBER_KEY, MEMBER_TIME_KEY, 9L,  TestLongitude + 0.00058,TestLatitude + 0.00058, 1);
    }
    @Test
    public void addVoice(){
        redisService.addLocation(VOICE_KEY, VOICE_TIME_KEY, 1L, TestLongitude, TestLatitude, 24);
        redisService.addLocation(VOICE_KEY, VOICE_TIME_KEY, 2L, TestLongitude, TestLatitude, 24);
    }


    @Test
    public void pokeAndVirusTest(){
        Voice voice = new Voice();
        voice.setVoiceId(5L);
        redisService.extracted(TestLatitude, TestLongitude, "virus", voice);
    }

    @Test
    public void markMsgReceivedTestV2(){
        Long voiceId = 1L;
        List<RedisResponseDTO> dtos = redisService.getMembersByRadius(TestLatitude, TestLongitude, 0.5d, voiceId, 3);
        for (RedisResponseDTO dto : dtos) {
            redisService.markReceived(voiceId, dto.getId());
        }
    }

    @Test
    public void getLocationsTestV2(){
        Point point = redisService.getLocations(MEMBER_KEY, "1").get(0);
        System.out.println(point.getX() + " " + point.getY());
    }

    @Test
    public void getWithRadiusTestV3(){
        List<RedisResponseDTO> result = redisService.getMembersByRadius(TestLatitude, TestLongitude, 1d, 1L, 3);

        for (RedisResponseDTO m : result) {
            System.out.println("memberId : " + m.getId() + " coord : " + m.getX() + " " + m.getY());
        }
    }

    @Test
    public void msgReceivedTestV2(){
        // markReceived 값에 따라서 true/false 맞게 변경하여 테스트
        Assertions.assertThat(redisService.isReceived(1L, 7L)).isTrue();
        Assertions.assertThat(redisService.isReceived(1L, 6L)).isTrue();
        Assertions.assertThat(redisService.isReceived(2L, 1L)).isFalse();
    }
}
