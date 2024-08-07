package com.ssafy.a505.RedisTest;

import com.ssafy.a505.domain.dto.response.RedisResponseDTO;
import com.ssafy.a505.domain.service.RedisService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.util.List;

@SpringBootTest
public class RedisGeoSpatialServiceTest {

    @Autowired
    private RedisService redisService;

    public double TestLatitude = 50;
    public double TestLongitude = 50;

    @Test
    public void addTestV3(){
        redisService.addLocation(1L, TestLatitude + 0.0005, TestLongitude + 0.0005);
        redisService.addLocation(2L, TestLatitude + 0.00051, TestLongitude + 0.00051);
        redisService.addLocation(3L, TestLatitude + 0.00052, TestLongitude + 0.00052);
        redisService.addLocation(4L, TestLatitude + 0.00053, TestLongitude + 0.00053);
        redisService.addLocation(5L, TestLatitude + 0.00054, TestLongitude + 0.00054);
        redisService.addLocation(6L, TestLatitude + 0.00055, TestLongitude + 0.00055);
        redisService.addLocation(7L, TestLatitude + 0.00056, TestLongitude + 0.00056);
        redisService.addLocation(8L, TestLatitude + 0.00057, TestLongitude + 0.00057);
        redisService.addLocation(9L, TestLatitude + 0.00058, TestLongitude + 0.00058);
    }

    @Test
    public void markMsgReceivedTestV2(){
        Long voiceId = 1L;
        List<RedisResponseDTO> dtos = redisService.getMembersByRadius(TestLatitude, TestLongitude, 0.5d, voiceId, 3);
        for (RedisResponseDTO dto : dtos) {
            redisService.markReceived(voiceId, dto.getMemberId());
        }
    }

    @Test
    public void getLocationsTestV2(){
        Point point = redisService.getLocations(1L).get(0);
        System.out.println(point.getX() + " " + point.getY());
    }

    @Test
    public void getWithRadiusTestV3(){
        List<RedisResponseDTO> result = redisService.getMembersByRadius(TestLatitude, TestLongitude, 1d, 1L, 3);

        for (RedisResponseDTO m : result) {
            System.out.println("memberId : " + m.getMemberId() + " coord : " + m.getX() + " " + m.getY());
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
