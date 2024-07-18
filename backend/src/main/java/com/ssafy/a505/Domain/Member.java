package com.ssafy.a505.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Member implements Serializable {
    String name;
    Double x;
    Double y;
    public Member(String name,Double x, Double y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
}