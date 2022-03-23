package com.app.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.app.gateway.IntegrationTest;
import com.app.gateway.domain.A;
import com.app.gateway.repository.ARepository;
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
 * Integration tests for the {@link AResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AResourceIT {

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_ENT_AA_ACOD = "AAAAAAAAAA";
    private static final String UPDATED_ENT_AA_ACOD = "BBBBBBBBBB";

    private static final String DEFAULT_ENT_AA_ADSC = "AAAAAAAAAA";
    private static final String UPDATED_ENT_AA_ADSC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/as";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ARepository aRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private A a;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static A createEntity(EntityManager em) {
        A a = new A().uid(DEFAULT_UID).entAAAcod(DEFAULT_ENT_AA_ACOD).entAAAdsc(DEFAULT_ENT_AA_ADSC);
        return a;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static A createUpdatedEntity(EntityManager em) {
        A a = new A().uid(UPDATED_UID).entAAAcod(UPDATED_ENT_AA_ACOD).entAAAdsc(UPDATED_ENT_AA_ADSC);
        return a;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(A.class).block();
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
        a = createEntity(em);
    }

    @Test
    void createA() throws Exception {
        int databaseSizeBeforeCreate = aRepository.findAll().collectList().block().size();
        // Create the A
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(a))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeCreate + 1);
        A testA = aList.get(aList.size() - 1);
        assertThat(testA.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testA.getEntAAAcod()).isEqualTo(DEFAULT_ENT_AA_ACOD);
        assertThat(testA.getEntAAAdsc()).isEqualTo(DEFAULT_ENT_AA_ADSC);
    }

    @Test
    void createAWithExistingId() throws Exception {
        // Create the A with an existing ID
        a.setId(1L);

        int databaseSizeBeforeCreate = aRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(a))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllASAsStream() {
        // Initialize the database
        aRepository.save(a).block();

        List<A> aList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(A.class)
            .getResponseBody()
            .filter(a::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(aList).isNotNull();
        assertThat(aList).hasSize(1);
        A testA = aList.get(0);
        assertThat(testA.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testA.getEntAAAcod()).isEqualTo(DEFAULT_ENT_AA_ACOD);
        assertThat(testA.getEntAAAdsc()).isEqualTo(DEFAULT_ENT_AA_ADSC);
    }

    @Test
    void getAllAS() {
        // Initialize the database
        aRepository.save(a).block();

        // Get all the aList
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
            .value(hasItem(a.getId().intValue()))
            .jsonPath("$.[*].uid")
            .value(hasItem(DEFAULT_UID.toString()))
            .jsonPath("$.[*].entAAAcod")
            .value(hasItem(DEFAULT_ENT_AA_ACOD))
            .jsonPath("$.[*].entAAAdsc")
            .value(hasItem(DEFAULT_ENT_AA_ADSC));
    }

    @Test
    void getA() {
        // Initialize the database
        aRepository.save(a).block();

        // Get the a
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, a.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(a.getId().intValue()))
            .jsonPath("$.uid")
            .value(is(DEFAULT_UID.toString()))
            .jsonPath("$.entAAAcod")
            .value(is(DEFAULT_ENT_AA_ACOD))
            .jsonPath("$.entAAAdsc")
            .value(is(DEFAULT_ENT_AA_ADSC));
    }

    @Test
    void getNonExistingA() {
        // Get the a
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewA() throws Exception {
        // Initialize the database
        aRepository.save(a).block();

        int databaseSizeBeforeUpdate = aRepository.findAll().collectList().block().size();

        // Update the a
        A updatedA = aRepository.findById(a.getId()).block();
        updatedA.uid(UPDATED_UID).entAAAcod(UPDATED_ENT_AA_ACOD).entAAAdsc(UPDATED_ENT_AA_ADSC);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedA.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedA))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
        A testA = aList.get(aList.size() - 1);
        assertThat(testA.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testA.getEntAAAcod()).isEqualTo(UPDATED_ENT_AA_ACOD);
        assertThat(testA.getEntAAAdsc()).isEqualTo(UPDATED_ENT_AA_ADSC);
    }

    @Test
    void putNonExistingA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().collectList().block().size();
        a.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, a.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(a))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().collectList().block().size();
        a.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(a))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().collectList().block().size();
        a.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(a))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAWithPatch() throws Exception {
        // Initialize the database
        aRepository.save(a).block();

        int databaseSizeBeforeUpdate = aRepository.findAll().collectList().block().size();

        // Update the a using partial update
        A partialUpdatedA = new A();
        partialUpdatedA.setId(a.getId());

        partialUpdatedA.uid(UPDATED_UID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedA.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedA))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
        A testA = aList.get(aList.size() - 1);
        assertThat(testA.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testA.getEntAAAcod()).isEqualTo(DEFAULT_ENT_AA_ACOD);
        assertThat(testA.getEntAAAdsc()).isEqualTo(DEFAULT_ENT_AA_ADSC);
    }

    @Test
    void fullUpdateAWithPatch() throws Exception {
        // Initialize the database
        aRepository.save(a).block();

        int databaseSizeBeforeUpdate = aRepository.findAll().collectList().block().size();

        // Update the a using partial update
        A partialUpdatedA = new A();
        partialUpdatedA.setId(a.getId());

        partialUpdatedA.uid(UPDATED_UID).entAAAcod(UPDATED_ENT_AA_ACOD).entAAAdsc(UPDATED_ENT_AA_ADSC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedA.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedA))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
        A testA = aList.get(aList.size() - 1);
        assertThat(testA.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testA.getEntAAAcod()).isEqualTo(UPDATED_ENT_AA_ACOD);
        assertThat(testA.getEntAAAdsc()).isEqualTo(UPDATED_ENT_AA_ADSC);
    }

    @Test
    void patchNonExistingA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().collectList().block().size();
        a.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, a.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(a))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().collectList().block().size();
        a.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(a))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamA() throws Exception {
        int databaseSizeBeforeUpdate = aRepository.findAll().collectList().block().size();
        a.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(a))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the A in the database
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteA() {
        // Initialize the database
        aRepository.save(a).block();

        int databaseSizeBeforeDelete = aRepository.findAll().collectList().block().size();

        // Delete the a
        webTestClient.delete().uri(ENTITY_API_URL_ID, a.getId()).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isNoContent();

        // Validate the database contains one less item
        List<A> aList = aRepository.findAll().collectList().block();
        assertThat(aList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
