package com.dtv.domain.repository;

import com.dtv.domain.entity.Spread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpreadRepository extends JpaRepository<Spread, Long> {
}
