package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Cinterest;
import com.dipassio.myapp.repository.CinterestRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Cinterest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CinterestResource {

    private final Logger log = LoggerFactory.getLogger(CinterestResource.class);

    private static final String ENTITY_NAME = "cinterest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CinterestRepository cinterestRepository;

    public CinterestResource(CinterestRepository cinterestRepository) {
        this.cinterestRepository = cinterestRepository;
    }

    /**
     * {@code POST  /cinterests} : Create a new cinterest.
     *
     * @param cinterest the cinterest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cinterest, or with status {@code 400 (Bad Request)} if the cinterest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cinterests")
    public ResponseEntity<Cinterest> createCinterest(@Valid @RequestBody Cinterest cinterest) throws URISyntaxException {
        log.debug("REST request to save Cinterest : {}", cinterest);
        if (cinterest.getId() != null) {
            throw new BadRequestAlertException("A new cinterest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cinterest result = cinterestRepository.save(cinterest);
        return ResponseEntity.created(new URI("/api/cinterests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cinterests} : Updates an existing cinterest.
     *
     * @param cinterest the cinterest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cinterest,
     * or with status {@code 400 (Bad Request)} if the cinterest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cinterest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cinterests")
    public ResponseEntity<Cinterest> updateCinterest(@Valid @RequestBody Cinterest cinterest) throws URISyntaxException {
        log.debug("REST request to update Cinterest : {}", cinterest);
        if (cinterest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cinterest result = cinterestRepository.save(cinterest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cinterest.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cinterests} : get all the cinterests.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cinterests in body.
     */
    @GetMapping("/cinterests")
    public ResponseEntity<List<Cinterest>> getAllCinterests(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Cinterests");
        Page<Cinterest> page;
        if (eagerload) {
            page = cinterestRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = cinterestRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cinterests/:id} : get the "id" cinterest.
     *
     * @param id the id of the cinterest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cinterest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cinterests/{id}")
    public ResponseEntity<Cinterest> getCinterest(@PathVariable Long id) {
        log.debug("REST request to get Cinterest : {}", id);
        Optional<Cinterest> cinterest = cinterestRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(cinterest);
    }

    /**
     * {@code DELETE  /cinterests/:id} : delete the "id" cinterest.
     *
     * @param id the id of the cinterest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cinterests/{id}")
    public ResponseEntity<Void> deleteCinterest(@PathVariable Long id) {
        log.debug("REST request to delete Cinterest : {}", id);
        cinterestRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
