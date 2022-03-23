package com.app.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.app.gateway.IntegrationTest;
import com.app.gateway.domain.E;
import com.app.gateway.repository.ERepository;
import com.app.gateway.repository.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link EResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EResourceIT {

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final Long DEFAULT_ENT_CC_CUID = 1L;
    private static final Long UPDATED_ENT_CC_CUID = 2L;

    private static final String DEFAULT_ENT_EE_ECOD = "AAAAAAAAAA";
    private static final String UPDATED_ENT_EE_ECOD = "BBBBBBBBBB";

    private static final String DEFAULT_ENT_EE_EDSC = "AAAAAAAAAA";
    private static final String UPDATED_ENT_EE_EDSC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/es";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ERepository eRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private E e;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static E createEntity(EntityManager em) {
        E e = new E().uid(DEFAULT_UID).entCCCuid(DEFAULT_ENT_CC_CUID).entEEEcod(DEFAULT_ENT_EE_ECOD).entEEEdsc(DEFAULT_ENT_EE_EDSC);
        return e;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static E createUpdatedEntity(EntityManager em) {
        E e = new E().uid(UPDATED_UID).entCCCuid(UPDATED_ENT_CC_CUID).entEEEcod(UPDATED_ENT_EE_ECOD).entEEEdsc(UPDATED_ENT_EE_EDSC);
        return e;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(E.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        e = createEntity(em);
    }

    @Test
    void createE() throws Exception {
        int databaseSizeBeforeCreate = eRepository.findAll().collectList().block().size();
        // Create the E
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(e))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeCreate + 1);
        E testE = eList.get(eList.size() - 1);
        assertThat(testE.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testE.getEntCCCuid()).isEqualTo(DEFAULT_ENT_CC_CUID);
        assertThat(testE.getEntEEEcod()).isEqualTo(DEFAULT_ENT_EE_ECOD);
        assertThat(testE.getEntEEEdsc()).isEqualTo(DEFAULT_ENT_EE_EDSC);
    }

    @Test
    void createEWithExistingId() throws Exception {
        // Create the E with an existing ID
        e.setId(1L);

        int databaseSizeBeforeCreate = eRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(e))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllESAsStream() {
        // Initialize the database
        eRepository.save(e).block();

        List<E> eList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(E.class)
            .getResponseBody()
            .filter(e::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(eList).isNotNull();
        assertThat(eList).hasSize(1);
        E testE = eList.get(0);
        assertThat(testE.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testE.getEntCCCuid()).isEqualTo(DEFAULT_ENT_CC_CUID);
        assertThat(testE.getEntEEEcod()).isEqualTo(DEFAULT_ENT_EE_ECOD);
        assertThat(testE.getEntEEEdsc()).isEqualTo(DEFAULT_ENT_EE_EDSC);
    }

    @Test
    void getAllES() {
        // Initialize the database
        eRepository.save(e).block();

        // Get all the eList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(e.getId().intValue()))
            .jsonPath("$.[*].uid")
            .value(hasItem(DEFAULT_UID.toString()))
            .jsonPath("$.[*].entCCCuid")
            .value(hasItem(DEFAULT_ENT_CC_CUID.intValue()))
            .jsonPath("$.[*].entEEEcod")
            .value(hasItem(DEFAULT_ENT_EE_ECOD))
            .jsonPath("$.[*].entEEEdsc")
            .value(hasItem(DEFAULT_ENT_EE_EDSC));
    }

    @Test
    void getE() {
        // Initialize the database
        eRepository.save(e).block();

        // Get the e
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, e.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(e.getId().intValue()))
            .jsonPath("$.uid")
            .value(is(DEFAULT_UID.toString()))
            .jsonPath("$.entCCCuid")
            .value(is(DEFAULT_ENT_CC_CUID.intValue()))
            .jsonPath("$.entEEEcod")
            .value(is(DEFAULT_ENT_EE_ECOD))
            .jsonPath("$.entEEEdsc")
            .value(is(DEFAULT_ENT_EE_EDSC));
    }

    @Test
    void getNonExistingE() {
        // Get the e
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewE() throws Exception {
        // Initialize the database
        eRepository.save(e).block();

        int databaseSizeBeforeUpdate = eRepository.findAll().collectList().block().size();

        // Update the e
        E updatedE = eRepository.findById(e.getId()).block();
        updatedE.uid(UPDATED_UID).entCCCuid(UPDATED_ENT_CC_CUID).entEEEcod(UPDATED_ENT_EE_ECOD).entEEEdsc(UPDATED_ENT_EE_EDSC);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedE.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedE))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeUpdate);
        E testE = eList.get(eList.size() - 1);
        assertThat(testE.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testE.getEntCCCuid()).isEqualTo(UPDATED_ENT_CC_CUID);
        assertThat(testE.getEntEEEcod()).isEqualTo(UPDATED_ENT_EE_ECOD);
        assertThat(testE.getEntEEEdsc()).isEqualTo(UPDATED_ENT_EE_EDSC);
    }

    @Test
    void putNonExistingE() throws Exception {
        int databaseSizeBeforeUpdate = eRepository.findAll().collectList().block().size();
        e.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, e.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(e))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchE() throws Exception {
        int databaseSizeBeforeUpdate = eRepository.findAll().collectList().block().size();
        e.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(e))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamE() throws Exception {
        int databaseSizeBeforeUpdate = eRepository.findAll().collectList().block().size();
        e.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(e))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEWithPatch() throws Exception {
        // Initialize the database
        eRepository.save(e).block();

        int databaseSizeBeforeUpdate = eRepository.findAll().collectList().block().size();

        // Update the e using partial update
        E partialUpdatedE = new E();
        partialUpdatedE.setId(e.getId());

        partialUpdatedE.entEEEcod(UPDATED_ENT_EE_ECOD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedE.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedE))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeUpdate);
        E testE = eList.get(eList.size() - 1);
        assertThat(testE.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testE.getEntCCCuid()).isEqualTo(DEFAULT_ENT_CC_CUID);
        assertThat(testE.getEntEEEcod()).isEqualTo(UPDATED_ENT_EE_ECOD);
        assertThat(testE.getEntEEEdsc()).isEqualTo(DEFAULT_ENT_EE_EDSC);
    }

    @Test
    void fullUpdateEWithPatch() throws Exception {
        // Initialize the database
        eRepository.save(e).block();

        int databaseSizeBeforeUpdate = eRepository.findAll().collectList().block().size();

        // Update the e using partial update
        E partialUpdatedE = new E();
        partialUpdatedE.setId(e.getId());

        partialUpdatedE.uid(UPDATED_UID).entCCCuid(UPDATED_ENT_CC_CUID).entEEEcod(UPDATED_ENT_EE_ECOD).entEEEdsc(UPDATED_ENT_EE_EDSC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedE.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedE))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeUpdate);
        E testE = eList.get(eList.size() - 1);
        assertThat(testE.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testE.getEntCCCuid()).isEqualTo(UPDATED_ENT_CC_CUID);
        assertThat(testE.getEntEEEcod()).isEqualTo(UPDATED_ENT_EE_ECOD);
        assertThat(testE.getEntEEEdsc()).isEqualTo(UPDATED_ENT_EE_EDSC);
    }

    @Test
    void patchNonExistingE() throws Exception {
        int databaseSizeBeforeUpdate = eRepository.findAll().collectList().block().size();
        e.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, e.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(e))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchE() throws Exception {
        int databaseSizeBeforeUpdate = eRepository.findAll().collectList().block().size();
        e.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(e))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamE() throws Exception {
        int databaseSizeBeforeUpdate = eRepository.findAll().collectList().block().size();
        e.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(e))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the E in the database
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteE() {
        // Initialize the database
        eRepository.save(e).block();

        int databaseSizeBeforeDelete = eRepository.findAll().collectList().block().size();

        // Delete the e
        webTestClient.delete().uri(ENTITY_API_URL_ID, e.getId()).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isNoContent();

        // Validate the database contains one less item
        List<E> eList = eRepository.findAll().collectList().block();
        assertThat(eList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
