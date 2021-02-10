package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Appphoto;
import com.dipassio.myapp.repository.AppphotoRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Appphoto}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AppphotoResource {

    private final Logger log = LoggerFactory.getLogger(AppphotoResource.class);

    private static final String ENTITY_NAME = "appphoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppphotoRepository appphotoRepository;

    public AppphotoResource(AppphotoRepository appphotoRepository) {
        this.appphotoRepository = appphotoRepository;
    }

    /**
     * {@code POST  /appphotos} : Create a new appphoto.
     *
     * @param appphoto the appphoto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appphoto, or with status {@code 400 (Bad Request)} if the appphoto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appphotos")
    public ResponseEntity<Appphoto> createAppphoto(@Valid @RequestBody Appphoto appphoto) throws URISyntaxException {
        log.debug("REST request to save Appphoto : {}", appphoto);
        if (appphoto.getId() != null) {
            throw new BadRequestAlertException("A new appphoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Appphoto result = appphotoRepository.save(appphoto);
        return ResponseEntity.created(new URI("/api/appphotos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appphotos} : Updates an existing appphoto.
     *
     * @param appphoto the appphoto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appphoto,
     * or with status {@code 400 (Bad Request)} if the appphoto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appphoto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appphotos")
    public ResponseEntity<Appphoto> updateAppphoto(@Valid @RequestBody Appphoto appphoto) throws URISyntaxException {
        log.debug("REST request to update Appphoto : {}", appphoto);
        if (appphoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Appphoto result = appphotoRepository.save(appphoto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appphoto.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /appphotos} : get all the appphotos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appphotos in body.
     */
    @GetMapping("/appphotos")
    public ResponseEntity<List<Appphoto>> getAllAppphotos(Pageable pageable) {
        log.debug("REST request to get a page of Appphotos");
        Page<Appphoto> page = appphotoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appphotos/:id} : get the "id" appphoto.
     *
     * @param id the id of the appphoto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appphoto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appphotos/{id}")
    public ResponseEntity<Appphoto> getAppphoto(@PathVariable Long id) {
        log.debug("REST request to get Appphoto : {}", id);
        Optional<Appphoto> appphoto = appphotoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(appphoto);
    }

    /**
     * {@code DELETE  /appphotos/:id} : delete the "id" appphoto.
     *
     * @param id the id of the appphoto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appphotos/{id}")
    public ResponseEntity<Void> deleteAppphoto(@PathVariable Long id) {
        log.debug("REST request to delete Appphoto : {}", id);
        appphotoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
