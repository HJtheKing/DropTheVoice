package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.request.MemberRequestDTO;
import com.ssafy.a505.domain.dto.request.PasswordRequestDTO;
import com.ssafy.a505.domain.dto.response.MailDTO;
import com.ssafy.a505.domain.dto.response.MemberResponseDTO;
import com.ssafy.a505.domain.entity.Member;
import org.springframework.web.multipart.MultipartFile;

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

    String getTempPassword();

    //임시비밀번호 메일 생성 및 비밀번호 변경
    MailDTO createMailAndChangePassword(MemberRequestDTO memberRequestDTO);
    
    // 메일 전송
    void sendMail(MailDTO mailDTO);

    // 유저 이미지 변경
    boolean setUserImg(long memberId, MultipartFile file);

    int findRemainChangeCount(Long memberId);

    boolean isUserNameDuplicate(String userName);

    MemberResponseDTO getMemberByMemberEmail(MemberRequestDTO memberRequestDTO);

}
