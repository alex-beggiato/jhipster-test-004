package com.app.gateway.web.rest;

import com.app.gateway.domain.E;
import com.app.gateway.repository.ERepository;
import com.app.gateway.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.app.gateway.domain.E}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EResource {

    private final Logger log = LoggerFactory.getLogger(EResource.class);

    private static final String ENTITY_NAME = "e";

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
    public Mono<ResponseEntity<E>> createE(@RequestBody E e) throws URISyntaxException {
        log.debug("REST request to save E : {}", e);
        if (e.getId() != null) {
            throw new BadRequestAlertException("A new e cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return eRepository
            .save(e)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/es/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<E>> updateE(@PathVariable(value = "id", required = false) final Long id, @RequestBody E e)
        throws URISyntaxException {
        log.debug("REST request to update E : {}, {}", id, e);
        if (e.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, e.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return eRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return eRepository
                    .save(e)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
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
    public Mono<ResponseEntity<E>> partialUpdateE(@PathVariable(value = "id", required = false) final Long id, @RequestBody E e)
        throws URISyntaxException {
        log.debug("REST request to partial update E partially : {}, {}", id, e);
        if (e.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, e.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return eRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<E> result = eRepository
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
                    .flatMap(eRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /es} : get all the eS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eS in body.
     */
    @GetMapping("/es")
    public Mono<List<E>> getAllES() {
        log.debug("REST request to get all ES");
        return eRepository.findAll().collectList();
    }

    /**
     * {@code GET  /es} : get all the eS as a stream.
     * @return the {@link Flux} of eS.
     */
    @GetMapping(value = "/es", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<E> getAllESAsStream() {
        log.debug("REST request to get all ES as a stream");
        return eRepository.findAll();
    }

    /**
     * {@code GET  /es/:id} : get the "id" e.
     *
     * @param id the id of the e to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the e, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/es/{id}")
    public Mono<ResponseEntity<E>> getE(@PathVariable Long id) {
        log.debug("REST request to get E : {}", id);
        Mono<E> e = eRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(e);
    }

    /**
     * {@code DELETE  /es/:id} : delete the "id" e.
     *
     * @param id the id of the e to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/es/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteE(@PathVariable Long id) {
        log.debug("REST request to delete E : {}", id);
        return eRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
