package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Cmessage;
import com.dipassio.myapp.repository.CmessageRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Cmessage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CmessageResource {

    private final Logger log = LoggerFactory.getLogger(CmessageResource.class);

    private static final String ENTITY_NAME = "cmessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CmessageRepository cmessageRepository;

    public CmessageResource(CmessageRepository cmessageRepository) {
        this.cmessageRepository = cmessageRepository;
    }

    /**
     * {@code POST  /cmessages} : Create a new cmessage.
     *
     * @param cmessage the cmessage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cmessage, or with status {@code 400 (Bad Request)} if the cmessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cmessages")
    public ResponseEntity<Cmessage> createCmessage(@Valid @RequestBody Cmessage cmessage) throws URISyntaxException {
        log.debug("REST request to save Cmessage : {}", cmessage);
        if (cmessage.getId() != null) {
            throw new BadRequestAlertException("A new cmessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cmessage result = cmessageRepository.save(cmessage);
        return ResponseEntity.created(new URI("/api/cmessages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cmessages} : Updates an existing cmessage.
     *
     * @param cmessage the cmessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cmessage,
     * or with status {@code 400 (Bad Request)} if the cmessage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cmessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cmessages")
    public ResponseEntity<Cmessage> updateCmessage(@Valid @RequestBody Cmessage cmessage) throws URISyntaxException {
        log.debug("REST request to update Cmessage : {}", cmessage);
        if (cmessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cmessage result = cmessageRepository.save(cmessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmessage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cmessages} : get all the cmessages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cmessages in body.
     */
    @GetMapping("/cmessages")
    public ResponseEntity<List<Cmessage>> getAllCmessages(Pageable pageable) {
        log.debug("REST request to get a page of Cmessages");
        Page<Cmessage> page = cmessageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cmessages/:id} : get the "id" cmessage.
     *
     * @param id the id of the cmessage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cmessage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cmessages/{id}")
    public ResponseEntity<Cmessage> getCmessage(@PathVariable Long id) {
        log.debug("REST request to get Cmessage : {}", id);
        Optional<Cmessage> cmessage = cmessageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cmessage);
    }

    /**
     * {@code DELETE  /cmessages/:id} : delete the "id" cmessage.
     *
     * @param id the id of the cmessage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cmessages/{id}")
    public ResponseEntity<Void> deleteCmessage(@PathVariable Long id) {
        log.debug("REST request to delete Cmessage : {}", id);
        cmessageRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
