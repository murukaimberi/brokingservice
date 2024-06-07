package za.co.afrikatek.brokingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.afrikatek.brokingservice.domain.ShareAsserts.*;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.createUpdateProxyForBean;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.IntegrationTest;
import za.co.afrikatek.brokingservice.domain.BusinessPartner;
import za.co.afrikatek.brokingservice.domain.Share;
import za.co.afrikatek.brokingservice.repository.ShareRepository;
import za.co.afrikatek.brokingservice.service.ShareService;

/**
 * Integration tests for the {@link ShareResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShareResourceIT {

    private static final BigDecimal DEFAULT_SHARE_PERCENTAGE = new BigDecimal(0);
    private static final BigDecimal UPDATED_SHARE_PERCENTAGE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_TOTAL_SUM_INSURED = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_SUM_INSURED = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LIMIT_OF_LIABILITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIMIT_OF_LIABILITY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_GROSS_PREMIUM = new BigDecimal(1);
    private static final BigDecimal UPDATED_GROSS_PREMIUM = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RI_COMMISSION = new BigDecimal(1);
    private static final BigDecimal UPDATED_RI_COMMISSION = new BigDecimal(2);

    private static final BigDecimal DEFAULT_NET_PREMIUM = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_PREMIUM = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BROKERAGE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BROKERAGE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_BROKERAGE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_BROKERAGE_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_NET_PAYABLE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_PAYABLE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/shares";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShareRepository shareRepository;

    @Mock
    private ShareRepository shareRepositoryMock;

    @Mock
    private ShareService shareServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShareMockMvc;

    private Share share;

    private Share insertedShare;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Share createEntity(EntityManager em) {
        Share share = new Share()
            .sharePercentage(DEFAULT_SHARE_PERCENTAGE)
            .totalSumInsured(DEFAULT_TOTAL_SUM_INSURED)
            .limitOfLiability(DEFAULT_LIMIT_OF_LIABILITY)
            .grossPremium(DEFAULT_GROSS_PREMIUM)
            .riCommission(DEFAULT_RI_COMMISSION)
            .netPremium(DEFAULT_NET_PREMIUM)
            .brokerage(DEFAULT_BROKERAGE)
            .brokerageAmount(DEFAULT_BROKERAGE_AMOUNT)
            .netPayable(DEFAULT_NET_PAYABLE);
        // Add required entity
        BusinessPartner businessPartner;
        if (TestUtil.findAll(em, BusinessPartner.class).isEmpty()) {
            businessPartner = BusinessPartnerResourceIT.createEntity(em);
            em.persist(businessPartner);
            em.flush();
        } else {
            businessPartner = TestUtil.findAll(em, BusinessPartner.class).get(0);
        }
        share.setReInsurer(businessPartner);
        return share;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Share createUpdatedEntity(EntityManager em) {
        Share share = new Share()
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .totalSumInsured(UPDATED_TOTAL_SUM_INSURED)
            .limitOfLiability(UPDATED_LIMIT_OF_LIABILITY)
            .grossPremium(UPDATED_GROSS_PREMIUM)
            .riCommission(UPDATED_RI_COMMISSION)
            .netPremium(UPDATED_NET_PREMIUM)
            .brokerage(UPDATED_BROKERAGE)
            .brokerageAmount(UPDATED_BROKERAGE_AMOUNT)
            .netPayable(UPDATED_NET_PAYABLE);
        // Add required entity
        BusinessPartner businessPartner;
        if (TestUtil.findAll(em, BusinessPartner.class).isEmpty()) {
            businessPartner = BusinessPartnerResourceIT.createUpdatedEntity(em);
            em.persist(businessPartner);
            em.flush();
        } else {
            businessPartner = TestUtil.findAll(em, BusinessPartner.class).get(0);
        }
        share.setReInsurer(businessPartner);
        return share;
    }

    @BeforeEach
    public void initTest() {
        share = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedShare != null) {
            shareRepository.delete(insertedShare);
            insertedShare = null;
        }
    }

    @Test
    @Transactional
    void createShare() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Share
        var returnedShare = om.readValue(
            restShareMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(share)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Share.class
        );

        // Validate the Share in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShareUpdatableFieldsEquals(returnedShare, getPersistedShare(returnedShare));

        insertedShare = returnedShare;
    }

    @Test
    @Transactional
    void createShareWithExistingId() throws Exception {
        // Create the Share with an existing ID
        share.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(share)))
            .andExpect(status().isBadRequest());

        // Validate the Share in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSharePercentageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        share.setSharePercentage(null);

        // Create the Share, which fails.

        restShareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(share)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBrokerageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        share.setBrokerage(null);

        // Create the Share, which fails.

        restShareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(share)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShares() throws Exception {
        // Initialize the database
        insertedShare = shareRepository.saveAndFlush(share);

        // Get all the shareList
        restShareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(share.getId().intValue())))
            .andExpect(jsonPath("$.[*].sharePercentage").value(hasItem(sameNumber(DEFAULT_SHARE_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].totalSumInsured").value(hasItem(sameNumber(DEFAULT_TOTAL_SUM_INSURED))))
            .andExpect(jsonPath("$.[*].limitOfLiability").value(hasItem(sameNumber(DEFAULT_LIMIT_OF_LIABILITY))))
            .andExpect(jsonPath("$.[*].grossPremium").value(hasItem(sameNumber(DEFAULT_GROSS_PREMIUM))))
            .andExpect(jsonPath("$.[*].riCommission").value(hasItem(sameNumber(DEFAULT_RI_COMMISSION))))
            .andExpect(jsonPath("$.[*].netPremium").value(hasItem(sameNumber(DEFAULT_NET_PREMIUM))))
            .andExpect(jsonPath("$.[*].brokerage").value(hasItem(sameNumber(DEFAULT_BROKERAGE))))
            .andExpect(jsonPath("$.[*].brokerageAmount").value(hasItem(sameNumber(DEFAULT_BROKERAGE_AMOUNT))))
            .andExpect(jsonPath("$.[*].netPayable").value(hasItem(sameNumber(DEFAULT_NET_PAYABLE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSharesWithEagerRelationshipsIsEnabled() throws Exception {
        when(shareServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShareMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shareServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSharesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shareServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShareMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shareRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShare() throws Exception {
        // Initialize the database
        insertedShare = shareRepository.saveAndFlush(share);

        // Get the share
        restShareMockMvc
            .perform(get(ENTITY_API_URL_ID, share.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(share.getId().intValue()))
            .andExpect(jsonPath("$.sharePercentage").value(sameNumber(DEFAULT_SHARE_PERCENTAGE)))
            .andExpect(jsonPath("$.totalSumInsured").value(sameNumber(DEFAULT_TOTAL_SUM_INSURED)))
            .andExpect(jsonPath("$.limitOfLiability").value(sameNumber(DEFAULT_LIMIT_OF_LIABILITY)))
            .andExpect(jsonPath("$.grossPremium").value(sameNumber(DEFAULT_GROSS_PREMIUM)))
            .andExpect(jsonPath("$.riCommission").value(sameNumber(DEFAULT_RI_COMMISSION)))
            .andExpect(jsonPath("$.netPremium").value(sameNumber(DEFAULT_NET_PREMIUM)))
            .andExpect(jsonPath("$.brokerage").value(sameNumber(DEFAULT_BROKERAGE)))
            .andExpect(jsonPath("$.brokerageAmount").value(sameNumber(DEFAULT_BROKERAGE_AMOUNT)))
            .andExpect(jsonPath("$.netPayable").value(sameNumber(DEFAULT_NET_PAYABLE)));
    }

    @Test
    @Transactional
    void getNonExistingShare() throws Exception {
        // Get the share
        restShareMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShare() throws Exception {
        // Initialize the database
        insertedShare = shareRepository.saveAndFlush(share);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the share
        Share updatedShare = shareRepository.findById(share.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShare are not directly saved in db
        em.detach(updatedShare);
        updatedShare
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .totalSumInsured(UPDATED_TOTAL_SUM_INSURED)
            .limitOfLiability(UPDATED_LIMIT_OF_LIABILITY)
            .grossPremium(UPDATED_GROSS_PREMIUM)
            .riCommission(UPDATED_RI_COMMISSION)
            .netPremium(UPDATED_NET_PREMIUM)
            .brokerage(UPDATED_BROKERAGE)
            .brokerageAmount(UPDATED_BROKERAGE_AMOUNT)
            .netPayable(UPDATED_NET_PAYABLE);

        restShareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShare.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShare))
            )
            .andExpect(status().isOk());

        // Validate the Share in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShareToMatchAllProperties(updatedShare);
    }

    @Test
    @Transactional
    void putNonExistingShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        share.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareMockMvc
            .perform(put(ENTITY_API_URL_ID, share.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(share)))
            .andExpect(status().isBadRequest());

        // Validate the Share in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        share.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(share))
            )
            .andExpect(status().isBadRequest());

        // Validate the Share in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        share.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(share)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Share in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShareWithPatch() throws Exception {
        // Initialize the database
        insertedShare = shareRepository.saveAndFlush(share);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the share using partial update
        Share partialUpdatedShare = new Share();
        partialUpdatedShare.setId(share.getId());

        partialUpdatedShare
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .limitOfLiability(UPDATED_LIMIT_OF_LIABILITY)
            .riCommission(UPDATED_RI_COMMISSION)
            .netPremium(UPDATED_NET_PREMIUM)
            .brokerage(UPDATED_BROKERAGE)
            .brokerageAmount(UPDATED_BROKERAGE_AMOUNT)
            .netPayable(UPDATED_NET_PAYABLE);

        restShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShare.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShare))
            )
            .andExpect(status().isOk());

        // Validate the Share in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShareUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedShare, share), getPersistedShare(share));
    }

    @Test
    @Transactional
    void fullUpdateShareWithPatch() throws Exception {
        // Initialize the database
        insertedShare = shareRepository.saveAndFlush(share);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the share using partial update
        Share partialUpdatedShare = new Share();
        partialUpdatedShare.setId(share.getId());

        partialUpdatedShare
            .sharePercentage(UPDATED_SHARE_PERCENTAGE)
            .totalSumInsured(UPDATED_TOTAL_SUM_INSURED)
            .limitOfLiability(UPDATED_LIMIT_OF_LIABILITY)
            .grossPremium(UPDATED_GROSS_PREMIUM)
            .riCommission(UPDATED_RI_COMMISSION)
            .netPremium(UPDATED_NET_PREMIUM)
            .brokerage(UPDATED_BROKERAGE)
            .brokerageAmount(UPDATED_BROKERAGE_AMOUNT)
            .netPayable(UPDATED_NET_PAYABLE);

        restShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShare.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShare))
            )
            .andExpect(status().isOk());

        // Validate the Share in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShareUpdatableFieldsEquals(partialUpdatedShare, getPersistedShare(partialUpdatedShare));
    }

    @Test
    @Transactional
    void patchNonExistingShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        share.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, share.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(share))
            )
            .andExpect(status().isBadRequest());

        // Validate the Share in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        share.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(share))
            )
            .andExpect(status().isBadRequest());

        // Validate the Share in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        share.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(share)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Share in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShare() throws Exception {
        // Initialize the database
        insertedShare = shareRepository.saveAndFlush(share);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the share
        restShareMockMvc
            .perform(delete(ENTITY_API_URL_ID, share.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shareRepository.count();
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

    protected Share getPersistedShare(Share share) {
        return shareRepository.findById(share.getId()).orElseThrow();
    }

    protected void assertPersistedShareToMatchAllProperties(Share expectedShare) {
        assertShareAllPropertiesEquals(expectedShare, getPersistedShare(expectedShare));
    }

    protected void assertPersistedShareToMatchUpdatableProperties(Share expectedShare) {
        assertShareAllUpdatablePropertiesEquals(expectedShare, getPersistedShare(expectedShare));
    }
}
