package com.dipassio.myapp.repository;

import com.dipassio.myapp.domain.ProposalVote;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProposalVote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProposalVoteRepository extends JpaRepository<ProposalVote, Long> {
}
