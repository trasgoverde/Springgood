package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.ProposalVote;
import com.dipassio.myapp.repository.ProposalVoteRepository;
import com.dipassio.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.dipassio.myapp.domain.ProposalVote}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProposalVoteResource {

    private final Logger log = LoggerFactory.getLogger(ProposalVoteResource.class);

    private static final String ENTITY_NAME = "proposalVote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProposalVoteRepository proposalVoteRepository;

    public ProposalVoteResource(ProposalVoteRepository proposalVoteRepository) {
        this.proposalVoteRepository = proposalVoteRepository;
    }

    /**
     * {@code POST  /proposal-votes} : Create a new proposalVote.
     *
     * @param proposalVote the proposalVote to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proposalVote, or with status {@code 400 (Bad Request)} if the proposalVote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proposal-votes")
    public ResponseEntity<ProposalVote> createProposalVote(@Valid @RequestBody ProposalVote proposalVote) throws URISyntaxException {
        log.debug("REST request to save ProposalVote : {}", proposalVote);
        if (proposalVote.getId() != null) {
            throw new BadRequestAlertException("A new proposalVote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProposalVote result = proposalVoteRepository.save(proposalVote);
        return ResponseEntity.created(new URI("/api/proposal-votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proposal-votes} : Updates an existing proposalVote.
     *
     * @param proposalVote the proposalVote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposalVote,
     * or with status {@code 400 (Bad Request)} if the proposalVote is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proposalVote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proposal-votes")
    public ResponseEntity<ProposalVote> updateProposalVote(@Valid @RequestBody ProposalVote proposalVote) throws URISyntaxException {
        log.debug("REST request to update ProposalVote : {}", proposalVote);
        if (proposalVote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProposalVote result = proposalVoteRepository.save(proposalVote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proposalVote.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /proposal-votes} : get all the proposalVotes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proposalVotes in body.
     */
    @GetMapping("/proposal-votes")
    public ResponseEntity<List<ProposalVote>> getAllProposalVotes(Pageable pageable) {
        log.debug("REST request to get a page of ProposalVotes");
        Page<ProposalVote> page = proposalVoteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proposal-votes/:id} : get the "id" proposalVote.
     *
     * @param id the id of the proposalVote to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proposalVote, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proposal-votes/{id}")
    public ResponseEntity<ProposalVote> getProposalVote(@PathVariable Long id) {
        log.debug("REST request to get ProposalVote : {}", id);
        Optional<ProposalVote> proposalVote = proposalVoteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(proposalVote);
    }

    /**
     * {@code DELETE  /proposal-votes/:id} : delete the "id" proposalVote.
     *
     * @param id the id of the proposalVote to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proposal-votes/{id}")
    public ResponseEntity<Void> deleteProposalVote(@PathVariable Long id) {
        log.debug("REST request to delete ProposalVote : {}", id);
        proposalVoteRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
