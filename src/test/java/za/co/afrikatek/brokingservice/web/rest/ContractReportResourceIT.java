package za.co.afrikatek.brokingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.afrikatek.brokingservice.domain.ContractReportAsserts.*;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.createUpdateProxyForBean;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.sameInstant;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
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
import za.co.afrikatek.brokingservice.domain.ContractReport;
import za.co.afrikatek.brokingservice.repository.ContractReportRepository;

/**
 * Integration tests for the {@link ContractReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractReportResourceIT {

    private static final byte[] DEFAULT_CONTRACT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTRACT_DOCUMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTRACT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTRACT_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/contract-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractReportRepository contractReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractReportMockMvc;

    private ContractReport contractReport;

    private ContractReport insertedContractReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractReport createEntity(EntityManager em) {
        ContractReport contractReport = new ContractReport()
            .contractDocument(DEFAULT_CONTRACT_DOCUMENT)
            .contractDocumentContentType(DEFAULT_CONTRACT_DOCUMENT_CONTENT_TYPE)
            .createdDate(DEFAULT_CREATED_DATE);
        return contractReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractReport createUpdatedEntity(EntityManager em) {
        ContractReport contractReport = new ContractReport()
            .contractDocument(UPDATED_CONTRACT_DOCUMENT)
            .contractDocumentContentType(UPDATED_CONTRACT_DOCUMENT_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE);
        return contractReport;
    }

    @BeforeEach
    public void initTest() {
        contractReport = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedContractReport != null) {
            contractReportRepository.delete(insertedContractReport);
            insertedContractReport = null;
        }
    }

    @Test
    @Transactional
    void createContractReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContractReport
        var returnedContractReport = om.readValue(
            restContractReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractReport)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContractReport.class
        );

        // Validate the ContractReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContractReportUpdatableFieldsEquals(returnedContractReport, getPersistedContractReport(returnedContractReport));

        insertedContractReport = returnedContractReport;
    }

    @Test
    @Transactional
    void createContractReportWithExistingId() throws Exception {
        // Create the ContractReport with an existing ID
        contractReport.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractReport)))
            .andExpect(status().isBadRequest());

        // Validate the ContractReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractReport.setCreatedDate(null);

        // Create the ContractReport, which fails.

        restContractReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractReport)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContractReports() throws Exception {
        // Initialize the database
        insertedContractReport = contractReportRepository.saveAndFlush(contractReport);

        // Get all the contractReportList
        restContractReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractDocumentContentType").value(hasItem(DEFAULT_CONTRACT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contractDocument").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTRACT_DOCUMENT))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))));
    }

    @Test
    @Transactional
    void getContractReport() throws Exception {
        // Initialize the database
        insertedContractReport = contractReportRepository.saveAndFlush(contractReport);

        // Get the contractReport
        restContractReportMockMvc
            .perform(get(ENTITY_API_URL_ID, contractReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractReport.getId().intValue()))
            .andExpect(jsonPath("$.contractDocumentContentType").value(DEFAULT_CONTRACT_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.contractDocument").value(Base64.getEncoder().encodeToString(DEFAULT_CONTRACT_DOCUMENT)))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingContractReport() throws Exception {
        // Get the contractReport
        restContractReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContractReport() throws Exception {
        // Initialize the database
        insertedContractReport = contractReportRepository.saveAndFlush(contractReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractReport
        ContractReport updatedContractReport = contractReportRepository.findById(contractReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContractReport are not directly saved in db
        em.detach(updatedContractReport);
        updatedContractReport
            .contractDocument(UPDATED_CONTRACT_DOCUMENT)
            .contractDocumentContentType(UPDATED_CONTRACT_DOCUMENT_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE);

        restContractReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContractReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContractReport))
            )
            .andExpect(status().isOk());

        // Validate the ContractReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractReportToMatchAllProperties(updatedContractReport);
    }

    @Test
    @Transactional
    void putNonExistingContractReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractReport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractReportWithPatch() throws Exception {
        // Initialize the database
        insertedContractReport = contractReportRepository.saveAndFlush(contractReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractReport using partial update
        ContractReport partialUpdatedContractReport = new ContractReport();
        partialUpdatedContractReport.setId(contractReport.getId());

        restContractReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractReport))
            )
            .andExpect(status().isOk());

        // Validate the ContractReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContractReport, contractReport),
            getPersistedContractReport(contractReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateContractReportWithPatch() throws Exception {
        // Initialize the database
        insertedContractReport = contractReportRepository.saveAndFlush(contractReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractReport using partial update
        ContractReport partialUpdatedContractReport = new ContractReport();
        partialUpdatedContractReport.setId(contractReport.getId());

        partialUpdatedContractReport
            .contractDocument(UPDATED_CONTRACT_DOCUMENT)
            .contractDocumentContentType(UPDATED_CONTRACT_DOCUMENT_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE);

        restContractReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractReport))
            )
            .andExpect(status().isOk());

        // Validate the ContractReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractReportUpdatableFieldsEquals(partialUpdatedContractReport, getPersistedContractReport(partialUpdatedContractReport));
    }

    @Test
    @Transactional
    void patchNonExistingContractReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contractReport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContractReport() throws Exception {
        // Initialize the database
        insertedContractReport = contractReportRepository.saveAndFlush(contractReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contractReport
        restContractReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contractReportRepository.count();
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

    protected ContractReport getPersistedContractReport(ContractReport contractReport) {
        return contractReportRepository.findById(contractReport.getId()).orElseThrow();
    }

    protected void assertPersistedContractReportToMatchAllProperties(ContractReport expectedContractReport) {
        assertContractReportAllPropertiesEquals(expectedContractReport, getPersistedContractReport(expectedContractReport));
    }

    protected void assertPersistedContractReportToMatchUpdatableProperties(ContractReport expectedContractReport) {
        assertContractReportAllUpdatablePropertiesEquals(expectedContractReport, getPersistedContractReport(expectedContractReport));
    }
}
