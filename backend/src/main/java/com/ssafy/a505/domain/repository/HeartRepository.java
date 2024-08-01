package com.ssafy.a505.domain.repository;

import com.ssafy.a505.domain.entity.Heart;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    List<Heart> findByMember(Member member);

    List<Heart> findByVoice(Voice voice);
}
