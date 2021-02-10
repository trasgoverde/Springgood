package com.dipassio.myapp.repository;

import com.dipassio.myapp.domain.Appprofile;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Appprofile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppprofileRepository extends JpaRepository<Appprofile, Long> {
}
