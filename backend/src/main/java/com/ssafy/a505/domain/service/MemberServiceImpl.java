package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.request.MemberDto;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public MemberDto findMemberByName(String name){
        Member member = memberRepository.findByMemberName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_NAME));
        return MemberDto.getMember(member);
    }

    @Override
    public boolean login(MemberDto memberDto) {
        MemberDto target = findMemberByName(memberDto.getMemberName());
        if(target != null && target.getMemberPassword() == memberDto.getMemberPassword()) return true;
        return false;
    }

    // membername 중복 불가
    @Override
    public boolean signup(Member member) {
        if(member.getMemberName().equals("1234") && member.getMemberPassword().equals("1234")){
            return true;
        }
        return false;
    }

    @Override
    public boolean removeUser(String memberId) {
        return false;
    }

    @Override
    public boolean setUserImg(String memberId, String newImageName) {
        return false;
    }
}
