package com.ssafy.a505.RedisTest;

import com.ssafy.a505.global.service.RedisService;
import com.ssafy.a505.domain.entity.Member;
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
    public void addTestV1(){
        redisService.addLocationV1("test1", TestLatitude + 0.0005, TestLongitude + 0.0005);
        redisService.addLocationV1("test2", TestLatitude + 0.00051, TestLongitude + 0.00051);
        redisService.addLocationV1("test3", TestLatitude + 0.00052, TestLongitude + 0.00052);
        redisService.addLocationV1("test4", TestLatitude + 0.00053, TestLongitude + 0.00053);
        redisService.addLocationV1("test5", TestLatitude + 0.00054, TestLongitude + 0.00054);
        redisService.addLocationV1("test6", TestLatitude + 0.00055, TestLongitude + 0.00055);
        redisService.addLocationV1("test7", TestLatitude + 0.00056, TestLongitude + 0.00056);
        redisService.addLocationV1("test8", TestLatitude + 0.00057, TestLongitude + 0.00057);
        redisService.addLocationV1("test9", TestLatitude + 0.00058, TestLongitude + 0.00058);
    }
    @Test
    public void addTestV2(){
        redisService.addLocationV2("test1", TestLatitude + 0.0005, TestLongitude + 0.0005);
        redisService.addLocationV2("test2", TestLatitude + 0.00051, TestLongitude + 0.00051);
        redisService.addLocationV2("test3", TestLatitude + 0.00052, TestLongitude + 0.00052);
        redisService.addLocationV2("test4", TestLatitude + 0.00053, TestLongitude + 0.00053);
        redisService.addLocationV2("test5", TestLatitude + 0.00054, TestLongitude + 0.00054);
        redisService.addLocationV2("test6", TestLatitude + 0.00055, TestLongitude + 0.00055);
        redisService.addLocationV2("test7", TestLatitude + 0.00056, TestLongitude + 0.00056);
        redisService.addLocationV2("test8", TestLatitude + 0.00057, TestLongitude + 0.00057);
        redisService.addLocationV2("test9", TestLatitude + 0.00058, TestLongitude + 0.00058);
    }

    @Test
    public void markMsgReceivedTest(){
        String msgId = "1";
        List<Member> members = redisService.getMembersByRadiusV2(TestLatitude, TestLongitude, 0.5d, msgId, 3);
        for (Member m : members) {
            redisService.markReceived(msgId, m.getName());
        }
    }

    @Test
    public void getLocationsTest(){
        Point point = redisService.getLocations("byeongGwan").get(0);
        System.out.println(point.getX() + " " + point.getY());
    }

    @Test
    public void getWithRadiusTestV1(){
        redisService.addLocationV1("test5", TestLatitude + 0.00052, TestLongitude + 0.00052);
        List<Member> result = redisService.getMembersByRadius(TestLatitude, TestLongitude, 1d, "1", 3);
        for (Member m : result) {
            System.out.println("name : " + m.getName() + " coord : " + m.getX() + " " + m.getY());
        }
    }
    @Test
    public void getWithRadiusTestV2(){
        List<Member> result = redisService.getMembersByRadiusV2(TestLatitude, TestLongitude, 1d, "1", 3);
        for (Member m : result) {
            System.out.println("name : " + m.getName() + " coord : " + m.getX() + " " + m.getY());
        }
    }

    @Test
    public void msgReceivedTest(){
        Assertions.assertThat(redisService.isReceived("1", "test2")).isTrue();
        Assertions.assertThat(redisService.isReceived("1", "test99999")).isFalse();
        Assertions.assertThat(redisService.isReceived("2", "test2")).isFalse();
    }
}
