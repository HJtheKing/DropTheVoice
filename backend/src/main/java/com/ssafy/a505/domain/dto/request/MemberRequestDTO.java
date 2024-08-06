package com.ssafy.a505.domain.dto.request;
import com.ssafy.a505.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDTO {
    private long memberId;
    private String memberEmail; // 유저 이메일
    private String memberName; // 유저 닉네임
    private String memberPassword; // 유저 패스워드
    private String profileImgUrl; // 프로필 이미지 URL

    public static MemberRequestDTO getMember(Member member) {
        return MemberRequestDTO.builder()
                .memberId(member.getMemberId())
                .memberEmail(member.getMemberEmail())
                .memberPassword(member.getMemberPassword())
                .memberName(member.getMemberName())
                .profileImgUrl(member.getProfileImgUrl())
                .build();
    }

}
