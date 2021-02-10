package com.dipassio.myapp.repository;

import com.dipassio.myapp.domain.Bmessage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Bmessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BmessageRepository extends JpaRepository<Bmessage, Long> {
}
