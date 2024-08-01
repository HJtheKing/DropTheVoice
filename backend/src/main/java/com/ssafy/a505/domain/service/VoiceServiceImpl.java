package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.entity.Heart;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.HeartRepository;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.repository.VoiceRepository;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoiceServiceImpl implements VoiceService {

    private final VoiceRepository voiceRepository;
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<Voice> findAllVoice() {
        return voiceRepository.findAll();
    }

    @Override
    public List<Voice> findHeartedByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_ID));
        List<Heart> hearts = heartRepository.findByMember(member);
        return hearts.stream().map(Heart::getVoice).collect(Collectors.toList());
    }

}
