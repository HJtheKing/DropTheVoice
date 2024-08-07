package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.response.RedisResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String GEO_KEY = "MEMBER_LOCATION";
    private static final String MSG_KEY_PREFIX = "MSG_RECEIVED_";
    private static final String TIME_KEY = "MEMBER_TIME";

    // key GEO_KEY에 value 위도, 경도, memberId 추가 + TIME_KEY에 memberId 추가하여 최근 로그인 기록
    public void addLocation(Long memberId, Double longitude, Double latitude){
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(longitude, latitude), memberId.toString());
        long expirationTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
        redisTemplate.opsForZSet().add(TIME_KEY, memberId.toString(), expirationTime);
    }

    // member의 특정 음성 수신여부 저장
    public void markReceived(Long voiceId, Long memberId){
        redisTemplate.opsForHash().put(MSG_KEY_PREFIX + voiceId, memberId.toString(), true);
    }

    // 특정 음성 수신한 멤버인지 확인
    public boolean isReceived(Long voiceId, Long memberId){
        return redisTemplate.opsForHash().hasKey(MSG_KEY_PREFIX + voiceId, memberId.toString());
    }

    // GEO_KEY에 있는 특정 member들의 위도, 경도 반환
    public List<Point> getLocations(Long... memberId){
        return redisTemplate.opsForGeo().position(GEO_KEY, memberId);
    }

    // 값이 없으면 add, 있으면 remove -> add
    public void updateLocation(Long memberId, Double longitude, Double latitude){
        GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
        geoOps.remove(GEO_KEY, memberId);
        geoOps.add(GEO_KEY, new Point(longitude, latitude), memberId);
        long expirationTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
        redisTemplate.opsForZSet().add(TIME_KEY, memberId.toString(), expirationTime);
    }

    //  member의 반경 내의 다른 멤버 조회 V3
    public List<RedisResponseDTO> getMembersByRadius(Double longitude, Double latitude, Double radiusInKm, Long voiceId, int cnt){
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
                .filter(dto -> !isReceived(voiceId, dto.getMemberId()))
                .collect(Collectors.toList());

        Collections.shuffle(result);
        return result.stream()
                .limit(cnt)
                .collect(Collectors.toList());
    }

    // 1분마다 만료된 사용자 위치 제거
    @Scheduled(fixedRate = 60000)
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
}
