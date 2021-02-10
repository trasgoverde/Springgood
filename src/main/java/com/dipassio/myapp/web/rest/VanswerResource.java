package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Vanswer;
import com.dipassio.myapp.repository.VanswerRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Vanswer}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VanswerResource {

    private final Logger log = LoggerFactory.getLogger(VanswerResource.class);

    private static final String ENTITY_NAME = "vanswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VanswerRepository vanswerRepository;

    public VanswerResource(VanswerRepository vanswerRepository) {
        this.vanswerRepository = vanswerRepository;
    }

    /**
     * {@code POST  /vanswers} : Create a new vanswer.
     *
     * @param vanswer the vanswer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vanswer, or with status {@code 400 (Bad Request)} if the vanswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vanswers")
    public ResponseEntity<Vanswer> createVanswer(@Valid @RequestBody Vanswer vanswer) throws URISyntaxException {
        log.debug("REST request to save Vanswer : {}", vanswer);
        if (vanswer.getId() != null) {
            throw new BadRequestAlertException("A new vanswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vanswer result = vanswerRepository.save(vanswer);
        return ResponseEntity.created(new URI("/api/vanswers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vanswers} : Updates an existing vanswer.
     *
     * @param vanswer the vanswer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vanswer,
     * or with status {@code 400 (Bad Request)} if the vanswer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vanswer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vanswers")
    public ResponseEntity<Vanswer> updateVanswer(@Valid @RequestBody Vanswer vanswer) throws URISyntaxException {
        log.debug("REST request to update Vanswer : {}", vanswer);
        if (vanswer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Vanswer result = vanswerRepository.save(vanswer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vanswer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vanswers} : get all the vanswers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vanswers in body.
     */
    @GetMapping("/vanswers")
    public ResponseEntity<List<Vanswer>> getAllVanswers(Pageable pageable) {
        log.debug("REST request to get a page of Vanswers");
        Page<Vanswer> page = vanswerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vanswers/:id} : get the "id" vanswer.
     *
     * @param id the id of the vanswer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vanswer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vanswers/{id}")
    public ResponseEntity<Vanswer> getVanswer(@PathVariable Long id) {
        log.debug("REST request to get Vanswer : {}", id);
        Optional<Vanswer> vanswer = vanswerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vanswer);
    }

    /**
     * {@code DELETE  /vanswers/:id} : delete the "id" vanswer.
     *
     * @param id the id of the vanswer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vanswers/{id}")
    public ResponseEntity<Void> deleteVanswer(@PathVariable Long id) {
        log.debug("REST request to delete Vanswer : {}", id);
        vanswerRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
