package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Follow;
import com.dipassio.myapp.repository.FollowRepository;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.dipassio.myapp.domain.Follow}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FollowResource {

    private final Logger log = LoggerFactory.getLogger(FollowResource.class);

    private static final String ENTITY_NAME = "follow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FollowRepository followRepository;

    public FollowResource(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    /**
     * {@code POST  /follows} : Create a new follow.
     *
     * @param follow the follow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new follow, or with status {@code 400 (Bad Request)} if the follow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/follows")
    public ResponseEntity<Follow> createFollow(@RequestBody Follow follow) throws URISyntaxException {
        log.debug("REST request to save Follow : {}", follow);
        if (follow.getId() != null) {
            throw new BadRequestAlertException("A new follow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Follow result = followRepository.save(follow);
        return ResponseEntity.created(new URI("/api/follows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /follows} : Updates an existing follow.
     *
     * @param follow the follow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated follow,
     * or with status {@code 400 (Bad Request)} if the follow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the follow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/follows")
    public ResponseEntity<Follow> updateFollow(@RequestBody Follow follow) throws URISyntaxException {
        log.debug("REST request to update Follow : {}", follow);
        if (follow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Follow result = followRepository.save(follow);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, follow.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /follows} : get all the follows.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of follows in body.
     */
    @GetMapping("/follows")
    public ResponseEntity<List<Follow>> getAllFollows(Pageable pageable) {
        log.debug("REST request to get a page of Follows");
        Page<Follow> page = followRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /follows/:id} : get the "id" follow.
     *
     * @param id the id of the follow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the follow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/follows/{id}")
    public ResponseEntity<Follow> getFollow(@PathVariable Long id) {
        log.debug("REST request to get Follow : {}", id);
        Optional<Follow> follow = followRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(follow);
    }

    /**
     * {@code DELETE  /follows/:id} : delete the "id" follow.
     *
     * @param id the id of the follow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/follows/{id}")
    public ResponseEntity<Void> deleteFollow(@PathVariable Long id) {
        log.debug("REST request to delete Follow : {}", id);
        followRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
