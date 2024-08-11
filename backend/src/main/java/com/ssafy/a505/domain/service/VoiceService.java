package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.response.VoiceResponseDTO;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface VoiceService {

    List<Voice> getVoiceOrderByHeartCountDesc(int page, int size);

    List<Voice> getAllVoice();

    List<Voice> findAllByTitle(String title, Pageable pageable);

    Page<VoiceResponseDTO> searchVoices(String keyword, int page, int size, String sort);

    List<Voice> findByMemberWithHeart(Long memberId, Pageable pageable);

    List<Voice> findByMemberWithSpread(Long memberId, Pageable pageable);

    boolean toggleLike(Long voiceId, Member member);

    Voice getVoiceById(Long voiceId);

    List<VoiceResponseDTO> getNearbyVoices(double latitude, double longitude, double radius);
}
