package com.dipassio.myapp.repository;

import com.dipassio.myapp.domain.Vthumb;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Vthumb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VthumbRepository extends JpaRepository<Vthumb, Long> {
}
