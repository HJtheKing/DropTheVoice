package com.ssafy.a505.domain.repository;

import com.ssafy.a505.domain.entity.Voice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
    List<Voice> findByTitleContaining(String userNam, Pageable pageable);
    List<Voice> findALlByTitle(String title, Pageable pageable);

    List<Voice> findAllByMember_MemberId(Long memberId, Pageable pageable);

    // 특정 유저의 좋아요 누른 음성 반환
    @Query("select v from Voice v join Heart h on v.voiceId = h.voice.voiceId where h.member.memberId = :memberId")
    List<Voice> findByMemberWithHeart(Long memberId, Pageable pageable);

    // 특정 유저가 주은 확산 음성 반환
    @Query("select v from Voice v join Spread s on v.voiceId = s.voice.voiceId where s.member.memberId = :memberId")
    List<Voice> findByMemberWithSpread(Long memberId, Pageable pageable);
}
