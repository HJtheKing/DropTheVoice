package com.ssafy.a505.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.a505.domain.entity.Voice;
import org.springframework.security.core.parameters.P;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
    List<Voice> findAllByTitle(String title, Pageable pageable);

    @Query("SELECT v FROM Voice v WHERE v.dateTime >= :dateTime ORDER BY v.heartCount DESC")
    List<Voice> findAllByOrderByHeartCountDesc(@Param("dateTime") LocalDateTime dateTime, Pageable pageable);

    @Query("SELECT v FROM Voice v WHERE v.title LIKE %:keyword%")
    Page<Voice> findVoicesWithKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select v from Voice v join Heart h on v.voiceId = h.voice.voiceId where h.member.memberId = :memberId")
    List<Voice> findByMemberWithHeart(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select v from Voice v join Spread s on v.voiceId = s.voice.voiceId where s.member.memberId = :memberId")
    List<Voice> findByMemberWithSpread(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT v FROM Voice v WHERE ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(v.longitude, v.latitude)) <= :radius")
    List<Voice> findNearbyVoices(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius);

}
