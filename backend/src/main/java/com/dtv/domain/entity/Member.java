package com.dtv.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonManagedReference
    private Long memberId; // 유저 아이디
    String memberEmail; // 유저 이메일
    String memberName; // 유저 닉네임
    String memberPassword; // 유저 패스워드
    String profileImgUrl; // 프로필 이미지 URL
    @Builder.Default
    int remainChangeCount = 5; // 남은 음성 변조 수
    int totalSpreadCount; // 총 음성 확산 수
    int totalUploadCount; // 총 음성 업로드 수
    @Builder.Default
    boolean hasNew = false;

    @Embedded
    private Coordinate coordinate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Voice> voices = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("heartAt ASC")
    private Set<Heart> hearts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("pickAt ASC")
    private Set<Pick> picks = new LinkedHashSet<>();

}
