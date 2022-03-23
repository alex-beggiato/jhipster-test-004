package com.app.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.app.gateway.IntegrationTest;
import com.app.gateway.domain.C;
import com.app.gateway.repository.CRepository;
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
 * Integration tests for the {@link CResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CResourceIT {

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_ENT_CC_CCOD = "AAAAAAAAAA";
    private static final String UPDATED_ENT_CC_CCOD = "BBBBBBBBBB";

    private static final String DEFAULT_ENT_CC_CDSC = "AAAAAAAAAA";
    private static final String UPDATED_ENT_CC_CDSC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CRepository cRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private C c;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static C createEntity(EntityManager em) {
        C c = new C().uid(DEFAULT_UID).entCCCcod(DEFAULT_ENT_CC_CCOD).entCCCdsc(DEFAULT_ENT_CC_CDSC);
        return c;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static C createUpdatedEntity(EntityManager em) {
        C c = new C().uid(UPDATED_UID).entCCCcod(UPDATED_ENT_CC_CCOD).entCCCdsc(UPDATED_ENT_CC_CDSC);
        return c;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(C.class).block();
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
        c = createEntity(em);
    }

    @Test
    void createC() throws Exception {
        int databaseSizeBeforeCreate = cRepository.findAll().collectList().block().size();
        // Create the C
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(c))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeCreate + 1);
        C testC = cList.get(cList.size() - 1);
        assertThat(testC.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testC.getEntCCCcod()).isEqualTo(DEFAULT_ENT_CC_CCOD);
        assertThat(testC.getEntCCCdsc()).isEqualTo(DEFAULT_ENT_CC_CDSC);
    }

    @Test
    void createCWithExistingId() throws Exception {
        // Create the C with an existing ID
        c.setId(1L);

        int databaseSizeBeforeCreate = cRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(c))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCSAsStream() {
        // Initialize the database
        cRepository.save(c).block();

        List<C> cList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(C.class)
            .getResponseBody()
            .filter(c::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(cList).isNotNull();
        assertThat(cList).hasSize(1);
        C testC = cList.get(0);
        assertThat(testC.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testC.getEntCCCcod()).isEqualTo(DEFAULT_ENT_CC_CCOD);
        assertThat(testC.getEntCCCdsc()).isEqualTo(DEFAULT_ENT_CC_CDSC);
    }

    @Test
    void getAllCS() {
        // Initialize the database
        cRepository.save(c).block();

        // Get all the cList
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
            .value(hasItem(c.getId().intValue()))
            .jsonPath("$.[*].uid")
            .value(hasItem(DEFAULT_UID.toString()))
            .jsonPath("$.[*].entCCCcod")
            .value(hasItem(DEFAULT_ENT_CC_CCOD))
            .jsonPath("$.[*].entCCCdsc")
            .value(hasItem(DEFAULT_ENT_CC_CDSC));
    }

    @Test
    void getC() {
        // Initialize the database
        cRepository.save(c).block();

        // Get the c
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, c.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(c.getId().intValue()))
            .jsonPath("$.uid")
            .value(is(DEFAULT_UID.toString()))
            .jsonPath("$.entCCCcod")
            .value(is(DEFAULT_ENT_CC_CCOD))
            .jsonPath("$.entCCCdsc")
            .value(is(DEFAULT_ENT_CC_CDSC));
    }

    @Test
    void getNonExistingC() {
        // Get the c
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewC() throws Exception {
        // Initialize the database
        cRepository.save(c).block();

        int databaseSizeBeforeUpdate = cRepository.findAll().collectList().block().size();

        // Update the c
        C updatedC = cRepository.findById(c.getId()).block();
        updatedC.uid(UPDATED_UID).entCCCcod(UPDATED_ENT_CC_CCOD).entCCCdsc(UPDATED_ENT_CC_CDSC);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedC.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedC))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeUpdate);
        C testC = cList.get(cList.size() - 1);
        assertThat(testC.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testC.getEntCCCcod()).isEqualTo(UPDATED_ENT_CC_CCOD);
        assertThat(testC.getEntCCCdsc()).isEqualTo(UPDATED_ENT_CC_CDSC);
    }

    @Test
    void putNonExistingC() throws Exception {
        int databaseSizeBeforeUpdate = cRepository.findAll().collectList().block().size();
        c.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, c.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(c))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchC() throws Exception {
        int databaseSizeBeforeUpdate = cRepository.findAll().collectList().block().size();
        c.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(c))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamC() throws Exception {
        int databaseSizeBeforeUpdate = cRepository.findAll().collectList().block().size();
        c.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(c))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCWithPatch() throws Exception {
        // Initialize the database
        cRepository.save(c).block();

        int databaseSizeBeforeUpdate = cRepository.findAll().collectList().block().size();

        // Update the c using partial update
        C partialUpdatedC = new C();
        partialUpdatedC.setId(c.getId());

        partialUpdatedC.uid(UPDATED_UID).entCCCcod(UPDATED_ENT_CC_CCOD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedC.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedC))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeUpdate);
        C testC = cList.get(cList.size() - 1);
        assertThat(testC.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testC.getEntCCCcod()).isEqualTo(UPDATED_ENT_CC_CCOD);
        assertThat(testC.getEntCCCdsc()).isEqualTo(DEFAULT_ENT_CC_CDSC);
    }

    @Test
    void fullUpdateCWithPatch() throws Exception {
        // Initialize the database
        cRepository.save(c).block();

        int databaseSizeBeforeUpdate = cRepository.findAll().collectList().block().size();

        // Update the c using partial update
        C partialUpdatedC = new C();
        partialUpdatedC.setId(c.getId());

        partialUpdatedC.uid(UPDATED_UID).entCCCcod(UPDATED_ENT_CC_CCOD).entCCCdsc(UPDATED_ENT_CC_CDSC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedC.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedC))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeUpdate);
        C testC = cList.get(cList.size() - 1);
        assertThat(testC.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testC.getEntCCCcod()).isEqualTo(UPDATED_ENT_CC_CCOD);
        assertThat(testC.getEntCCCdsc()).isEqualTo(UPDATED_ENT_CC_CDSC);
    }

    @Test
    void patchNonExistingC() throws Exception {
        int databaseSizeBeforeUpdate = cRepository.findAll().collectList().block().size();
        c.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, c.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(c))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchC() throws Exception {
        int databaseSizeBeforeUpdate = cRepository.findAll().collectList().block().size();
        c.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(c))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamC() throws Exception {
        int databaseSizeBeforeUpdate = cRepository.findAll().collectList().block().size();
        c.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(c))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the C in the database
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteC() {
        // Initialize the database
        cRepository.save(c).block();

        int databaseSizeBeforeDelete = cRepository.findAll().collectList().block().size();

        // Delete the c
        webTestClient.delete().uri(ENTITY_API_URL_ID, c.getId()).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isNoContent();

        // Validate the database contains one less item
        List<C> cList = cRepository.findAll().collectList().block();
        assertThat(cList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
