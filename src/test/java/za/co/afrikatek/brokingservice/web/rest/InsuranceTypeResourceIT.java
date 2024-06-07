package za.co.afrikatek.brokingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.afrikatek.brokingservice.domain.InsuranceTypeAsserts.*;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.IntegrationTest;
import za.co.afrikatek.brokingservice.domain.InsuranceType;
import za.co.afrikatek.brokingservice.repository.InsuranceTypeRepository;

/**
 * Integration tests for the {@link InsuranceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insurance-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceTypeRepository insuranceTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceTypeMockMvc;

    private InsuranceType insuranceType;

    private InsuranceType insertedInsuranceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceType createEntity(EntityManager em) {
        InsuranceType insuranceType = new InsuranceType().name(DEFAULT_NAME);
        return insuranceType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceType createUpdatedEntity(EntityManager em) {
        InsuranceType insuranceType = new InsuranceType().name(UPDATED_NAME);
        return insuranceType;
    }

    @BeforeEach
    public void initTest() {
        insuranceType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedInsuranceType != null) {
            insuranceTypeRepository.delete(insertedInsuranceType);
            insertedInsuranceType = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceType
        var returnedInsuranceType = om.readValue(
            restInsuranceTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceType.class
        );

        // Validate the InsuranceType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceTypeUpdatableFieldsEquals(returnedInsuranceType, getPersistedInsuranceType(returnedInsuranceType));

        insertedInsuranceType = returnedInsuranceType;
    }

    @Test
    @Transactional
    void createInsuranceTypeWithExistingId() throws Exception {
        // Create the InsuranceType with an existing ID
        insuranceType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceType)))
            .andExpect(status().isBadRequest());

        // Validate the InsuranceType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insuranceType.setName(null);

        // Create the InsuranceType, which fails.

        restInsuranceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInsuranceTypes() throws Exception {
        // Initialize the database
        insertedInsuranceType = insuranceTypeRepository.saveAndFlush(insuranceType);

        // Get all the insuranceTypeList
        restInsuranceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getInsuranceType() throws Exception {
        // Initialize the database
        insertedInsuranceType = insuranceTypeRepository.saveAndFlush(insuranceType);

        // Get the insuranceType
        restInsuranceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceType() throws Exception {
        // Get the insuranceType
        restInsuranceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceType() throws Exception {
        // Initialize the database
        insertedInsuranceType = insuranceTypeRepository.saveAndFlush(insuranceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceType
        InsuranceType updatedInsuranceType = insuranceTypeRepository.findById(insuranceType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceType are not directly saved in db
        em.detach(updatedInsuranceType);
        updatedInsuranceType.name(UPDATED_NAME);

        restInsuranceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceType))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceTypeToMatchAllProperties(updatedInsuranceType);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceType = insuranceTypeRepository.saveAndFlush(insuranceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceType using partial update
        InsuranceType partialUpdatedInsuranceType = new InsuranceType();
        partialUpdatedInsuranceType.setId(insuranceType.getId());

        restInsuranceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceType))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuranceType, insuranceType),
            getPersistedInsuranceType(insuranceType)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceType = insuranceTypeRepository.saveAndFlush(insuranceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceType using partial update
        InsuranceType partialUpdatedInsuranceType = new InsuranceType();
        partialUpdatedInsuranceType.setId(insuranceType.getId());

        partialUpdatedInsuranceType.name(UPDATED_NAME);

        restInsuranceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceType))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceTypeUpdatableFieldsEquals(partialUpdatedInsuranceType, getPersistedInsuranceType(partialUpdatedInsuranceType));
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(insuranceType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceType() throws Exception {
        // Initialize the database
        insertedInsuranceType = insuranceTypeRepository.saveAndFlush(insuranceType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceType
        restInsuranceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected InsuranceType getPersistedInsuranceType(InsuranceType insuranceType) {
        return insuranceTypeRepository.findById(insuranceType.getId()).orElseThrow();
    }

    protected void assertPersistedInsuranceTypeToMatchAllProperties(InsuranceType expectedInsuranceType) {
        assertInsuranceTypeAllPropertiesEquals(expectedInsuranceType, getPersistedInsuranceType(expectedInsuranceType));
    }

    protected void assertPersistedInsuranceTypeToMatchUpdatableProperties(InsuranceType expectedInsuranceType) {
        assertInsuranceTypeAllUpdatablePropertiesEquals(expectedInsuranceType, getPersistedInsuranceType(expectedInsuranceType));
    }
}
