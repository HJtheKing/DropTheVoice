package com.ssafy.a505.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@Embeddable
public class Coordinate implements Serializable {
    String name;
    Double x;
    Double y;
    public Coordinate(String name, Double x, Double y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
}
