package com.dipassio.myapp.repository;

import com.dipassio.myapp.domain.Blockuser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Blockuser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlockuserRepository extends JpaRepository<Blockuser, Long> {
}
