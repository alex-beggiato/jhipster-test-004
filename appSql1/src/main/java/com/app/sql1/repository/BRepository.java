package com.app.sql1.repository;

import com.app.sql1.domain.B;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the B entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BRepository extends JpaRepository<B, Long> {}
