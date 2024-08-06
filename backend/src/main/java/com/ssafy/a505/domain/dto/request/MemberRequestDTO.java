package com.ssafy.a505.domain.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDTO {
    private String userEmail; // 유저 이메일
    private String userName; // 유저 닉네임
    private String userPassword; // 유저 패스워드
    private String profileImgUrl; // 프로필 이미지 URL
}
