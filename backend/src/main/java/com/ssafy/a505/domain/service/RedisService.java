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

    public static final String MEMBER_KEY = "MEMBER_LOCATION";
    public static final String MEMBER_TIME_KEY = "MEMBER_TIME";

    public static final String VOICE_KEY = "VOICE_LOCATION";
    public static final String VOICE_TIME_KEY = "VOICE_TIME";
    public static final String MSG_KEY_PREFIX = "MSG_RECEIVED_";

    // key GEO_KEY에 value 위도, 경도, id 추가(유저/포켓몬) + TIME_KEY에 memberId 추가하여 최근 로그인 기록
    public void addLocation(String locationKey, String timeKey, Long id, Double longitude, Double latitude, int expHrs){
        redisTemplate.opsForGeo().add(locationKey, new Point(longitude, latitude), id.toString());
        long expirationTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(expHrs);
        redisTemplate.opsForZSet().add(timeKey, id.toString(), expirationTime);
    }

    // member의 특정 음성 수신 저장
    public void markReceived(Long voiceId, Long memberId){
        redisTemplate.opsForHash().put(MSG_KEY_PREFIX + voiceId, memberId.toString(), true);
    }

    // 특정 음성 수신한 멤버인지 확인
    public boolean isReceived(Long voiceId, Long memberId){
        return redisTemplate.opsForHash().hasKey(MSG_KEY_PREFIX + voiceId, memberId.toString());
    }

    // KEY에 있는 특정 member/voice들의 위도, 경도 반환
    public List<Point> getLocations(String locationKey, Long... id){
        return redisTemplate.opsForGeo().position(locationKey, id);
    }

    // 값이 없으면 add, 있으면 remove -> add
    public void updateLocation(Long memberId, Double longitude, Double latitude){
        GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
        geoOps.remove(MEMBER_KEY, memberId);
        geoOps.add(MEMBER_KEY, new Point(longitude, latitude), memberId);
        long expirationTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
        redisTemplate.opsForZSet().add(MEMBER_TIME_KEY, memberId.toString(), expirationTime);
    }

    //  member의 반경 내의 다른 멤버 조회 V3
    public List<RedisResponseDTO> getMembersByRadius(Double longitude, Double latitude, Double radiusInKm, Long voiceId, int cnt){
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoRadiusCommandArgs args = newGeoRadiusArgs().includeCoordinates();
        GeoResults<GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius(MEMBER_KEY, within, args);

        List<RedisResponseDTO> result = geoResults.getContent().stream()
                .map(geoResult -> {
                    GeoLocation<Object> content = geoResult.getContent();
                    Point point = content.getPoint();
                    Long memberId = Long.valueOf(content.getName().toString());
                    return new RedisResponseDTO(memberId, point.getX(), point.getY());
                })
                .filter(dto -> !isReceived(voiceId, dto.getId()))
                .collect(Collectors.toList());

        Collections.shuffle(result);
        return result.stream()
                .limit(cnt)
                .collect(Collectors.toList());
    }

    public List<RedisResponseDTO> getVoicesByRadius(Double longitude, Double latitude, Double radiusInKm, Long memberId){
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoRadiusCommandArgs args = newGeoRadiusArgs().includeCoordinates();
        GeoResults<GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius(VOICE_KEY, within, args);

        List<RedisResponseDTO> result = geoResults.getContent().stream()
                .map(geoResult -> {
                    GeoLocation<Object> content = geoResult.getContent();
                    Point point = content.getPoint();
                    Long voiceId = Long.valueOf(content.getName().toString());
                    return new RedisResponseDTO(voiceId, point.getX(), point.getY());
                })
                .filter(dto -> !isReceived(dto.getId(), memberId))
                .collect(Collectors.toList());
        return result;
    }

    // 1분마다 만료된 사용자 위치 제거
    @Scheduled(fixedRate = 60000)
    public void cleanExpiredData(){
        log.info("clean Activated");
        long currentTime = System.currentTimeMillis();
        Set<Object> expiredMembers = redisTemplate.opsForZSet().rangeByScore(MEMBER_TIME_KEY, 0, currentTime);
        Set<Object> expiredVoices = redisTemplate.opsForZSet().rangeByScore(VOICE_TIME_KEY, 0, currentTime);

        if(expiredMembers != null && !expiredMembers.isEmpty()){
            for(Object memberId : expiredMembers){
                redisTemplate.opsForGeo().remove(MEMBER_KEY, memberId.toString());
                redisTemplate.opsForZSet().remove(MEMBER_TIME_KEY, memberId.toString());
            }
        }
        if(expiredVoices != null && !expiredVoices.isEmpty()){
            for(Object voiceId : expiredVoices){
                redisTemplate.opsForGeo().remove(VOICE_KEY, voiceId.toString());
                redisTemplate.opsForZSet().remove(VOICE_TIME_KEY, voiceId.toString());
            }
        }
    }
}
