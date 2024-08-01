package com.ssafy.a505.domain.repository;

import com.ssafy.a505.domain.entity.Voice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
    List<Voice> findByTitleContaining(String userNam, Pageable pageable);
    List<Voice> findALlByTitle(String title, Pageable pageable);
}
