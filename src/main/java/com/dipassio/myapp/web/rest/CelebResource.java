package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Celeb;
import com.dipassio.myapp.repository.CelebRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Celeb}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CelebResource {

    private final Logger log = LoggerFactory.getLogger(CelebResource.class);

    private static final String ENTITY_NAME = "celeb";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CelebRepository celebRepository;

    public CelebResource(CelebRepository celebRepository) {
        this.celebRepository = celebRepository;
    }

    /**
     * {@code POST  /celebs} : Create a new celeb.
     *
     * @param celeb the celeb to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new celeb, or with status {@code 400 (Bad Request)} if the celeb has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/celebs")
    public ResponseEntity<Celeb> createCeleb(@Valid @RequestBody Celeb celeb) throws URISyntaxException {
        log.debug("REST request to save Celeb : {}", celeb);
        if (celeb.getId() != null) {
            throw new BadRequestAlertException("A new celeb cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Celeb result = celebRepository.save(celeb);
        return ResponseEntity.created(new URI("/api/celebs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /celebs} : Updates an existing celeb.
     *
     * @param celeb the celeb to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated celeb,
     * or with status {@code 400 (Bad Request)} if the celeb is not valid,
     * or with status {@code 500 (Internal Server Error)} if the celeb couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/celebs")
    public ResponseEntity<Celeb> updateCeleb(@Valid @RequestBody Celeb celeb) throws URISyntaxException {
        log.debug("REST request to update Celeb : {}", celeb);
        if (celeb.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Celeb result = celebRepository.save(celeb);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, celeb.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /celebs} : get all the celebs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of celebs in body.
     */
    @GetMapping("/celebs")
    public ResponseEntity<List<Celeb>> getAllCelebs(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Celebs");
        Page<Celeb> page;
        if (eagerload) {
            page = celebRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = celebRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /celebs/:id} : get the "id" celeb.
     *
     * @param id the id of the celeb to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the celeb, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/celebs/{id}")
    public ResponseEntity<Celeb> getCeleb(@PathVariable Long id) {
        log.debug("REST request to get Celeb : {}", id);
        Optional<Celeb> celeb = celebRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(celeb);
    }

    /**
     * {@code DELETE  /celebs/:id} : delete the "id" celeb.
     *
     * @param id the id of the celeb to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/celebs/{id}")
    public ResponseEntity<Void> deleteCeleb(@PathVariable Long id) {
        log.debug("REST request to delete Celeb : {}", id);
        celebRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
