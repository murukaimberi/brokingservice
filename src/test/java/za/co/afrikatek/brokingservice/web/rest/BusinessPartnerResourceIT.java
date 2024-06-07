package za.co.afrikatek.brokingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.afrikatek.brokingservice.domain.BusinessPartnerAsserts.*;
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
import za.co.afrikatek.brokingservice.domain.BusinessPartner;
import za.co.afrikatek.brokingservice.domain.enumeration.InsuranceAgentType;
import za.co.afrikatek.brokingservice.repository.BusinessPartnerRepository;

/**
 * Integration tests for the {@link BusinessPartnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusinessPartnerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REPRESENTATIVE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPRESENTATIVE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final InsuranceAgentType DEFAULT_AGENT_TYPE = InsuranceAgentType.INSURED;
    private static final InsuranceAgentType UPDATED_AGENT_TYPE = InsuranceAgentType.BROKER;

    private static final String ENTITY_API_URL = "/api/business-partners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BusinessPartnerRepository businessPartnerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessPartnerMockMvc;

    private BusinessPartner businessPartner;

    private BusinessPartner insertedBusinessPartner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessPartner createEntity(EntityManager em) {
        BusinessPartner businessPartner = new BusinessPartner()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .representativeName(DEFAULT_REPRESENTATIVE_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .agentType(DEFAULT_AGENT_TYPE);
        return businessPartner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessPartner createUpdatedEntity(EntityManager em) {
        BusinessPartner businessPartner = new BusinessPartner()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .representativeName(UPDATED_REPRESENTATIVE_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .agentType(UPDATED_AGENT_TYPE);
        return businessPartner;
    }

    @BeforeEach
    public void initTest() {
        businessPartner = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBusinessPartner != null) {
            businessPartnerRepository.delete(insertedBusinessPartner);
            insertedBusinessPartner = null;
        }
    }

    @Test
    @Transactional
    void createBusinessPartner() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BusinessPartner
        var returnedBusinessPartner = om.readValue(
            restBusinessPartnerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessPartner)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BusinessPartner.class
        );

        // Validate the BusinessPartner in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBusinessPartnerUpdatableFieldsEquals(returnedBusinessPartner, getPersistedBusinessPartner(returnedBusinessPartner));

        insertedBusinessPartner = returnedBusinessPartner;
    }

    @Test
    @Transactional
    void createBusinessPartnerWithExistingId() throws Exception {
        // Create the BusinessPartner with an existing ID
        businessPartner.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessPartner)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessPartner in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        businessPartner.setName(null);

        // Create the BusinessPartner, which fails.

        restBusinessPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessPartner)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRepresentativeNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        businessPartner.setRepresentativeName(null);

        // Create the BusinessPartner, which fails.

        restBusinessPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessPartner)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        businessPartner.setEmail(null);

        // Create the BusinessPartner, which fails.

        restBusinessPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessPartner)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        businessPartner.setPhoneNumber(null);

        // Create the BusinessPartner, which fails.

        restBusinessPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessPartner)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        businessPartner.setAgentType(null);

        // Create the BusinessPartner, which fails.

        restBusinessPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessPartner)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessPartners() throws Exception {
        // Initialize the database
        insertedBusinessPartner = businessPartnerRepository.saveAndFlush(businessPartner);

        // Get all the businessPartnerList
        restBusinessPartnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessPartner.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].representativeName").value(hasItem(DEFAULT_REPRESENTATIVE_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].agentType").value(hasItem(DEFAULT_AGENT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getBusinessPartner() throws Exception {
        // Initialize the database
        insertedBusinessPartner = businessPartnerRepository.saveAndFlush(businessPartner);

        // Get the businessPartner
        restBusinessPartnerMockMvc
            .perform(get(ENTITY_API_URL_ID, businessPartner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessPartner.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.representativeName").value(DEFAULT_REPRESENTATIVE_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.agentType").value(DEFAULT_AGENT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBusinessPartner() throws Exception {
        // Get the businessPartner
        restBusinessPartnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBusinessPartner() throws Exception {
        // Initialize the database
        insertedBusinessPartner = businessPartnerRepository.saveAndFlush(businessPartner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessPartner
        BusinessPartner updatedBusinessPartner = businessPartnerRepository.findById(businessPartner.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBusinessPartner are not directly saved in db
        em.detach(updatedBusinessPartner);
        updatedBusinessPartner
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .representativeName(UPDATED_REPRESENTATIVE_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .agentType(UPDATED_AGENT_TYPE);

        restBusinessPartnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBusinessPartner.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBusinessPartner))
            )
            .andExpect(status().isOk());

        // Validate the BusinessPartner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBusinessPartnerToMatchAllProperties(updatedBusinessPartner);
    }

    @Test
    @Transactional
    void putNonExistingBusinessPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessPartner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessPartnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessPartner.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessPartner))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessPartner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessPartner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessPartnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessPartner))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessPartner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessPartner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessPartnerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessPartner)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessPartner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessPartnerWithPatch() throws Exception {
        // Initialize the database
        insertedBusinessPartner = businessPartnerRepository.saveAndFlush(businessPartner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessPartner using partial update
        BusinessPartner partialUpdatedBusinessPartner = new BusinessPartner();
        partialUpdatedBusinessPartner.setId(businessPartner.getId());

        partialUpdatedBusinessPartner
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .representativeName(UPDATED_REPRESENTATIVE_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .agentType(UPDATED_AGENT_TYPE);

        restBusinessPartnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessPartner.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusinessPartner))
            )
            .andExpect(status().isOk());

        // Validate the BusinessPartner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessPartnerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBusinessPartner, businessPartner),
            getPersistedBusinessPartner(businessPartner)
        );
    }

    @Test
    @Transactional
    void fullUpdateBusinessPartnerWithPatch() throws Exception {
        // Initialize the database
        insertedBusinessPartner = businessPartnerRepository.saveAndFlush(businessPartner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessPartner using partial update
        BusinessPartner partialUpdatedBusinessPartner = new BusinessPartner();
        partialUpdatedBusinessPartner.setId(businessPartner.getId());

        partialUpdatedBusinessPartner
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .representativeName(UPDATED_REPRESENTATIVE_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .agentType(UPDATED_AGENT_TYPE);

        restBusinessPartnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessPartner.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusinessPartner))
            )
            .andExpect(status().isOk());

        // Validate the BusinessPartner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessPartnerUpdatableFieldsEquals(
            partialUpdatedBusinessPartner,
            getPersistedBusinessPartner(partialUpdatedBusinessPartner)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBusinessPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessPartner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessPartnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessPartner.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessPartner))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessPartner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessPartner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessPartnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessPartner))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessPartner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessPartner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessPartnerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(businessPartner)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessPartner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusinessPartner() throws Exception {
        // Initialize the database
        insertedBusinessPartner = businessPartnerRepository.saveAndFlush(businessPartner);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the businessPartner
        restBusinessPartnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessPartner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return businessPartnerRepository.count();
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

    protected BusinessPartner getPersistedBusinessPartner(BusinessPartner businessPartner) {
        return businessPartnerRepository.findById(businessPartner.getId()).orElseThrow();
    }

    protected void assertPersistedBusinessPartnerToMatchAllProperties(BusinessPartner expectedBusinessPartner) {
        assertBusinessPartnerAllPropertiesEquals(expectedBusinessPartner, getPersistedBusinessPartner(expectedBusinessPartner));
    }

    protected void assertPersistedBusinessPartnerToMatchUpdatableProperties(BusinessPartner expectedBusinessPartner) {
        assertBusinessPartnerAllUpdatablePropertiesEquals(expectedBusinessPartner, getPersistedBusinessPartner(expectedBusinessPartner));
    }
}
