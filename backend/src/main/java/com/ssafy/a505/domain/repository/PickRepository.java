package com.ssafy.a505.domain.repository;

import com.ssafy.a505.domain.entity.Heart;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Pick;
import com.ssafy.a505.domain.entity.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PickRepository extends JpaRepository<Pick, Long> {
    Optional<Pick> findByVoiceAndMember(Voice voice, Member member);
}
