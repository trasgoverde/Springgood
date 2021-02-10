package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Vthumb;
import com.dipassio.myapp.repository.VthumbRepository;
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
 * REST controller for managing {@link com.dipassio.myapp.domain.Vthumb}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VthumbResource {

    private final Logger log = LoggerFactory.getLogger(VthumbResource.class);

    private static final String ENTITY_NAME = "vthumb";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VthumbRepository vthumbRepository;

    public VthumbResource(VthumbRepository vthumbRepository) {
        this.vthumbRepository = vthumbRepository;
    }

    /**
     * {@code POST  /vthumbs} : Create a new vthumb.
     *
     * @param vthumb the vthumb to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vthumb, or with status {@code 400 (Bad Request)} if the vthumb has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vthumbs")
    public ResponseEntity<Vthumb> createVthumb(@Valid @RequestBody Vthumb vthumb) throws URISyntaxException {
        log.debug("REST request to save Vthumb : {}", vthumb);
        if (vthumb.getId() != null) {
            throw new BadRequestAlertException("A new vthumb cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vthumb result = vthumbRepository.save(vthumb);
        return ResponseEntity.created(new URI("/api/vthumbs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vthumbs} : Updates an existing vthumb.
     *
     * @param vthumb the vthumb to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vthumb,
     * or with status {@code 400 (Bad Request)} if the vthumb is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vthumb couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vthumbs")
    public ResponseEntity<Vthumb> updateVthumb(@Valid @RequestBody Vthumb vthumb) throws URISyntaxException {
        log.debug("REST request to update Vthumb : {}", vthumb);
        if (vthumb.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Vthumb result = vthumbRepository.save(vthumb);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vthumb.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vthumbs} : get all the vthumbs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vthumbs in body.
     */
    @GetMapping("/vthumbs")
    public ResponseEntity<List<Vthumb>> getAllVthumbs(Pageable pageable) {
        log.debug("REST request to get a page of Vthumbs");
        Page<Vthumb> page = vthumbRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vthumbs/:id} : get the "id" vthumb.
     *
     * @param id the id of the vthumb to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vthumb, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vthumbs/{id}")
    public ResponseEntity<Vthumb> getVthumb(@PathVariable Long id) {
        log.debug("REST request to get Vthumb : {}", id);
        Optional<Vthumb> vthumb = vthumbRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vthumb);
    }

    /**
     * {@code DELETE  /vthumbs/:id} : delete the "id" vthumb.
     *
     * @param id the id of the vthumb to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vthumbs/{id}")
    public ResponseEntity<Void> deleteVthumb(@PathVariable Long id) {
        log.debug("REST request to delete Vthumb : {}", id);
        vthumbRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
