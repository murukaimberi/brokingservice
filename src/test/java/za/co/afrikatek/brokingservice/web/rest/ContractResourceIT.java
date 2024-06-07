package za.co.afrikatek.brokingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.afrikatek.brokingservice.domain.ContractAsserts.*;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.createUpdateProxyForBean;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.sameInstant;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.IntegrationTest;
import za.co.afrikatek.brokingservice.domain.BusinessPartner;
import za.co.afrikatek.brokingservice.domain.ClassOfBusiness;
import za.co.afrikatek.brokingservice.domain.Contract;
import za.co.afrikatek.brokingservice.domain.Country;
import za.co.afrikatek.brokingservice.domain.SubClassOfBusiness;
import za.co.afrikatek.brokingservice.domain.enumeration.ContractStatus;
import za.co.afrikatek.brokingservice.domain.enumeration.ContractSubType;
import za.co.afrikatek.brokingservice.domain.enumeration.ContractType;
import za.co.afrikatek.brokingservice.repository.ContractRepository;
import za.co.afrikatek.brokingservice.service.ContractService;

/**
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final ContractType DEFAULT_TYPE = ContractType.FACULTATIVE;
    private static final ContractType UPDATED_TYPE = ContractType.TREATY;

    private static final ContractSubType DEFAULT_SUB_TYPE = ContractSubType.PROPORTIONAL;
    private static final ContractSubType UPDATED_SUB_TYPE = ContractSubType.NON_PROPORTIONAL;

    private static final ZonedDateTime DEFAULT_INCEPTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INCEPTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EXPIRY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_SUM_INSURED = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_SUM_INSURED = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LIMIT_OF_LIABILITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIMIT_OF_LIABILITY = new BigDecimal(2);

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final ContractStatus DEFAULT_STATUS = ContractStatus.CREATED;
    private static final ContractStatus UPDATED_STATUS = ContractStatus.CREATED_PLACEMENT;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractRepository contractRepository;

    @Mock
    private ContractRepository contractRepositoryMock;

    @Mock
    private ContractService contractServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    private Contract insertedContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .type(DEFAULT_TYPE)
            .subType(DEFAULT_SUB_TYPE)
            .inception(DEFAULT_INCEPTION)
            .expiry(DEFAULT_EXPIRY)
            .currency(DEFAULT_CURRENCY)
            .totalSumInsured(DEFAULT_TOTAL_SUM_INSURED)
            .limitOfLiability(DEFAULT_LIMIT_OF_LIABILITY)
            .uuid(DEFAULT_UUID)
            .status(DEFAULT_STATUS)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        BusinessPartner businessPartner;
        if (TestUtil.findAll(em, BusinessPartner.class).isEmpty()) {
            businessPartner = BusinessPartnerResourceIT.createEntity(em);
            em.persist(businessPartner);
            em.flush();
        } else {
            businessPartner = TestUtil.findAll(em, BusinessPartner.class).get(0);
        }
        contract.setInsured(businessPartner);
        // Add required entity
        contract.setInsurer(businessPartner);
        // Add required entity
        contract.setBroker(businessPartner);
        // Add required entity
        contract.getReinsurers().add(businessPartner);
        // Add required entity
        ClassOfBusiness classOfBusiness;
        if (TestUtil.findAll(em, ClassOfBusiness.class).isEmpty()) {
            classOfBusiness = ClassOfBusinessResourceIT.createEntity(em);
            em.persist(classOfBusiness);
            em.flush();
        } else {
            classOfBusiness = TestUtil.findAll(em, ClassOfBusiness.class).get(0);
        }
        contract.setClassOfBusiness(classOfBusiness);
        // Add required entity
        SubClassOfBusiness subClassOfBusiness;
        if (TestUtil.findAll(em, SubClassOfBusiness.class).isEmpty()) {
            subClassOfBusiness = SubClassOfBusinessResourceIT.createEntity(em);
            em.persist(subClassOfBusiness);
            em.flush();
        } else {
            subClassOfBusiness = TestUtil.findAll(em, SubClassOfBusiness.class).get(0);
        }
        contract.setSubClassOfBusiness(subClassOfBusiness);
        // Add required entity
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        contract.setCountry(country);
        return contract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity(EntityManager em) {
        Contract contract = new Contract()
            .type(UPDATED_TYPE)
            .subType(UPDATED_SUB_TYPE)
            .inception(UPDATED_INCEPTION)
            .expiry(UPDATED_EXPIRY)
            .currency(UPDATED_CURRENCY)
            .totalSumInsured(UPDATED_TOTAL_SUM_INSURED)
            .limitOfLiability(UPDATED_LIMIT_OF_LIABILITY)
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .active(UPDATED_ACTIVE);
        // Add required entity
        BusinessPartner businessPartner;
        if (TestUtil.findAll(em, BusinessPartner.class).isEmpty()) {
            businessPartner = BusinessPartnerResourceIT.createUpdatedEntity(em);
            em.persist(businessPartner);
            em.flush();
        } else {
            businessPartner = TestUtil.findAll(em, BusinessPartner.class).get(0);
        }
        contract.setInsured(businessPartner);
        // Add required entity
        contract.setInsurer(businessPartner);
        // Add required entity
        contract.setBroker(businessPartner);
        // Add required entity
        contract.getReinsurers().add(businessPartner);
        // Add required entity
        ClassOfBusiness classOfBusiness;
        if (TestUtil.findAll(em, ClassOfBusiness.class).isEmpty()) {
            classOfBusiness = ClassOfBusinessResourceIT.createUpdatedEntity(em);
            em.persist(classOfBusiness);
            em.flush();
        } else {
            classOfBusiness = TestUtil.findAll(em, ClassOfBusiness.class).get(0);
        }
        contract.setClassOfBusiness(classOfBusiness);
        // Add required entity
        SubClassOfBusiness subClassOfBusiness;
        if (TestUtil.findAll(em, SubClassOfBusiness.class).isEmpty()) {
            subClassOfBusiness = SubClassOfBusinessResourceIT.createUpdatedEntity(em);
            em.persist(subClassOfBusiness);
            em.flush();
        } else {
            subClassOfBusiness = TestUtil.findAll(em, SubClassOfBusiness.class).get(0);
        }
        contract.setSubClassOfBusiness(subClassOfBusiness);
        // Add required entity
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createUpdatedEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        contract.setCountry(country);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedContract != null) {
            contractRepository.delete(insertedContract);
            insertedContract = null;
        }
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Contract
        var returnedContract = om.readValue(
            restContractMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Contract.class
        );

        // Validate the Contract in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContractUpdatableFieldsEquals(returnedContract, getPersistedContract(returnedContract));

        insertedContract = returnedContract;
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setType(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setSubType(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInceptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setInception(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setExpiry(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setCurrency(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalSumInsuredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setTotalSumInsured(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLimitOfLiabilityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setLimitOfLiability(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contract.setStatus(null);

        // Create the Contract, which fails.

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subType").value(hasItem(DEFAULT_SUB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].inception").value(hasItem(sameInstant(DEFAULT_INCEPTION))))
            .andExpect(jsonPath("$.[*].expiry").value(hasItem(sameInstant(DEFAULT_EXPIRY))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].totalSumInsured").value(hasItem(sameNumber(DEFAULT_TOTAL_SUM_INSURED))))
            .andExpect(jsonPath("$.[*].limitOfLiability").value(hasItem(sameNumber(DEFAULT_LIMIT_OF_LIABILITY))))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contractServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contractRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.subType").value(DEFAULT_SUB_TYPE.toString()))
            .andExpect(jsonPath("$.inception").value(sameInstant(DEFAULT_INCEPTION)))
            .andExpect(jsonPath("$.expiry").value(sameInstant(DEFAULT_EXPIRY)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.totalSumInsured").value(sameNumber(DEFAULT_TOTAL_SUM_INSURED)))
            .andExpect(jsonPath("$.limitOfLiability").value(sameNumber(DEFAULT_LIMIT_OF_LIABILITY)))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .type(UPDATED_TYPE)
            .subType(UPDATED_SUB_TYPE)
            .inception(UPDATED_INCEPTION)
            .expiry(UPDATED_EXPIRY)
            .currency(UPDATED_CURRENCY)
            .totalSumInsured(UPDATED_TOTAL_SUM_INSURED)
            .limitOfLiability(UPDATED_LIMIT_OF_LIABILITY)
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .active(UPDATED_ACTIVE);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractToMatchAllProperties(updatedContract);
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contract.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract.inception(UPDATED_INCEPTION).uuid(UPDATED_UUID).status(UPDATED_STATUS);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContract, contract), getPersistedContract(contract));
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .type(UPDATED_TYPE)
            .subType(UPDATED_SUB_TYPE)
            .inception(UPDATED_INCEPTION)
            .expiry(UPDATED_EXPIRY)
            .currency(UPDATED_CURRENCY)
            .totalSumInsured(UPDATED_TOTAL_SUM_INSURED)
            .limitOfLiability(UPDATED_LIMIT_OF_LIABILITY)
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .active(UPDATED_ACTIVE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(partialUpdatedContract, getPersistedContract(partialUpdatedContract));
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contractRepository.count();
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

    protected Contract getPersistedContract(Contract contract) {
        return contractRepository.findById(contract.getId()).orElseThrow();
    }

    protected void assertPersistedContractToMatchAllProperties(Contract expectedContract) {
        assertContractAllPropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }

    protected void assertPersistedContractToMatchUpdatableProperties(Contract expectedContract) {
        assertContractAllUpdatablePropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }
}
