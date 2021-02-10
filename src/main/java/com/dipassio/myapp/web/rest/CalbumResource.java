package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Calbum;
import com.dipassio.myapp.repository.CalbumRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Calbum}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CalbumResource {

    private final Logger log = LoggerFactory.getLogger(CalbumResource.class);

    private static final String ENTITY_NAME = "calbum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalbumRepository calbumRepository;

    public CalbumResource(CalbumRepository calbumRepository) {
        this.calbumRepository = calbumRepository;
    }

    /**
     * {@code POST  /calbums} : Create a new calbum.
     *
     * @param calbum the calbum to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calbum, or with status {@code 400 (Bad Request)} if the calbum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calbums")
    public ResponseEntity<Calbum> createCalbum(@Valid @RequestBody Calbum calbum) throws URISyntaxException {
        log.debug("REST request to save Calbum : {}", calbum);
        if (calbum.getId() != null) {
            throw new BadRequestAlertException("A new calbum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Calbum result = calbumRepository.save(calbum);
        return ResponseEntity.created(new URI("/api/calbums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calbums} : Updates an existing calbum.
     *
     * @param calbum the calbum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calbum,
     * or with status {@code 400 (Bad Request)} if the calbum is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calbum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calbums")
    public ResponseEntity<Calbum> updateCalbum(@Valid @RequestBody Calbum calbum) throws URISyntaxException {
        log.debug("REST request to update Calbum : {}", calbum);
        if (calbum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Calbum result = calbumRepository.save(calbum);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calbum.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /calbums} : get all the calbums.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calbums in body.
     */
    @GetMapping("/calbums")
    public ResponseEntity<List<Calbum>> getAllCalbums(Pageable pageable) {
        log.debug("REST request to get a page of Calbums");
        Page<Calbum> page = calbumRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calbums/:id} : get the "id" calbum.
     *
     * @param id the id of the calbum to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calbum, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calbums/{id}")
    public ResponseEntity<Calbum> getCalbum(@PathVariable Long id) {
        log.debug("REST request to get Calbum : {}", id);
        Optional<Calbum> calbum = calbumRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(calbum);
    }

    /**
     * {@code DELETE  /calbums/:id} : delete the "id" calbum.
     *
     * @param id the id of the calbum to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calbums/{id}")
    public ResponseEntity<Void> deleteCalbum(@PathVariable Long id) {
        log.debug("REST request to delete Calbum : {}", id);
        calbumRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
