package za.co.afrikatek.brokingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.afrikatek.brokingservice.domain.ReinsurancePlacementAsserts.*;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.createUpdateProxyForBean;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import za.co.afrikatek.brokingservice.domain.Contract;
import za.co.afrikatek.brokingservice.domain.ReinsurancePlacement;
import za.co.afrikatek.brokingservice.repository.ReinsurancePlacementRepository;

/**
 * Integration tests for the {@link ReinsurancePlacementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReinsurancePlacementResourceIT {

    private static final BigDecimal DEFAULT_RI_PERCENTAGE_COMMISSION = new BigDecimal(0);
    private static final BigDecimal UPDATED_RI_PERCENTAGE_COMMISSION = new BigDecimal(1);

    private static final BigDecimal DEFAULT_RI_PERCENTAGE_SHARE = new BigDecimal(0);
    private static final BigDecimal UPDATED_RI_PERCENTAGE_SHARE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_RI_TOTAL_SUM_INSURED = new BigDecimal(1);
    private static final BigDecimal UPDATED_RI_TOTAL_SUM_INSURED = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RI_LIMIT_OF_LIABILITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_RI_LIMIT_OF_LIABILITY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_GROSS_PREMIUM_HUNDRED = new BigDecimal(1);
    private static final BigDecimal UPDATED_GROSS_PREMIUM_HUNDRED = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RI_PREMIUM = new BigDecimal(1);
    private static final BigDecimal UPDATED_RI_PREMIUM = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RI_COMMISSION = new BigDecimal(1);
    private static final BigDecimal UPDATED_RI_COMMISSION = new BigDecimal(2);

    private static final BigDecimal DEFAULT_NET_DUE_FROM_INSURER = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_DUE_FROM_INSURER = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/reinsurance-placements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReinsurancePlacementRepository reinsurancePlacementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReinsurancePlacementMockMvc;

    private ReinsurancePlacement reinsurancePlacement;

    private ReinsurancePlacement insertedReinsurancePlacement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReinsurancePlacement createEntity(EntityManager em) {
        ReinsurancePlacement reinsurancePlacement = new ReinsurancePlacement()
            .riPercentageCommission(DEFAULT_RI_PERCENTAGE_COMMISSION)
            .riPercentageShare(DEFAULT_RI_PERCENTAGE_SHARE)
            .riTotalSumInsured(DEFAULT_RI_TOTAL_SUM_INSURED)
            .riLimitOfLiability(DEFAULT_RI_LIMIT_OF_LIABILITY)
            .grossPremiumHundred(DEFAULT_GROSS_PREMIUM_HUNDRED)
            .riPremium(DEFAULT_RI_PREMIUM)
            .riCommission(DEFAULT_RI_COMMISSION)
            .netDueFromInsurer(DEFAULT_NET_DUE_FROM_INSURER);
        // Add required entity
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            contract = ContractResourceIT.createEntity(em);
            em.persist(contract);
            em.flush();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        reinsurancePlacement.setContract(contract);
        return reinsurancePlacement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReinsurancePlacement createUpdatedEntity(EntityManager em) {
        ReinsurancePlacement reinsurancePlacement = new ReinsurancePlacement()
            .riPercentageCommission(UPDATED_RI_PERCENTAGE_COMMISSION)
            .riPercentageShare(UPDATED_RI_PERCENTAGE_SHARE)
            .riTotalSumInsured(UPDATED_RI_TOTAL_SUM_INSURED)
            .riLimitOfLiability(UPDATED_RI_LIMIT_OF_LIABILITY)
            .grossPremiumHundred(UPDATED_GROSS_PREMIUM_HUNDRED)
            .riPremium(UPDATED_RI_PREMIUM)
            .riCommission(UPDATED_RI_COMMISSION)
            .netDueFromInsurer(UPDATED_NET_DUE_FROM_INSURER);
        // Add required entity
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            contract = ContractResourceIT.createUpdatedEntity(em);
            em.persist(contract);
            em.flush();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        reinsurancePlacement.setContract(contract);
        return reinsurancePlacement;
    }

    @BeforeEach
    public void initTest() {
        reinsurancePlacement = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedReinsurancePlacement != null) {
            reinsurancePlacementRepository.delete(insertedReinsurancePlacement);
            insertedReinsurancePlacement = null;
        }
    }

    @Test
    @Transactional
    void createReinsurancePlacement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReinsurancePlacement
        var returnedReinsurancePlacement = om.readValue(
            restReinsurancePlacementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reinsurancePlacement)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReinsurancePlacement.class
        );

        // Validate the ReinsurancePlacement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReinsurancePlacementUpdatableFieldsEquals(
            returnedReinsurancePlacement,
            getPersistedReinsurancePlacement(returnedReinsurancePlacement)
        );

        insertedReinsurancePlacement = returnedReinsurancePlacement;
    }

    @Test
    @Transactional
    void createReinsurancePlacementWithExistingId() throws Exception {
        // Create the ReinsurancePlacement with an existing ID
        reinsurancePlacement.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReinsurancePlacementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reinsurancePlacement)))
            .andExpect(status().isBadRequest());

        // Validate the ReinsurancePlacement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRiPercentageCommissionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reinsurancePlacement.setRiPercentageCommission(null);

        // Create the ReinsurancePlacement, which fails.

        restReinsurancePlacementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reinsurancePlacement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRiPercentageShareIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reinsurancePlacement.setRiPercentageShare(null);

        // Create the ReinsurancePlacement, which fails.

        restReinsurancePlacementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reinsurancePlacement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGrossPremiumHundredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reinsurancePlacement.setGrossPremiumHundred(null);

        // Create the ReinsurancePlacement, which fails.

        restReinsurancePlacementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reinsurancePlacement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReinsurancePlacements() throws Exception {
        // Initialize the database
        insertedReinsurancePlacement = reinsurancePlacementRepository.saveAndFlush(reinsurancePlacement);

        // Get all the reinsurancePlacementList
        restReinsurancePlacementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reinsurancePlacement.getId().intValue())))
            .andExpect(jsonPath("$.[*].riPercentageCommission").value(hasItem(sameNumber(DEFAULT_RI_PERCENTAGE_COMMISSION))))
            .andExpect(jsonPath("$.[*].riPercentageShare").value(hasItem(sameNumber(DEFAULT_RI_PERCENTAGE_SHARE))))
            .andExpect(jsonPath("$.[*].riTotalSumInsured").value(hasItem(sameNumber(DEFAULT_RI_TOTAL_SUM_INSURED))))
            .andExpect(jsonPath("$.[*].riLimitOfLiability").value(hasItem(sameNumber(DEFAULT_RI_LIMIT_OF_LIABILITY))))
            .andExpect(jsonPath("$.[*].grossPremiumHundred").value(hasItem(sameNumber(DEFAULT_GROSS_PREMIUM_HUNDRED))))
            .andExpect(jsonPath("$.[*].riPremium").value(hasItem(sameNumber(DEFAULT_RI_PREMIUM))))
            .andExpect(jsonPath("$.[*].riCommission").value(hasItem(sameNumber(DEFAULT_RI_COMMISSION))))
            .andExpect(jsonPath("$.[*].netDueFromInsurer").value(hasItem(sameNumber(DEFAULT_NET_DUE_FROM_INSURER))));
    }

    @Test
    @Transactional
    void getReinsurancePlacement() throws Exception {
        // Initialize the database
        insertedReinsurancePlacement = reinsurancePlacementRepository.saveAndFlush(reinsurancePlacement);

        // Get the reinsurancePlacement
        restReinsurancePlacementMockMvc
            .perform(get(ENTITY_API_URL_ID, reinsurancePlacement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reinsurancePlacement.getId().intValue()))
            .andExpect(jsonPath("$.riPercentageCommission").value(sameNumber(DEFAULT_RI_PERCENTAGE_COMMISSION)))
            .andExpect(jsonPath("$.riPercentageShare").value(sameNumber(DEFAULT_RI_PERCENTAGE_SHARE)))
            .andExpect(jsonPath("$.riTotalSumInsured").value(sameNumber(DEFAULT_RI_TOTAL_SUM_INSURED)))
            .andExpect(jsonPath("$.riLimitOfLiability").value(sameNumber(DEFAULT_RI_LIMIT_OF_LIABILITY)))
            .andExpect(jsonPath("$.grossPremiumHundred").value(sameNumber(DEFAULT_GROSS_PREMIUM_HUNDRED)))
            .andExpect(jsonPath("$.riPremium").value(sameNumber(DEFAULT_RI_PREMIUM)))
            .andExpect(jsonPath("$.riCommission").value(sameNumber(DEFAULT_RI_COMMISSION)))
            .andExpect(jsonPath("$.netDueFromInsurer").value(sameNumber(DEFAULT_NET_DUE_FROM_INSURER)));
    }

    @Test
    @Transactional
    void getNonExistingReinsurancePlacement() throws Exception {
        // Get the reinsurancePlacement
        restReinsurancePlacementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReinsurancePlacement() throws Exception {
        // Initialize the database
        insertedReinsurancePlacement = reinsurancePlacementRepository.saveAndFlush(reinsurancePlacement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reinsurancePlacement
        ReinsurancePlacement updatedReinsurancePlacement = reinsurancePlacementRepository
            .findById(reinsurancePlacement.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedReinsurancePlacement are not directly saved in db
        em.detach(updatedReinsurancePlacement);
        updatedReinsurancePlacement
            .riPercentageCommission(UPDATED_RI_PERCENTAGE_COMMISSION)
            .riPercentageShare(UPDATED_RI_PERCENTAGE_SHARE)
            .riTotalSumInsured(UPDATED_RI_TOTAL_SUM_INSURED)
            .riLimitOfLiability(UPDATED_RI_LIMIT_OF_LIABILITY)
            .grossPremiumHundred(UPDATED_GROSS_PREMIUM_HUNDRED)
            .riPremium(UPDATED_RI_PREMIUM)
            .riCommission(UPDATED_RI_COMMISSION)
            .netDueFromInsurer(UPDATED_NET_DUE_FROM_INSURER);

        restReinsurancePlacementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReinsurancePlacement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReinsurancePlacement))
            )
            .andExpect(status().isOk());

        // Validate the ReinsurancePlacement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReinsurancePlacementToMatchAllProperties(updatedReinsurancePlacement);
    }

    @Test
    @Transactional
    void putNonExistingReinsurancePlacement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reinsurancePlacement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReinsurancePlacementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reinsurancePlacement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reinsurancePlacement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReinsurancePlacement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReinsurancePlacement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reinsurancePlacement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReinsurancePlacementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reinsurancePlacement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReinsurancePlacement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReinsurancePlacement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reinsurancePlacement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReinsurancePlacementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reinsurancePlacement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReinsurancePlacement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReinsurancePlacementWithPatch() throws Exception {
        // Initialize the database
        insertedReinsurancePlacement = reinsurancePlacementRepository.saveAndFlush(reinsurancePlacement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reinsurancePlacement using partial update
        ReinsurancePlacement partialUpdatedReinsurancePlacement = new ReinsurancePlacement();
        partialUpdatedReinsurancePlacement.setId(reinsurancePlacement.getId());

        partialUpdatedReinsurancePlacement.grossPremiumHundred(UPDATED_GROSS_PREMIUM_HUNDRED);

        restReinsurancePlacementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReinsurancePlacement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReinsurancePlacement))
            )
            .andExpect(status().isOk());

        // Validate the ReinsurancePlacement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReinsurancePlacementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReinsurancePlacement, reinsurancePlacement),
            getPersistedReinsurancePlacement(reinsurancePlacement)
        );
    }

    @Test
    @Transactional
    void fullUpdateReinsurancePlacementWithPatch() throws Exception {
        // Initialize the database
        insertedReinsurancePlacement = reinsurancePlacementRepository.saveAndFlush(reinsurancePlacement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reinsurancePlacement using partial update
        ReinsurancePlacement partialUpdatedReinsurancePlacement = new ReinsurancePlacement();
        partialUpdatedReinsurancePlacement.setId(reinsurancePlacement.getId());

        partialUpdatedReinsurancePlacement
            .riPercentageCommission(UPDATED_RI_PERCENTAGE_COMMISSION)
            .riPercentageShare(UPDATED_RI_PERCENTAGE_SHARE)
            .riTotalSumInsured(UPDATED_RI_TOTAL_SUM_INSURED)
            .riLimitOfLiability(UPDATED_RI_LIMIT_OF_LIABILITY)
            .grossPremiumHundred(UPDATED_GROSS_PREMIUM_HUNDRED)
            .riPremium(UPDATED_RI_PREMIUM)
            .riCommission(UPDATED_RI_COMMISSION)
            .netDueFromInsurer(UPDATED_NET_DUE_FROM_INSURER);

        restReinsurancePlacementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReinsurancePlacement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReinsurancePlacement))
            )
            .andExpect(status().isOk());

        // Validate the ReinsurancePlacement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReinsurancePlacementUpdatableFieldsEquals(
            partialUpdatedReinsurancePlacement,
            getPersistedReinsurancePlacement(partialUpdatedReinsurancePlacement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReinsurancePlacement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reinsurancePlacement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReinsurancePlacementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reinsurancePlacement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reinsurancePlacement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReinsurancePlacement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReinsurancePlacement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reinsurancePlacement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReinsurancePlacementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reinsurancePlacement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReinsurancePlacement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReinsurancePlacement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reinsurancePlacement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReinsurancePlacementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reinsurancePlacement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReinsurancePlacement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReinsurancePlacement() throws Exception {
        // Initialize the database
        insertedReinsurancePlacement = reinsurancePlacementRepository.saveAndFlush(reinsurancePlacement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reinsurancePlacement
        restReinsurancePlacementMockMvc
            .perform(delete(ENTITY_API_URL_ID, reinsurancePlacement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reinsurancePlacementRepository.count();
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

    protected ReinsurancePlacement getPersistedReinsurancePlacement(ReinsurancePlacement reinsurancePlacement) {
        return reinsurancePlacementRepository.findById(reinsurancePlacement.getId()).orElseThrow();
    }

    protected void assertPersistedReinsurancePlacementToMatchAllProperties(ReinsurancePlacement expectedReinsurancePlacement) {
        assertReinsurancePlacementAllPropertiesEquals(
            expectedReinsurancePlacement,
            getPersistedReinsurancePlacement(expectedReinsurancePlacement)
        );
    }

    protected void assertPersistedReinsurancePlacementToMatchUpdatableProperties(ReinsurancePlacement expectedReinsurancePlacement) {
        assertReinsurancePlacementAllUpdatablePropertiesEquals(
            expectedReinsurancePlacement,
            getPersistedReinsurancePlacement(expectedReinsurancePlacement)
        );
    }
}
