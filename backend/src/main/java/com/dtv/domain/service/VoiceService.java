package com.dtv.domain.service;

import com.dtv.domain.dto.response.VoiceResponseDTO;
import com.dtv.domain.entity.Member;
import com.dtv.domain.entity.Voice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoiceService {

    List<Voice> getVoiceOrderByHeartCountDesc(int page, int size);

    List<Voice> getAllVoice();

    Page<VoiceResponseDTO> searchVoices(String keyword, int page, int size, String sort);

    List<Voice> findByMemberWithHeart(Long memberId, Pageable pageable);

    List<Voice> findByMemberWithPick(Long memberId, Pageable pageable);

    List<Voice> findByMemberWithSpread(Long memberId, Pageable pageable);

    boolean toggleLike(Long voiceId, Member member);

    boolean togglePick(Long voiceId, Member member);

    Voice getVoiceById(Long voiceId);

    List<VoiceResponseDTO> getNearbyVoices(double latitude, double longitude, double radius, Member member);
}
