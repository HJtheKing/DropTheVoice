package com.dtv.domain.repository;

import com.dtv.domain.entity.Member;
import com.dtv.domain.entity.Pick;
import com.dtv.domain.entity.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PickRepository extends JpaRepository<Pick, Long> {
    Optional<Pick> findByVoiceAndMember(Voice voice, Member member);
}
