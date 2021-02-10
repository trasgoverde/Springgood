package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Proposal;
import com.dipassio.myapp.repository.ProposalRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Proposal}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProposalResource {

    private final Logger log = LoggerFactory.getLogger(ProposalResource.class);

    private static final String ENTITY_NAME = "proposal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProposalRepository proposalRepository;

    public ProposalResource(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    /**
     * {@code POST  /proposals} : Create a new proposal.
     *
     * @param proposal the proposal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proposal, or with status {@code 400 (Bad Request)} if the proposal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proposals")
    public ResponseEntity<Proposal> createProposal(@Valid @RequestBody Proposal proposal) throws URISyntaxException {
        log.debug("REST request to save Proposal : {}", proposal);
        if (proposal.getId() != null) {
            throw new BadRequestAlertException("A new proposal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Proposal result = proposalRepository.save(proposal);
        return ResponseEntity.created(new URI("/api/proposals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proposals} : Updates an existing proposal.
     *
     * @param proposal the proposal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposal,
     * or with status {@code 400 (Bad Request)} if the proposal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proposal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proposals")
    public ResponseEntity<Proposal> updateProposal(@Valid @RequestBody Proposal proposal) throws URISyntaxException {
        log.debug("REST request to update Proposal : {}", proposal);
        if (proposal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Proposal result = proposalRepository.save(proposal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proposal.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /proposals} : get all the proposals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proposals in body.
     */
    @GetMapping("/proposals")
    public ResponseEntity<List<Proposal>> getAllProposals(Pageable pageable) {
        log.debug("REST request to get a page of Proposals");
        Page<Proposal> page = proposalRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proposals/:id} : get the "id" proposal.
     *
     * @param id the id of the proposal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proposal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proposals/{id}")
    public ResponseEntity<Proposal> getProposal(@PathVariable Long id) {
        log.debug("REST request to get Proposal : {}", id);
        Optional<Proposal> proposal = proposalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(proposal);
    }

    /**
     * {@code DELETE  /proposals/:id} : delete the "id" proposal.
     *
     * @param id the id of the proposal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proposals/{id}")
    public ResponseEntity<Void> deleteProposal(@PathVariable Long id) {
        log.debug("REST request to delete Proposal : {}", id);
        proposalRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
