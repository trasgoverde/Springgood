package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Cceleb;
import com.dipassio.myapp.repository.CcelebRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Cceleb}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CcelebResource {

    private final Logger log = LoggerFactory.getLogger(CcelebResource.class);

    private static final String ENTITY_NAME = "cceleb";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CcelebRepository ccelebRepository;

    public CcelebResource(CcelebRepository ccelebRepository) {
        this.ccelebRepository = ccelebRepository;
    }

    /**
     * {@code POST  /ccelebs} : Create a new cceleb.
     *
     * @param cceleb the cceleb to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cceleb, or with status {@code 400 (Bad Request)} if the cceleb has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ccelebs")
    public ResponseEntity<Cceleb> createCceleb(@Valid @RequestBody Cceleb cceleb) throws URISyntaxException {
        log.debug("REST request to save Cceleb : {}", cceleb);
        if (cceleb.getId() != null) {
            throw new BadRequestAlertException("A new cceleb cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cceleb result = ccelebRepository.save(cceleb);
        return ResponseEntity.created(new URI("/api/ccelebs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ccelebs} : Updates an existing cceleb.
     *
     * @param cceleb the cceleb to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cceleb,
     * or with status {@code 400 (Bad Request)} if the cceleb is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cceleb couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ccelebs")
    public ResponseEntity<Cceleb> updateCceleb(@Valid @RequestBody Cceleb cceleb) throws URISyntaxException {
        log.debug("REST request to update Cceleb : {}", cceleb);
        if (cceleb.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cceleb result = ccelebRepository.save(cceleb);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cceleb.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ccelebs} : get all the ccelebs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ccelebs in body.
     */
    @GetMapping("/ccelebs")
    public ResponseEntity<List<Cceleb>> getAllCcelebs(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Ccelebs");
        Page<Cceleb> page;
        if (eagerload) {
            page = ccelebRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = ccelebRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ccelebs/:id} : get the "id" cceleb.
     *
     * @param id the id of the cceleb to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cceleb, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ccelebs/{id}")
    public ResponseEntity<Cceleb> getCceleb(@PathVariable Long id) {
        log.debug("REST request to get Cceleb : {}", id);
        Optional<Cceleb> cceleb = ccelebRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(cceleb);
    }

    /**
     * {@code DELETE  /ccelebs/:id} : delete the "id" cceleb.
     *
     * @param id the id of the cceleb to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ccelebs/{id}")
    public ResponseEntity<Void> deleteCceleb(@PathVariable Long id) {
        log.debug("REST request to delete Cceleb : {}", id);
        ccelebRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
