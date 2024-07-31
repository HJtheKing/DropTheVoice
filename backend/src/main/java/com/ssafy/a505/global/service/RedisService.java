package com.ssafy.a505.global.service;

import com.ssafy.a505.domain.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs.*;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Member> redisMemberTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Member> redisMemberTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.redisMemberTemplate = redisMemberTemplate;
        this.redisTemplate = redisTemplate;
    }

    private static final String GEO_KEY = "XXOOO";
    private static final String MSG_KEY_PREFIX = "MSG_RECEIVED_";

    // key GEO_KEY에 value 위도, 경도, member 값 추가 V1
    public void addLocationV1(String name, Double longitude, Double latitude ){
        redisMemberTemplate.opsForGeo().add(GEO_KEY, new Point(longitude, latitude), new Member(name, longitude, latitude));
    }

    // key GEO_KEY에 value 위도, 경도, member 값 추가 V2
    public void addLocationV2(String name, Double longitude, Double latitude){
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(longitude, latitude), name);
    }

    // member의 특정 음성 수신여부 저장
    public void markReceived(String msgId, String name){
        redisTemplate.opsForHash().put(MSG_KEY_PREFIX + msgId, name, true);
    }

    // 특정 음성 수신한 멤버인지 확인
    public boolean isReceived(String msgId, String name){
        return redisTemplate.opsForHash().hasKey(MSG_KEY_PREFIX + msgId, name);
    }

    // GEO_KEY에 있는 특정 member들의 위도, 경도 반환
    public List<Point> getLocations(String... name){
        return redisTemplate.opsForGeo().position(GEO_KEY, name);
    }

    // 값이 없으면 add, 있으면 remove -> add
    public void updateLocation(String name, Double longitude, Double latitude){
        GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
        geoOps.remove(GEO_KEY, name);
        geoOps.add(GEO_KEY, new Point(longitude, latitude), name);
    }

    //  member의 반경 내의 다른 멤버 조회 V1
    public List<Member> getMembersByRadius(Double longitude, Double latitude, Double radiusInKm, String msgId, int cnt){
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoResults<GeoLocation<Member>> geoResults = redisMemberTemplate.opsForGeo().radius(GEO_KEY, within);
        List<Member> members = geoResults.getContent().stream() // stream<GeoResult<Member> 로 변환
                .map(result -> result.getContent().getName()) // stream<Member> 로 변환
                .filter(m -> !isReceived(msgId, m.getName())) // 해당 음성을 수신했는지 filter
                .collect(Collectors.toList());
        Collections.shuffle(members);
        return members.stream().limit(cnt).collect(Collectors.toList());
    }

    //  member의 반경 내의 다른 멤버 조회 V2
    public List<Member> getMembersByRadiusV2(Double longitude, Double latitude, Double radiusInKm, String msgId, int cnt){
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoRadiusCommandArgs args = newGeoRadiusArgs().includeCoordinates();
        GeoResults<GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius(GEO_KEY, within, args);

        List<Member> result = geoResults.getContent().stream()
                .map(geoResult -> {
                    GeoLocation<Object> content = geoResult.getContent();
                    String name = content.getName().toString();
                    Point point = content.getPoint();
                    return new Member(name, point.getX(), point.getY());
                })
                .filter(m -> !isReceived(msgId, m.getName()))
                .collect(Collectors.toList());

        Collections.shuffle(result);
        return result.stream()
                .limit(cnt)
                .collect(Collectors.toList());
    }
}
