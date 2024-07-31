package com.ssafy.a505.domain.entity;

import lombok.Data;
import lombok.Getter;

@Data
public class User {

    long userId; // 유저 아이디
    String userEmail; // 유저 이메일
    String userName; // 유저 닉네임
    String userPassword; // 유저 패스워드
    String profileImgUrl; // 프로필 이미지 URL
    int remainChangeCount; // 남은 음성 변조 수
    int totalSpreadCount; // 총 음성 확산 수
    int totalUploadCount; // 총 음성 업로드 수
}
