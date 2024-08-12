package com.ssafy.a505.domain.repository;

import com.ssafy.a505.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberName(String memberName);

    Optional<Member> findByMemberId(long memberId);

    Optional<Member> findByMemberEmail(String memberEmail);

}
