package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.entity.Member;

public interface MemberService {

    //로그인 하기
    public boolean login(Member member);

    //사용자 등록하기
    public boolean signup(Member member);

    //회원 탈퇴
    public boolean removeUser(String memberId);

    // 유저 이미지 변경
    public boolean setUserImg(String memberId, String newImageName);

}
