package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Cactivity;
import com.dipassio.myapp.repository.CactivityRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Cactivity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CactivityResource {

    private final Logger log = LoggerFactory.getLogger(CactivityResource.class);

    private static final String ENTITY_NAME = "cactivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CactivityRepository cactivityRepository;

    public CactivityResource(CactivityRepository cactivityRepository) {
        this.cactivityRepository = cactivityRepository;
    }

    /**
     * {@code POST  /cactivities} : Create a new cactivity.
     *
     * @param cactivity the cactivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cactivity, or with status {@code 400 (Bad Request)} if the cactivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cactivities")
    public ResponseEntity<Cactivity> createCactivity(@Valid @RequestBody Cactivity cactivity) throws URISyntaxException {
        log.debug("REST request to save Cactivity : {}", cactivity);
        if (cactivity.getId() != null) {
            throw new BadRequestAlertException("A new cactivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cactivity result = cactivityRepository.save(cactivity);
        return ResponseEntity.created(new URI("/api/cactivities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cactivities} : Updates an existing cactivity.
     *
     * @param cactivity the cactivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cactivity,
     * or with status {@code 400 (Bad Request)} if the cactivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cactivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cactivities")
    public ResponseEntity<Cactivity> updateCactivity(@Valid @RequestBody Cactivity cactivity) throws URISyntaxException {
        log.debug("REST request to update Cactivity : {}", cactivity);
        if (cactivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cactivity result = cactivityRepository.save(cactivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cactivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cactivities} : get all the cactivities.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cactivities in body.
     */
    @GetMapping("/cactivities")
    public ResponseEntity<List<Cactivity>> getAllCactivities(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Cactivities");
        Page<Cactivity> page;
        if (eagerload) {
            page = cactivityRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = cactivityRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cactivities/:id} : get the "id" cactivity.
     *
     * @param id the id of the cactivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cactivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cactivities/{id}")
    public ResponseEntity<Cactivity> getCactivity(@PathVariable Long id) {
        log.debug("REST request to get Cactivity : {}", id);
        Optional<Cactivity> cactivity = cactivityRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(cactivity);
    }

    /**
     * {@code DELETE  /cactivities/:id} : delete the "id" cactivity.
     *
     * @param id the id of the cactivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cactivities/{id}")
    public ResponseEntity<Void> deleteCactivity(@PathVariable Long id) {
        log.debug("REST request to delete Cactivity : {}", id);
        cactivityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
