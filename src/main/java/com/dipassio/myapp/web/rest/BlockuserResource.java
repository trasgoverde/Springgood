package com.dipassio.myapp.web.rest;

import com.dipassio.myapp.domain.Blockuser;
import com.dipassio.myapp.repository.BlockuserRepository;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.dipassio.myapp.domain.Blockuser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BlockuserResource {

    private final Logger log = LoggerFactory.getLogger(BlockuserResource.class);

    private static final String ENTITY_NAME = "blockuser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlockuserRepository blockuserRepository;

    public BlockuserResource(BlockuserRepository blockuserRepository) {
        this.blockuserRepository = blockuserRepository;
    }

    /**
     * {@code POST  /blockusers} : Create a new blockuser.
     *
     * @param blockuser the blockuser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blockuser, or with status {@code 400 (Bad Request)} if the blockuser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/blockusers")
    public ResponseEntity<Blockuser> createBlockuser(@RequestBody Blockuser blockuser) throws URISyntaxException {
        log.debug("REST request to save Blockuser : {}", blockuser);
        if (blockuser.getId() != null) {
            throw new BadRequestAlertException("A new blockuser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Blockuser result = blockuserRepository.save(blockuser);
        return ResponseEntity.created(new URI("/api/blockusers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /blockusers} : Updates an existing blockuser.
     *
     * @param blockuser the blockuser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockuser,
     * or with status {@code 400 (Bad Request)} if the blockuser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blockuser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/blockusers")
    public ResponseEntity<Blockuser> updateBlockuser(@RequestBody Blockuser blockuser) throws URISyntaxException {
        log.debug("REST request to update Blockuser : {}", blockuser);
        if (blockuser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Blockuser result = blockuserRepository.save(blockuser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blockuser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /blockusers} : get all the blockusers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blockusers in body.
     */
    @GetMapping("/blockusers")
    public ResponseEntity<List<Blockuser>> getAllBlockusers(Pageable pageable) {
        log.debug("REST request to get a page of Blockusers");
        Page<Blockuser> page = blockuserRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /blockusers/:id} : get the "id" blockuser.
     *
     * @param id the id of the blockuser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blockuser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/blockusers/{id}")
    public ResponseEntity<Blockuser> getBlockuser(@PathVariable Long id) {
        log.debug("REST request to get Blockuser : {}", id);
        Optional<Blockuser> blockuser = blockuserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(blockuser);
    }

    /**
     * {@code DELETE  /blockusers/:id} : delete the "id" blockuser.
     *
     * @param id the id of the blockuser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/blockusers/{id}")
    public ResponseEntity<Void> deleteBlockuser(@PathVariable Long id) {
        log.debug("REST request to delete Blockuser : {}", id);
        blockuserRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
