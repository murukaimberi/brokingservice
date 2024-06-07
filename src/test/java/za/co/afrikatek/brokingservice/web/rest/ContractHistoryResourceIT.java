package za.co.afrikatek.brokingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.afrikatek.brokingservice.domain.ContractHistoryAsserts.*;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.createUpdateProxyForBean;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.sameInstant;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import za.co.afrikatek.brokingservice.domain.ContractHistory;
import za.co.afrikatek.brokingservice.domain.User;
import za.co.afrikatek.brokingservice.repository.ContractHistoryRepository;
import za.co.afrikatek.brokingservice.repository.UserRepository;
import za.co.afrikatek.brokingservice.service.ContractHistoryService;

/**
 * Integration tests for the {@link ContractHistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContractHistoryResourceIT {

    private static final ZonedDateTime DEFAULT_CONTRACT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CONTRACT_ACTIVE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_ACTIVE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CONTRACT_IN_ACTIVE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_IN_ACTIVE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CONTRACT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_CONTRACT_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CHANGE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contract-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractHistoryRepository contractHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ContractHistoryRepository contractHistoryRepositoryMock;

    @Mock
    private ContractHistoryService contractHistoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractHistoryMockMvc;

    private ContractHistory contractHistory;

    private ContractHistory insertedContractHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractHistory createEntity(EntityManager em) {
        ContractHistory contractHistory = new ContractHistory()
            .contractCreatedDate(DEFAULT_CONTRACT_CREATED_DATE)
            .contractActiveDate(DEFAULT_CONTRACT_ACTIVE_DATE)
            .contractInActiveDate(DEFAULT_CONTRACT_IN_ACTIVE_DATE)
            .contractLastModifiedDate(DEFAULT_CONTRACT_LAST_MODIFIED_DATE)
            .changeDescription(DEFAULT_CHANGE_DESCRIPTION);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        contractHistory.setUpdated(user);
        return contractHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractHistory createUpdatedEntity(EntityManager em) {
        ContractHistory contractHistory = new ContractHistory()
            .contractCreatedDate(UPDATED_CONTRACT_CREATED_DATE)
            .contractActiveDate(UPDATED_CONTRACT_ACTIVE_DATE)
            .contractInActiveDate(UPDATED_CONTRACT_IN_ACTIVE_DATE)
            .contractLastModifiedDate(UPDATED_CONTRACT_LAST_MODIFIED_DATE)
            .changeDescription(UPDATED_CHANGE_DESCRIPTION);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        contractHistory.setUpdated(user);
        return contractHistory;
    }

    @BeforeEach
    public void initTest() {
        contractHistory = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedContractHistory != null) {
            contractHistoryRepository.delete(insertedContractHistory);
            insertedContractHistory = null;
        }
    }

    @Test
    @Transactional
    void createContractHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContractHistory
        var returnedContractHistory = om.readValue(
            restContractHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContractHistory.class
        );

        // Validate the ContractHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContractHistoryUpdatableFieldsEquals(returnedContractHistory, getPersistedContractHistory(returnedContractHistory));

        insertedContractHistory = returnedContractHistory;
    }

    @Test
    @Transactional
    void createContractHistoryWithExistingId() throws Exception {
        // Create the ContractHistory with an existing ID
        contractHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractHistory)))
            .andExpect(status().isBadRequest());

        // Validate the ContractHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContractCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractHistory.setContractCreatedDate(null);

        // Create the ContractHistory, which fails.

        restContractHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractActiveDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractHistory.setContractActiveDate(null);

        // Create the ContractHistory, which fails.

        restContractHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContractHistories() throws Exception {
        // Initialize the database
        insertedContractHistory = contractHistoryRepository.saveAndFlush(contractHistory);

        // Get all the contractHistoryList
        restContractHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractCreatedDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].contractActiveDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_ACTIVE_DATE))))
            .andExpect(jsonPath("$.[*].contractInActiveDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_IN_ACTIVE_DATE))))
            .andExpect(jsonPath("$.[*].contractLastModifiedDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_LAST_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].changeDescription").value(hasItem(DEFAULT_CHANGE_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(contractHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contractHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contractHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contractHistoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContractHistory() throws Exception {
        // Initialize the database
        insertedContractHistory = contractHistoryRepository.saveAndFlush(contractHistory);

        // Get the contractHistory
        restContractHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, contractHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractHistory.getId().intValue()))
            .andExpect(jsonPath("$.contractCreatedDate").value(sameInstant(DEFAULT_CONTRACT_CREATED_DATE)))
            .andExpect(jsonPath("$.contractActiveDate").value(sameInstant(DEFAULT_CONTRACT_ACTIVE_DATE)))
            .andExpect(jsonPath("$.contractInActiveDate").value(sameInstant(DEFAULT_CONTRACT_IN_ACTIVE_DATE)))
            .andExpect(jsonPath("$.contractLastModifiedDate").value(sameInstant(DEFAULT_CONTRACT_LAST_MODIFIED_DATE)))
            .andExpect(jsonPath("$.changeDescription").value(DEFAULT_CHANGE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContractHistory() throws Exception {
        // Get the contractHistory
        restContractHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContractHistory() throws Exception {
        // Initialize the database
        insertedContractHistory = contractHistoryRepository.saveAndFlush(contractHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractHistory
        ContractHistory updatedContractHistory = contractHistoryRepository.findById(contractHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContractHistory are not directly saved in db
        em.detach(updatedContractHistory);
        updatedContractHistory
            .contractCreatedDate(UPDATED_CONTRACT_CREATED_DATE)
            .contractActiveDate(UPDATED_CONTRACT_ACTIVE_DATE)
            .contractInActiveDate(UPDATED_CONTRACT_IN_ACTIVE_DATE)
            .contractLastModifiedDate(UPDATED_CONTRACT_LAST_MODIFIED_DATE)
            .changeDescription(UPDATED_CHANGE_DESCRIPTION);

        restContractHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContractHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContractHistory))
            )
            .andExpect(status().isOk());

        // Validate the ContractHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractHistoryToMatchAllProperties(updatedContractHistory);
    }

    @Test
    @Transactional
    void putNonExistingContractHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedContractHistory = contractHistoryRepository.saveAndFlush(contractHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractHistory using partial update
        ContractHistory partialUpdatedContractHistory = new ContractHistory();
        partialUpdatedContractHistory.setId(contractHistory.getId());

        partialUpdatedContractHistory.changeDescription(UPDATED_CHANGE_DESCRIPTION);

        restContractHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractHistory))
            )
            .andExpect(status().isOk());

        // Validate the ContractHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContractHistory, contractHistory),
            getPersistedContractHistory(contractHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateContractHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedContractHistory = contractHistoryRepository.saveAndFlush(contractHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractHistory using partial update
        ContractHistory partialUpdatedContractHistory = new ContractHistory();
        partialUpdatedContractHistory.setId(contractHistory.getId());

        partialUpdatedContractHistory
            .contractCreatedDate(UPDATED_CONTRACT_CREATED_DATE)
            .contractActiveDate(UPDATED_CONTRACT_ACTIVE_DATE)
            .contractInActiveDate(UPDATED_CONTRACT_IN_ACTIVE_DATE)
            .contractLastModifiedDate(UPDATED_CONTRACT_LAST_MODIFIED_DATE)
            .changeDescription(UPDATED_CHANGE_DESCRIPTION);

        restContractHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractHistory))
            )
            .andExpect(status().isOk());

        // Validate the ContractHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractHistoryUpdatableFieldsEquals(
            partialUpdatedContractHistory,
            getPersistedContractHistory(partialUpdatedContractHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingContractHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contractHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContractHistory() throws Exception {
        // Initialize the database
        insertedContractHistory = contractHistoryRepository.saveAndFlush(contractHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contractHistory
        restContractHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contractHistoryRepository.count();
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

    protected ContractHistory getPersistedContractHistory(ContractHistory contractHistory) {
        return contractHistoryRepository.findById(contractHistory.getId()).orElseThrow();
    }

    protected void assertPersistedContractHistoryToMatchAllProperties(ContractHistory expectedContractHistory) {
        assertContractHistoryAllPropertiesEquals(expectedContractHistory, getPersistedContractHistory(expectedContractHistory));
    }

    protected void assertPersistedContractHistoryToMatchUpdatableProperties(ContractHistory expectedContractHistory) {
        assertContractHistoryAllUpdatablePropertiesEquals(expectedContractHistory, getPersistedContractHistory(expectedContractHistory));
    }
}
