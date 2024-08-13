package com.ssafy.a505.domain.repository;

import com.ssafy.a505.domain.entity.Spread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpreadRepository extends JpaRepository<Spread, Long> {
}
