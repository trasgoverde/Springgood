package com.dipassio.myapp.repository;

import com.dipassio.myapp.domain.Vquestion;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Vquestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VquestionRepository extends JpaRepository<Vquestion, Long> {
}
