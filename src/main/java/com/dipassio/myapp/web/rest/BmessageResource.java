package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Bmessage;
import com.dipassio.myapp.repository.BmessageRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Bmessage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BmessageResource {

    private final Logger log = LoggerFactory.getLogger(BmessageResource.class);

    private static final String ENTITY_NAME = "bmessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BmessageRepository bmessageRepository;

    public BmessageResource(BmessageRepository bmessageRepository) {
        this.bmessageRepository = bmessageRepository;
    }

    /**
     * {@code POST  /bmessages} : Create a new bmessage.
     *
     * @param bmessage the bmessage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bmessage, or with status {@code 400 (Bad Request)} if the bmessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bmessages")
    public ResponseEntity<Bmessage> createBmessage(@Valid @RequestBody Bmessage bmessage) throws URISyntaxException {
        log.debug("REST request to save Bmessage : {}", bmessage);
        if (bmessage.getId() != null) {
            throw new BadRequestAlertException("A new bmessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bmessage result = bmessageRepository.save(bmessage);
        return ResponseEntity.created(new URI("/api/bmessages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bmessages} : Updates an existing bmessage.
     *
     * @param bmessage the bmessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bmessage,
     * or with status {@code 400 (Bad Request)} if the bmessage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bmessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bmessages")
    public ResponseEntity<Bmessage> updateBmessage(@Valid @RequestBody Bmessage bmessage) throws URISyntaxException {
        log.debug("REST request to update Bmessage : {}", bmessage);
        if (bmessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Bmessage result = bmessageRepository.save(bmessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bmessage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bmessages} : get all the bmessages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bmessages in body.
     */
    @GetMapping("/bmessages")
    public ResponseEntity<List<Bmessage>> getAllBmessages(Pageable pageable) {
        log.debug("REST request to get a page of Bmessages");
        Page<Bmessage> page = bmessageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bmessages/:id} : get the "id" bmessage.
     *
     * @param id the id of the bmessage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bmessage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bmessages/{id}")
    public ResponseEntity<Bmessage> getBmessage(@PathVariable Long id) {
        log.debug("REST request to get Bmessage : {}", id);
        Optional<Bmessage> bmessage = bmessageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bmessage);
    }

    /**
     * {@code DELETE  /bmessages/:id} : delete the "id" bmessage.
     *
     * @param id the id of the bmessage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bmessages/{id}")
    public ResponseEntity<Void> deleteBmessage(@PathVariable Long id) {
        log.debug("REST request to delete Bmessage : {}", id);
        bmessageRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
