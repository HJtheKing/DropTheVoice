package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{
    @Override
    public boolean login(Member member) {
        return false;
    }

    @Override
    public boolean signup(Member member) {
        if(member.getMemberName().equals("1234") && member.getMemberPassword().equals("1234")){
            return true;
        }
        return false;
    }

    @Override
    public boolean removeUser(String memberId) {
        return false;
    }

    @Override
    public boolean setUserImg(String memberId, String newImageName) {
        return false;
    }
}
