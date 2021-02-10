package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Vquestion;
import com.dipassio.myapp.repository.VquestionRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Vquestion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VquestionResource {

    private final Logger log = LoggerFactory.getLogger(VquestionResource.class);

    private static final String ENTITY_NAME = "vquestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VquestionRepository vquestionRepository;

    public VquestionResource(VquestionRepository vquestionRepository) {
        this.vquestionRepository = vquestionRepository;
    }

    /**
     * {@code POST  /vquestions} : Create a new vquestion.
     *
     * @param vquestion the vquestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vquestion, or with status {@code 400 (Bad Request)} if the vquestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vquestions")
    public ResponseEntity<Vquestion> createVquestion(@Valid @RequestBody Vquestion vquestion) throws URISyntaxException {
        log.debug("REST request to save Vquestion : {}", vquestion);
        if (vquestion.getId() != null) {
            throw new BadRequestAlertException("A new vquestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vquestion result = vquestionRepository.save(vquestion);
        return ResponseEntity.created(new URI("/api/vquestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vquestions} : Updates an existing vquestion.
     *
     * @param vquestion the vquestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vquestion,
     * or with status {@code 400 (Bad Request)} if the vquestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vquestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vquestions")
    public ResponseEntity<Vquestion> updateVquestion(@Valid @RequestBody Vquestion vquestion) throws URISyntaxException {
        log.debug("REST request to update Vquestion : {}", vquestion);
        if (vquestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Vquestion result = vquestionRepository.save(vquestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vquestion.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vquestions} : get all the vquestions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vquestions in body.
     */
    @GetMapping("/vquestions")
    public ResponseEntity<List<Vquestion>> getAllVquestions(Pageable pageable) {
        log.debug("REST request to get a page of Vquestions");
        Page<Vquestion> page = vquestionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vquestions/:id} : get the "id" vquestion.
     *
     * @param id the id of the vquestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vquestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vquestions/{id}")
    public ResponseEntity<Vquestion> getVquestion(@PathVariable Long id) {
        log.debug("REST request to get Vquestion : {}", id);
        Optional<Vquestion> vquestion = vquestionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vquestion);
    }

    /**
     * {@code DELETE  /vquestions/:id} : delete the "id" vquestion.
     *
     * @param id the id of the vquestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vquestions/{id}")
    public ResponseEntity<Void> deleteVquestion(@PathVariable Long id) {
        log.debug("REST request to delete Vquestion : {}", id);
        vquestionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
