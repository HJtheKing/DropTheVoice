package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.request.MemberRequestDTO;
import com.ssafy.a505.domain.dto.response.MemberResponseDTO;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public int findRemainChangeCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_ID));
        return member.getRemainChangeCount();
    }

    @Override
    public boolean isUserNameDuplicate(String userName) {
        return memberRepository.findByUserName(userName).isPresent();
    }

    @Override
    public MemberResponseDTO registerMember(MemberRequestDTO memberRequestDto) {
        Member member = Member.builder()
                .userEmail(memberRequestDto.getUserEmail())
                .userName(memberRequestDto.getUserName())
                .userPassword(memberRequestDto.getUserPassword())
                .profileImgUrl(memberRequestDto.getProfileImgUrl())
                .build();

        Member savedMember = memberRepository.save(member);

        MemberResponseDTO memberResponseDto = new MemberResponseDTO();
        memberResponseDto.setMemberId(savedMember.getMemberId());
        memberResponseDto.setUserEmail(savedMember.getUserEmail());
        memberResponseDto.setUserName(savedMember.getUserName());
        memberResponseDto.setProfileImgUrl(savedMember.getProfileImgUrl());
        memberResponseDto.setRemainChangeCount(savedMember.getRemainChangeCount());
        memberResponseDto.setTotalSpreadCount(savedMember.getTotalSpreadCount());
        memberResponseDto.setTotalUploadCount(savedMember.getTotalUploadCount());
        memberResponseDto.setVoices(savedMember.getVoices());
        memberResponseDto.setHearts(savedMember.getHearts());

        return memberResponseDto;
    }

    }
