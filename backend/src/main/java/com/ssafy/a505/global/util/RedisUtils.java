package com.ssafy.a505.global.util;

import com.ssafy.a505.domain.entity.Coordinate;
import io.lettuce.core.RedisCommandExecutionException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Coordinate> redisMemberTemplate;


    public RedisUtils(RedisTemplate<String, Object> redisTemplate, RedisTemplate<String, Coordinate> redisMemberTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisMemberTemplate = redisMemberTemplate;
    }

    public void setLong(String key, Long value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setLongTimeLimit(String key, Long value, Long expiredTime) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(expiredTime));
    }

    public Long getLong(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;
        return (long) ((int) value);
    }

    public void setString(String key, String value, Long expiredTime) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(expiredTime));
    }

    public String getVerificationToken(String key) throws RedisCommandExecutionException {
        Object value = redisTemplate.opsForValue().get(key);
        return Objects.toString(value);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void addLocation(String key,Double latitude, Double longitude) {
        GeoOperations<String, Coordinate> geoOps = redisMemberTemplate.opsForGeo();
        Point point = new Point(latitude,longitude);
        geoOps.add(key, new RedisGeoCommands.GeoLocation<Coordinate>(new Coordinate(UUID.randomUUID() + "",latitude,longitude), point));
    }

    public List<Coordinate> getLocationsWithinRadius(String key, double longitude, double latitude, double radius) {
        GeoOperations<String, Coordinate> geoOps = redisMemberTemplate.opsForGeo();
        Circle within = new Circle(new Point(longitude, latitude), new Distance(radius, RedisGeoCommands.DistanceUnit.KILOMETERS));
        GeoResults<RedisGeoCommands.GeoLocation<Coordinate>> geoResults = geoOps.radius(key, within);
        return geoResults.getContent().stream()
                .map(result -> result.getContent().getName())
                .collect(Collectors.toList());
    }

    public void deleteGeoInfos(List<Coordinate> coordinates){
        for(Coordinate coordinate : coordinates){
            deleteGeoInfo(coordinate);
        }
    }

    public void deleteGeoInfo(Coordinate coordinate){
        GeoOperations<String, Coordinate> geoOps = redisMemberTemplate.opsForGeo();
        geoOps.remove("geotest", coordinate);
    }

    // 저장된 위치의 좌표 가져오기
    public List<Point> getPosition(String key, String member) {
        GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
        return geoOps.position(key, member);
    }

    public void addOrRemoveUserLocationToCache(String roomId, String servernum, boolean add) {
    }
}