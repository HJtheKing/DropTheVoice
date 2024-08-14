package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.response.RedisResponseDTO;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.repository.SpreadRepository;
import com.ssafy.a505.domain.repository.VoiceRepository;
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
    private final MemberRepository memberRepository;
    private final SpreadRepository spreadRepository;
    private final VoiceRepository voiceRepository;

    public static final String WS_KEY= "WS_SESSION";

    public static final String MEMBER_KEY = "MEMBER_LOCATION";
    public static final String MEMBER_TIME_KEY = "MEMBER_TIME";


    public static final String VOICE_KEY = "VOICE_LOCATION";
    public static final String VOICE_TIME_KEY = "VOICE_TIME";
    public static final String MSG_KEY_PREFIX = "MSG_RECEIVED_";
    public static final String WS_KEY_PREFIX= "WS_SESSION_";

    // eventListner로 각 세션 관리하기 위함
    public void addSessionId(String sessionID,String userId){
        redisTemplate.opsForValue().set(WS_KEY_PREFIX + sessionID, userId);
    }

    // hash로 동일 키에 hashKey-value 저장 -> 반경 탐색으로 사용하기 위해 memberId 값 저장
    public void addSessionIdV2(String userId){
        redisTemplate.opsForHash().put(WS_KEY, WS_KEY_PREFIX + userId, userId);
    }
    //
    public Set<String> getWsMemberIds(){
        return redisTemplate.opsForHash().values(WS_KEY).stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    public String removeSessionIdAndGetUserId(String sessionID){
        return redisTemplate.opsForValue().getAndDelete(WS_KEY_PREFIX + sessionID).toString();
    }

    // 세션 종료시
    public void removeSessionIdAndGetUserIdV2(String sessionID){
        Object o = redisTemplate.opsForHash().get(WS_KEY, WS_KEY_PREFIX + sessionID);
        if(o != null){
            redisTemplate.opsForHash().delete(WS_KEY, WS_KEY_PREFIX + sessionID);
        }
    }

    // key GEO_KEY에 value 위도, 경도, id 추가(유저/포켓몬) + TIME_KEY에 memberId 추가하여 만료기간 갱신
    public void addLocation(String locationKey, String timeKey, Long id, Double longitude, Double latitude, int expHrs){
        redisTemplate.opsForGeo().add(locationKey, new Point(longitude, latitude), id.toString());
        long expirationTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(expHrs);
        redisTemplate.opsForZSet().add(timeKey, id.toString(), expirationTime);
    }

    // member의 특정 음성 수신 저장
    public void markReceived(Long voiceId, Long memberId){
        redisTemplate.opsForHash().put(MSG_KEY_PREFIX + voiceId, memberId.toString(), true);
        redisTemplate.expire(MSG_KEY_PREFIX + voiceId, 7, TimeUnit.DAYS);
    }

    // 특정 음성 수신한 멤버인지 확인
    public boolean isReceived(Long voiceId, Long memberId){
        return redisTemplate.opsForHash().hasKey(MSG_KEY_PREFIX + voiceId, memberId.toString());
    }

    // KEY에 있는 특정 member/voice들의 위도, 경도 반환
    public List<Point> getLocations(String locationKey, String... id){
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
    public Set<RedisResponseDTO> getMembersByRadius(Double longitude, Double latitude, Double radiusInKm, Long voiceId, int cnt){
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoRadiusCommandArgs args = newGeoRadiusArgs().includeCoordinates();
        GeoResults<GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius(MEMBER_KEY, within, args);

        List<RedisResponseDTO> shuffledList = geoResults.getContent().stream()
                .map(geoResult -> {
                    GeoLocation<Object> content = geoResult.getContent();
                    Point point = content.getPoint();
                    Long memberId = Long.valueOf(content.getName().toString());
                    return new RedisResponseDTO(memberId, point.getX(), point.getY());
                })
                .filter(dto -> !isReceived(voiceId, dto.getId())).distinct().collect(Collectors.toList());

        Collections.shuffle(shuffledList);
        return shuffledList.stream()
                .limit(cnt)
                .collect(Collectors.toSet());
    }

    //  member의 반경 내 현재 접속 중인 유저 반환
    public Set<RedisResponseDTO> getMembersByRadiusV2(Double longitude, Double latitude, Double radiusInKm, Long voiceId, int cnt, Set<String> set) {
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoRadiusCommandArgs args = newGeoRadiusArgs().includeCoordinates();
        GeoResults<GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius(MEMBER_KEY, within, args);

        List<RedisResponseDTO> shuffledList = geoResults.getContent().stream()
                .map(geoResult -> {
                    GeoLocation<Object> content = geoResult.getContent();
                    Point point = content.getPoint();
                    Long memberId = Long.valueOf(content.getName().toString());
                    return new RedisResponseDTO(memberId, point.getX(), point.getY());
                })
                .filter(dto -> !isReceived(voiceId, dto.getId()))
                .filter(dto -> set.contains(dto.getId().toString()))
                .distinct()
                .collect(Collectors.toList());

        Collections.shuffle(shuffledList);
        return shuffledList.stream()
                .limit(cnt)
                .collect(Collectors.toSet());
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
