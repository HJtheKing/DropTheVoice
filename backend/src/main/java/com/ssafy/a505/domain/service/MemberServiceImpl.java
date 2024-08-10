package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.request.MemberRequestDTO;
import com.ssafy.a505.domain.dto.request.PasswordRequestDTO;
import com.ssafy.a505.domain.dto.response.MemberResponseDTO;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MemberResponseDTO getMemberByMemberId(long id){
        Member member = memberRepository.findByMemberId(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_ID));
        return MemberResponseDTO.getMember(member);
    }

    @Override
    public MemberRequestDTO findMemberByName(String name){
        Member member = memberRepository.findByMemberName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_NAME));
        return MemberRequestDTO.getMember(member);
    }

    @Override
    public long login(MemberRequestDTO memberRequestDTO) {
        MemberRequestDTO target = findMemberByName(memberRequestDTO.getMemberName());
        if(target != null) {
            if(passwordEncoder.matches(memberRequestDTO.getMemberPassword(), target.getMemberPassword())){
                return target.getMemberId();
            }
            return -1;
        }
        return -1;
    }

    // membername 중복 불가(front에서 처리)
    @Override
    public MemberResponseDTO registerMember(MemberRequestDTO memberRequestDto) {
        String encodedPassword = passwordEncoder.encode(memberRequestDto.getMemberPassword());

        Member member = Member.builder()
                .memberEmail(memberRequestDto.getMemberEmail())
                .memberName(memberRequestDto.getMemberName())
                .memberPassword(encodedPassword)
                .profileImgUrl(memberRequestDto.getProfileImgUrl())
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberResponseDTO.getMember(savedMember);
    }

    //회원 삭제
    @Override
    public boolean removeUser(long memberId) {
        if (memberRepository.existsById(memberId)) {
            memberRepository.deleteById(memberId);
            return true;
        }
        return false;
    }

    //비밀번호 변경
    public boolean changePassword(PasswordRequestDTO passwordRequestDTO) {
        Optional<Member> findOptional = memberRepository.findByMemberName(passwordRequestDTO.getMemberName());
        Member target = findOptional.get();
        if(target != null) {
            if(passwordEncoder.matches(passwordRequestDTO.getOldPassword(), target.getMemberPassword())){
                String encodedPassword = passwordEncoder.encode(passwordRequestDTO.getNewPassword());
                //target.setMemberPassword(encodedPassword);
                target.setMemberPassword(encodedPassword);
                memberRepository.save(target);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean setUserImg(String memberId, String newImageName) {
        return false;
    }

    @Override
    public int findRemainChangeCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_ID));
        return member.getRemainChangeCount();
    }

    @Override
    public boolean isUserNameDuplicate(String userName) {
        return memberRepository.findByMemberName(userName).isPresent();
    }


}


