package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.request.MemberRequestDTO;
import com.ssafy.a505.domain.dto.response.MemberResponseDTO;
import com.ssafy.a505.domain.entity.Member;

public interface MemberService {

    int findRemainChangeCount(Long memberId);

    boolean isUserNameDuplicate(String userName);

    MemberResponseDTO registerMember(MemberRequestDTO memberRequestDTO);
}
