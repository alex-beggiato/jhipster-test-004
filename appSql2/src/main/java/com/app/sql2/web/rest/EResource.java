package com.app.sql2.web.rest;

import com.app.sql2.domain.E;
import com.app.sql2.repository.ERepository;
import com.app.sql2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.app.sql2.domain.E}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EResource {

    private final Logger log = LoggerFactory.getLogger(EResource.class);

    private static final String ENTITY_NAME = "appSql2E";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ERepository eRepository;

    public EResource(ERepository eRepository) {
        this.eRepository = eRepository;
    }

    /**
     * {@code POST  /es} : Create a new e.
     *
     * @param e the e to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new e, or with status {@code 400 (Bad Request)} if the e has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/es")
    public ResponseEntity<E> createE(@RequestBody E e) throws URISyntaxException {
        log.debug("REST request to save E : {}", e);
        if (e.getId() != null) {
            throw new BadRequestAlertException("A new e cannot already have an ID", ENTITY_NAME, "idexists");
        }
        E result = eRepository.save(e);
        return ResponseEntity
            .created(new URI("/api/es/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /es/:id} : Updates an existing e.
     *
     * @param id the id of the e to save.
     * @param e the e to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated e,
     * or with status {@code 400 (Bad Request)} if the e is not valid,
     * or with status {@code 500 (Internal Server Error)} if the e couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/es/{id}")
    public ResponseEntity<E> updateE(@PathVariable(value = "id", required = false) final Long id, @RequestBody E e)
        throws URISyntaxException {
        log.debug("REST request to update E : {}, {}", id, e);
        if (e.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, e.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        E result = eRepository.save(e);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, e.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /es/:id} : Partial updates given fields of an existing e, field will ignore if it is null
     *
     * @param id the id of the e to save.
     * @param e the e to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated e,
     * or with status {@code 400 (Bad Request)} if the e is not valid,
     * or with status {@code 404 (Not Found)} if the e is not found,
     * or with status {@code 500 (Internal Server Error)} if the e couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/es/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<E> partialUpdateE(@PathVariable(value = "id", required = false) final Long id, @RequestBody E e)
        throws URISyntaxException {
        log.debug("REST request to partial update E partially : {}, {}", id, e);
        if (e.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, e.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<E> result = eRepository
            .findById(e.getId())
            .map(existingE -> {
                if (e.getUid() != null) {
                    existingE.setUid(e.getUid());
                }
                if (e.getEntCCCuid() != null) {
                    existingE.setEntCCCuid(e.getEntCCCuid());
                }
                if (e.getEntEEEcod() != null) {
                    existingE.setEntEEEcod(e.getEntEEEcod());
                }
                if (e.getEntEEEdsc() != null) {
                    existingE.setEntEEEdsc(e.getEntEEEdsc());
                }

                return existingE;
            })
            .map(eRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, e.getId().toString())
        );
    }

    /**
     * {@code GET  /es} : get all the eS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eS in body.
     */
    @GetMapping("/es")
    public List<E> getAllES() {
        log.debug("REST request to get all ES");
        return eRepository.findAll();
    }

    /**
     * {@code GET  /es/:id} : get the "id" e.
     *
     * @param id the id of the e to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the e, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/es/{id}")
    public ResponseEntity<E> getE(@PathVariable Long id) {
        log.debug("REST request to get E : {}", id);
        Optional<E> e = eRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(e);
    }

    /**
     * {@code DELETE  /es/:id} : delete the "id" e.
     *
     * @param id the id of the e to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/es/{id}")
    public ResponseEntity<Void> deleteE(@PathVariable Long id) {
        log.debug("REST request to delete E : {}", id);
        eRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
