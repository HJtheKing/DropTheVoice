package com.dtv.domain.repository;

import com.dtv.domain.entity.Heart;
import com.dtv.domain.entity.Member;
import com.dtv.domain.entity.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByVoiceAndMember(Voice voice, Member member);
}
