package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Appprofile;
import com.dipassio.myapp.repository.AppprofileRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Appprofile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AppprofileResource {

    private final Logger log = LoggerFactory.getLogger(AppprofileResource.class);

    private static final String ENTITY_NAME = "appprofile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppprofileRepository appprofileRepository;

    public AppprofileResource(AppprofileRepository appprofileRepository) {
        this.appprofileRepository = appprofileRepository;
    }

    /**
     * {@code POST  /appprofiles} : Create a new appprofile.
     *
     * @param appprofile the appprofile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appprofile, or with status {@code 400 (Bad Request)} if the appprofile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appprofiles")
    public ResponseEntity<Appprofile> createAppprofile(@Valid @RequestBody Appprofile appprofile) throws URISyntaxException {
        log.debug("REST request to save Appprofile : {}", appprofile);
        if (appprofile.getId() != null) {
            throw new BadRequestAlertException("A new appprofile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Appprofile result = appprofileRepository.save(appprofile);
        return ResponseEntity.created(new URI("/api/appprofiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appprofiles} : Updates an existing appprofile.
     *
     * @param appprofile the appprofile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appprofile,
     * or with status {@code 400 (Bad Request)} if the appprofile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appprofile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appprofiles")
    public ResponseEntity<Appprofile> updateAppprofile(@Valid @RequestBody Appprofile appprofile) throws URISyntaxException {
        log.debug("REST request to update Appprofile : {}", appprofile);
        if (appprofile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Appprofile result = appprofileRepository.save(appprofile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appprofile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /appprofiles} : get all the appprofiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appprofiles in body.
     */
    @GetMapping("/appprofiles")
    public ResponseEntity<List<Appprofile>> getAllAppprofiles(Pageable pageable) {
        log.debug("REST request to get a page of Appprofiles");
        Page<Appprofile> page = appprofileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appprofiles/:id} : get the "id" appprofile.
     *
     * @param id the id of the appprofile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appprofile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appprofiles/{id}")
    public ResponseEntity<Appprofile> getAppprofile(@PathVariable Long id) {
        log.debug("REST request to get Appprofile : {}", id);
        Optional<Appprofile> appprofile = appprofileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(appprofile);
    }

    /**
     * {@code DELETE  /appprofiles/:id} : delete the "id" appprofile.
     *
     * @param id the id of the appprofile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appprofiles/{id}")
    public ResponseEntity<Void> deleteAppprofile(@PathVariable Long id) {
        log.debug("REST request to delete Appprofile : {}", id);
        appprofileRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
