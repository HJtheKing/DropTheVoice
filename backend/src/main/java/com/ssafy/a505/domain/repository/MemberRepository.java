package com.ssafy.a505.domain.repository;

import com.ssafy.a505.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
