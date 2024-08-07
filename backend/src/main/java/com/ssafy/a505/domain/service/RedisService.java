package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.response.RedisResponseDTO;
import com.ssafy.a505.domain.entity.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs.*;

@Slf4j
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Coordinate> redisMemberTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Coordinate> redisMemberTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.redisMemberTemplate = redisMemberTemplate;
        this.redisTemplate = redisTemplate;
    }

    private static final String GEO_KEY = "MEMBER_LOCATION";
    private static final String MSG_KEY_PREFIX = "MSG_RECEIVED_";
    private static final String TIME_KEY = "MEMBER_TIME";

    // key GEO_KEY에 value 위도, 경도, memberId 추가 + TIME_KEY에 memberId 추가하여 최근 로그인 기록
    public void addLocationV3(Long memberId, Double longitude, Double latitude){
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(longitude, latitude), memberId.toString());
        long expirationTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1);
        redisTemplate.opsForZSet().add(TIME_KEY, memberId.toString(), expirationTime);
    }

    // member의 특정 음성 수신여부 저장
    public void markReceivedV2(Long voiceId, Long memberId){
        redisTemplate.opsForHash().put(MSG_KEY_PREFIX + voiceId, memberId.toString(), true);
    }
    // 특정 음성 수신한 멤버인지 확인
    public boolean isReceivedV2(Long voiceId, Long memberId){
        return redisTemplate.opsForHash().hasKey(MSG_KEY_PREFIX + voiceId, memberId.toString());
    }
    // GEO_KEY에 있는 특정 member들의 위도, 경도 반환
    public List<Point> getLocationsV2(Long... memberId){
        return redisTemplate.opsForGeo().position(GEO_KEY, memberId);
    }
    // 값이 없으면 add, 있으면 remove -> add
    public void updateLocationV2(Long memberId, Double longitude, Double latitude){
        GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
        geoOps.remove(GEO_KEY, memberId);
        geoOps.add(GEO_KEY, new Point(longitude, latitude), memberId);
        long expirationTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1);
        redisTemplate.opsForZSet().add(TIME_KEY, memberId.toString(), expirationTime);
    }
    //  member의 반경 내의 다른 멤버 조회 V3
    public List<RedisResponseDTO> getMembersByRadiusV3(Double longitude, Double latitude, Double radiusInKm, Long voiceId, int cnt){
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoRadiusCommandArgs args = newGeoRadiusArgs().includeCoordinates();
        GeoResults<GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius(GEO_KEY, within, args);

        List<RedisResponseDTO> result = geoResults.getContent().stream()
                .map(geoResult -> {
                    GeoLocation<Object> content = geoResult.getContent();
                    Point point = content.getPoint();
                    Long memberId = Long.valueOf(content.getName().toString());
                    return new RedisResponseDTO(memberId, point.getX(), point.getY());
                })
                .filter(dto -> !isReceivedV2(voiceId, dto.getMemberId()))
                .collect(Collectors.toList());

        Collections.shuffle(result);
        return result.stream()
                .limit(cnt)
                .collect(Collectors.toList());
    }
    // 30초마다 만료된 사용자 위치 제거
    @Scheduled(fixedRate = 30000)
    public void cleanExpiredLocations(){
        log.info("clean Activated");
        long currentTime = System.currentTimeMillis();
        Set<Object> expiredMembers = redisTemplate.opsForZSet().rangeByScore(TIME_KEY, 0, currentTime);

        if(expiredMembers != null && !expiredMembers.isEmpty()){
            for(Object memberId : expiredMembers){
                redisTemplate.opsForGeo().remove(GEO_KEY, memberId.toString());
                redisTemplate.opsForZSet().remove(TIME_KEY, memberId.toString());
            }
        }
    }







    // key GEO_KEY에 value 위도, 경도, member 값 추가 V1
    public void addLocationV1(String name, Double longitude, Double latitude ){
        redisMemberTemplate.opsForGeo().add(GEO_KEY, new Point(longitude, latitude), new Coordinate(name, longitude, latitude));
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
    public List<Coordinate> getMembersByRadius(Double longitude, Double latitude, Double radiusInKm, String msgId, int cnt){
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoResults<GeoLocation<Coordinate>> geoResults = redisMemberTemplate.opsForGeo().radius(GEO_KEY, within);
        List<Coordinate> coordinates = geoResults.getContent().stream() // stream<GeoResult<Member> 로 변환
                .map(result -> result.getContent().getName()) // stream<Member> 로 변환
                .filter(m -> !isReceived(msgId, m.getName())) // 해당 음성을 수신했는지 filter
                .collect(Collectors.toList());
        Collections.shuffle(coordinates);
        return coordinates.stream().limit(cnt).collect(Collectors.toList());
    }

    //  member의 반경 내의 다른 멤버 조회 V2
    public List<Coordinate> getMembersByRadiusV2(Double longitude, Double latitude, Double radiusInKm, String msgId, int cnt){
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoRadiusCommandArgs args = newGeoRadiusArgs().includeCoordinates();
        GeoResults<GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius(GEO_KEY, within, args);

        List<Coordinate> result = geoResults.getContent().stream()
                .map(geoResult -> {
                    GeoLocation<Object> content = geoResult.getContent();
                    String name = content.getName().toString();
                    Point point = content.getPoint();
                    return new Coordinate(name, point.getX(), point.getY());
                })
                .filter(m -> !isReceived(msgId, m.getName()))
                .collect(Collectors.toList());

        Collections.shuffle(result);
        return result.stream()
                .limit(cnt)
                .collect(Collectors.toList());
    }
}
