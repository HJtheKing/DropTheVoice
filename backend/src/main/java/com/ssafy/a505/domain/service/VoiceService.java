package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;

import java.util.List;

public interface VoiceService {

    List<Voice> findAllVoice();

    List<Voice> findHeartedByMember(Long memberId);
}
