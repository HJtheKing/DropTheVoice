package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.request.MemberRequestDTO;
import com.ssafy.a505.domain.dto.request.PasswordRequestDTO;
import com.ssafy.a505.domain.dto.response.MemberResponseDTO;
import com.ssafy.a505.domain.entity.Member;

public interface MemberService {

    public MemberRequestDTO findMemberByName(String name);

    //회원 정보 조회
    MemberResponseDTO getMemberByMemberId(long id);

    //로그인 하기
    long login(MemberRequestDTO memberRequestDTO);

    //사용자 등록하기
    MemberResponseDTO registerMember(MemberRequestDTO memberRequestDTO);

    //회원 탈퇴
    boolean removeUser(long memberId);

    //비밀번호 변경
    boolean changePassword(PasswordRequestDTO passwordRequestDTO);

    // 유저 이미지 변경
    boolean setUserImg(String memberId, String newImageName);

    int findRemainChangeCount(Long memberId);

    boolean isUserNameDuplicate(String userName);

}
