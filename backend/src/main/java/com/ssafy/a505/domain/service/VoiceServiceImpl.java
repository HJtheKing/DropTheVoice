package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.response.VoiceResponseDTO;
import com.ssafy.a505.domain.entity.Heart;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.HeartRepository;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.repository.VoiceRepository;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoiceServiceImpl implements VoiceService{
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final VoiceRepository voiceRepository;


    @Override
    public List<Voice> getVoiceOrderByHeartCountDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return voiceRepository.findAllByOrderByHeartCountDesc(pageable);
    }

    @Override
    public List<Voice> findByMemberWithHeart(Long memberId, Pageable pageable) {
        return voiceRepository.findByMemberWithHeart(memberId, pageable);
    }


    @Override
    public List<Voice> findHeartedByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_ID));
        List<Heart> hearts = heartRepository.findByMember(member);
        return hearts.stream().map(Heart::getVoice).collect(Collectors.toList());
    }

    @Override
    public List<Voice> findByMemberWithSpread(Long memberId, Pageable pageable) {
        return voiceRepository.findByMemberWithSpread(memberId, pageable);
    }

    @Override
    public List<Voice> findByTitleContaining(String userNam, Pageable pageable) {
        return voiceRepository.findByTitleContaining(userNam, pageable);
    }

    @Override
    public List<Voice> findAllByTitle(String title, Pageable pageable) {
        return voiceRepository.findAllByTitle(title, pageable);
    }

    @Override
    public Voice findById(Long id) {
            return voiceRepository.findById(id).get();
    }

    public Page<VoiceResponseDTO> searchVoices(String keyword, int page, int size, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, getSort(sort));
        return voiceRepository.findVoicesWithKeyword(keyword, pageRequest).map(VoiceResponseDTO::fromEntity);
    }

    @Override
    public List<Voice> findAllByMember_MemberId(Long memberId, Pageable pageable) {
        return null;
    }

    private Sort getSort(String sort) {
        switch (sort) {
            case "listenCount":
                return Sort.by(Sort.Direction.DESC, "listenCount");
            case "heartCount":
                return Sort.by(Sort.Direction.DESC, "heartCount");
            case "latest":
            default:
                return Sort.by(Sort.Direction.DESC, "dateTime");
            }
        }
}
