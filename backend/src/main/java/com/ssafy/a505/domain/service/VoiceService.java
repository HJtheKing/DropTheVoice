package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoiceService {

    List<Voice> findAllVoice();

    List<Voice> findHeartedByMember(Long memberId);

    List<Voice> findByTitleContaining(String userNam, Pageable pageable);

    List<Voice> findALlByTitle(String title, Pageable pageable);

    Voice findById(Long id);

    List<Voice> findAllByMember_MemberId(Long memberId, Pageable pageable);

    List<Voice> findByMemberWithHeart(Long memberId, Pageable pageable);

    List<Voice> findByMemberWithSpread(Long memberId, Pageable pageable);
}
