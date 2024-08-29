package com.dtv.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dtv.domain.entity.Voice;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceRepository extends JpaRepository<Voice, Long> {
    @Query("SELECT v FROM Voice v WHERE v.dateTime >= :dateTime ORDER BY v.heartCount DESC")
    List<Voice> findAllByOrderByHeartCountDesc(@Param("dateTime") LocalDateTime dateTime, Pageable pageable);

    @Query("SELECT v FROM Voice v WHERE v.title LIKE %:keyword%")
    Page<Voice> findVoicesWithKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select v from Voice v join Heart h on v.voiceId = h.voice.voiceId where h.member.memberId = :memberId")
    List<Voice> findByMemberWithHeart(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT v FROM Voice v JOIN Pick p ON v.voiceId = p.voice.voiceId WHERE p.member.memberId= :memberId")
    List<Voice> findByMemberWithPick(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select v from Voice v join Spread s on v.voiceId = s.voice.voiceId where s.member.memberId = :memberId")
    List<Voice> findByMemberWithSpread(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT v FROM Voice v WHERE ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(v.longitude, v.latitude)) <= :radius")
    List<Voice> findNearbyVoices(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius);

}
