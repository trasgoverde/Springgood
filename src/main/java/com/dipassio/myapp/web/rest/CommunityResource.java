package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Community;
import com.dipassio.myapp.repository.CommunityRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Community}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommunityResource {

    private final Logger log = LoggerFactory.getLogger(CommunityResource.class);

    private static final String ENTITY_NAME = "community";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityRepository communityRepository;

    public CommunityResource(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    /**
     * {@code POST  /communities} : Create a new community.
     *
     * @param community the community to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new community, or with status {@code 400 (Bad Request)} if the community has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/communities")
    public ResponseEntity<Community> createCommunity(@Valid @RequestBody Community community) throws URISyntaxException {
        log.debug("REST request to save Community : {}", community);
        if (community.getId() != null) {
            throw new BadRequestAlertException("A new community cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Community result = communityRepository.save(community);
        return ResponseEntity.created(new URI("/api/communities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /communities} : Updates an existing community.
     *
     * @param community the community to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated community,
     * or with status {@code 400 (Bad Request)} if the community is not valid,
     * or with status {@code 500 (Internal Server Error)} if the community couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/communities")
    public ResponseEntity<Community> updateCommunity(@Valid @RequestBody Community community) throws URISyntaxException {
        log.debug("REST request to update Community : {}", community);
        if (community.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Community result = communityRepository.save(community);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, community.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /communities} : get all the communities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communities in body.
     */
    @GetMapping("/communities")
    public ResponseEntity<List<Community>> getAllCommunities(Pageable pageable) {
        log.debug("REST request to get a page of Communities");
        Page<Community> page = communityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /communities/:id} : get the "id" community.
     *
     * @param id the id of the community to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the community, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/communities/{id}")
    public ResponseEntity<Community> getCommunity(@PathVariable Long id) {
        log.debug("REST request to get Community : {}", id);
        Optional<Community> community = communityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(community);
    }

    /**
     * {@code DELETE  /communities/:id} : delete the "id" community.
     *
     * @param id the id of the community to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/communities/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id) {
        log.debug("REST request to delete Community : {}", id);
        communityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
