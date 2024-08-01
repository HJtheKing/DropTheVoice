package com.ssafy.a505.RedisTest;

import com.ssafy.a505.global.util.RedisUtils;
import com.ssafy.a505.domain.entity.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RedisGeoSpatialUtilsTest {

    @Autowired
    private RedisUtils redisUtils;
    public double TestLatitude = 50;
    public double TestLongitude = 50;

    public void addMember(double plus){
        redisUtils.addLocation("geotest",TestLatitude+plus,TestLongitude+plus);
    }

    public List<Coordinate> getMembers(){
        System.out.println("get test at "+System.nanoTime());
        List<Coordinate> positions = redisUtils.getLocationsWithinRadius("geotest",TestLatitude,TestLongitude,1);
        return positions;
    }

    //50,50부터 50.0757,50.0757 까지 1km 맞음.
    @Test
    public void geospatialTest(){
        redisUtils.deleteKey("geotest");
        for(int i=0;i<10000;i++){
            addMember(i*0.00001);
        }
        List<Coordinate> coordinates = getMembers();
        System.out.println("total size is "+ coordinates.size());
        Assertions.assertEquals(757, coordinates.size());
    }
}
