package com.ssafy.a505.domain.dto.request;

import com.ssafy.a505.domain.entity.Member;
import lombok.*;

@Data
@Builder
public class MemberDto {

    private Long memberId; // 유저 아이디
    String memberEmail; // 유저 이메일
    String memberName; // 유저 닉네임
    String memberPassword; // 유저 패스워드
    String profileImgUrl; // 프로필 이미지 URL
    int remainChangeCount; // 남은 음성 변조 수
    int totalSpreadCount; // 총 음성 확산 수
    int totalUploadCount; // 총 음성 업로드 수

    public static MemberDto getMember(Member member) {
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .memberEmail(member.getMemberEmail())
                .memberPassword(member.getMemberPassword())
                .memberName(member.getMemberName())
                .profileImgUrl(member.getProfileImgUrl())
                .remainChangeCount(member.getRemainChangeCount())
                .totalSpreadCount(member.getTotalSpreadCount())
                .totalUploadCount(member.getTotalUploadCount())
                .build();
    }
}
