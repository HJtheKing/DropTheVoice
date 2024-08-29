package com.dtv.domain.dto.response;

import com.dtv.domain.entity.Heart;
import com.dtv.domain.entity.Member;
import com.dtv.domain.entity.Voice;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {
    private Long memberId; // 유저 아이디
    private String memberEmail; // 유저 이메일
    private String memberName; // 유저 닉네임
    private String profileImgUrl; // 프로필 이미지 URL
    private int remainChangeCount; // 남은 음성 변조 수
    private int totalSpreadCount; // 총 음성 확산 수
    private int totalUploadCount; // 총 음성 업로드 수
    private List<Voice> voices; // 유저의 음성 목록
    private Set<Heart> hearts; // 유저의 하트 목록

    public static MemberResponseDTO getMember(Member member) {
        return MemberResponseDTO.builder()
                .memberId(member.getMemberId())
                .memberEmail(member.getMemberEmail())
                .memberName(member.getMemberName())
                .profileImgUrl(member.getProfileImgUrl())
                .remainChangeCount(member.getRemainChangeCount())
                .totalSpreadCount(member.getTotalSpreadCount())
                .totalUploadCount(member.getTotalUploadCount())
                .build();
    }
}
