package com.ssafy.a505.RedisTest;

import com.ssafy.a505.Config.Redis.RedisUtils;
import com.ssafy.a505.Domain.Member;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@SpringBootTest
public class RedisGeoSpatialTest {

    @Autowired
    private RedisUtils redisUtils;
    public double TestLatitude = 50;
    public double TestLongitude = 50;

    public void addMember(double plus){
        redisUtils.addLocation("geotest",TestLatitude+plus,TestLongitude+plus);
    }

    public List<Member> getMembers(){
        System.out.println("get test at "+System.nanoTime());
        List<Member> positions = redisUtils.getLocationsWithinRadius("geotest",TestLatitude,TestLongitude,1);
        return positions;
    }

    //50,50부터 50.0757,50.0757 까지 1km 맞음.
    @Test
    public void geospatialTest(){
        redisUtils.deleteKey("geotest");
        for(int i=0;i<10000;i++){
            addMember(i*0.00001);
        }
        List<Member> members = getMembers();
        System.out.println("total size is "+members.size());
        Assertions.assertEquals(757,members.size());
    }
}
