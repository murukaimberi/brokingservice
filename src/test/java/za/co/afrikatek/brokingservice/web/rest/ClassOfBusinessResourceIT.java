package za.co.afrikatek.brokingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.afrikatek.brokingservice.domain.ClassOfBusinessAsserts.*;
import static za.co.afrikatek.brokingservice.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import za.co.afrikatek.brokingservice.domain.ClassOfBusiness;
import za.co.afrikatek.brokingservice.domain.InsuranceType;
import za.co.afrikatek.brokingservice.repository.ClassOfBusinessRepository;
import za.co.afrikatek.brokingservice.service.ClassOfBusinessService;

/**
 * Integration tests for the {@link ClassOfBusinessResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClassOfBusinessResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/class-of-businesses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClassOfBusinessRepository classOfBusinessRepository;

    @Mock
    private ClassOfBusinessRepository classOfBusinessRepositoryMock;

    @Mock
    private ClassOfBusinessService classOfBusinessServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassOfBusinessMockMvc;

    private ClassOfBusiness classOfBusiness;

    private ClassOfBusiness insertedClassOfBusiness;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassOfBusiness createEntity(EntityManager em) {
        ClassOfBusiness classOfBusiness = new ClassOfBusiness().name(DEFAULT_NAME);
        // Add required entity
        InsuranceType insuranceType;
        if (TestUtil.findAll(em, InsuranceType.class).isEmpty()) {
            insuranceType = InsuranceTypeResourceIT.createEntity(em);
            em.persist(insuranceType);
            em.flush();
        } else {
            insuranceType = TestUtil.findAll(em, InsuranceType.class).get(0);
        }
        classOfBusiness.setInsuranceType(insuranceType);
        return classOfBusiness;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassOfBusiness createUpdatedEntity(EntityManager em) {
        ClassOfBusiness classOfBusiness = new ClassOfBusiness().name(UPDATED_NAME);
        // Add required entity
        InsuranceType insuranceType;
        if (TestUtil.findAll(em, InsuranceType.class).isEmpty()) {
            insuranceType = InsuranceTypeResourceIT.createUpdatedEntity(em);
            em.persist(insuranceType);
            em.flush();
        } else {
            insuranceType = TestUtil.findAll(em, InsuranceType.class).get(0);
        }
        classOfBusiness.setInsuranceType(insuranceType);
        return classOfBusiness;
    }

    @BeforeEach
    public void initTest() {
        classOfBusiness = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedClassOfBusiness != null) {
            classOfBusinessRepository.delete(insertedClassOfBusiness);
            insertedClassOfBusiness = null;
        }
    }

    @Test
    @Transactional
    void createClassOfBusiness() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClassOfBusiness
        var returnedClassOfBusiness = om.readValue(
            restClassOfBusinessMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classOfBusiness)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClassOfBusiness.class
        );

        // Validate the ClassOfBusiness in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertClassOfBusinessUpdatableFieldsEquals(returnedClassOfBusiness, getPersistedClassOfBusiness(returnedClassOfBusiness));

        insertedClassOfBusiness = returnedClassOfBusiness;
    }

    @Test
    @Transactional
    void createClassOfBusinessWithExistingId() throws Exception {
        // Create the ClassOfBusiness with an existing ID
        classOfBusiness.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassOfBusinessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classOfBusiness)))
            .andExpect(status().isBadRequest());

        // Validate the ClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClassOfBusinesses() throws Exception {
        // Initialize the database
        insertedClassOfBusiness = classOfBusinessRepository.saveAndFlush(classOfBusiness);

        // Get all the classOfBusinessList
        restClassOfBusinessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classOfBusiness.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassOfBusinessesWithEagerRelationshipsIsEnabled() throws Exception {
        when(classOfBusinessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassOfBusinessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classOfBusinessServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassOfBusinessesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(classOfBusinessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassOfBusinessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(classOfBusinessRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getClassOfBusiness() throws Exception {
        // Initialize the database
        insertedClassOfBusiness = classOfBusinessRepository.saveAndFlush(classOfBusiness);

        // Get the classOfBusiness
        restClassOfBusinessMockMvc
            .perform(get(ENTITY_API_URL_ID, classOfBusiness.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classOfBusiness.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingClassOfBusiness() throws Exception {
        // Get the classOfBusiness
        restClassOfBusinessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClassOfBusiness() throws Exception {
        // Initialize the database
        insertedClassOfBusiness = classOfBusinessRepository.saveAndFlush(classOfBusiness);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classOfBusiness
        ClassOfBusiness updatedClassOfBusiness = classOfBusinessRepository.findById(classOfBusiness.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClassOfBusiness are not directly saved in db
        em.detach(updatedClassOfBusiness);
        updatedClassOfBusiness.name(UPDATED_NAME);

        restClassOfBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClassOfBusiness.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedClassOfBusiness))
            )
            .andExpect(status().isOk());

        // Validate the ClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClassOfBusinessToMatchAllProperties(updatedClassOfBusiness);
    }

    @Test
    @Transactional
    void putNonExistingClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classOfBusiness.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassOfBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classOfBusiness.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classOfBusiness))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classOfBusiness.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassOfBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classOfBusiness))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classOfBusiness.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassOfBusinessMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classOfBusiness)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassOfBusinessWithPatch() throws Exception {
        // Initialize the database
        insertedClassOfBusiness = classOfBusinessRepository.saveAndFlush(classOfBusiness);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classOfBusiness using partial update
        ClassOfBusiness partialUpdatedClassOfBusiness = new ClassOfBusiness();
        partialUpdatedClassOfBusiness.setId(classOfBusiness.getId());

        partialUpdatedClassOfBusiness.name(UPDATED_NAME);

        restClassOfBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassOfBusiness.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassOfBusiness))
            )
            .andExpect(status().isOk());

        // Validate the ClassOfBusiness in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassOfBusinessUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClassOfBusiness, classOfBusiness),
            getPersistedClassOfBusiness(classOfBusiness)
        );
    }

    @Test
    @Transactional
    void fullUpdateClassOfBusinessWithPatch() throws Exception {
        // Initialize the database
        insertedClassOfBusiness = classOfBusinessRepository.saveAndFlush(classOfBusiness);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classOfBusiness using partial update
        ClassOfBusiness partialUpdatedClassOfBusiness = new ClassOfBusiness();
        partialUpdatedClassOfBusiness.setId(classOfBusiness.getId());

        partialUpdatedClassOfBusiness.name(UPDATED_NAME);

        restClassOfBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassOfBusiness.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassOfBusiness))
            )
            .andExpect(status().isOk());

        // Validate the ClassOfBusiness in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassOfBusinessUpdatableFieldsEquals(
            partialUpdatedClassOfBusiness,
            getPersistedClassOfBusiness(partialUpdatedClassOfBusiness)
        );
    }

    @Test
    @Transactional
    void patchNonExistingClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classOfBusiness.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassOfBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classOfBusiness.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classOfBusiness))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classOfBusiness.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassOfBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classOfBusiness))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classOfBusiness.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassOfBusinessMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(classOfBusiness)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassOfBusiness() throws Exception {
        // Initialize the database
        insertedClassOfBusiness = classOfBusinessRepository.saveAndFlush(classOfBusiness);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the classOfBusiness
        restClassOfBusinessMockMvc
            .perform(delete(ENTITY_API_URL_ID, classOfBusiness.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return classOfBusinessRepository.count();
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

    protected ClassOfBusiness getPersistedClassOfBusiness(ClassOfBusiness classOfBusiness) {
        return classOfBusinessRepository.findById(classOfBusiness.getId()).orElseThrow();
    }

    protected void assertPersistedClassOfBusinessToMatchAllProperties(ClassOfBusiness expectedClassOfBusiness) {
        assertClassOfBusinessAllPropertiesEquals(expectedClassOfBusiness, getPersistedClassOfBusiness(expectedClassOfBusiness));
    }

    protected void assertPersistedClassOfBusinessToMatchUpdatableProperties(ClassOfBusiness expectedClassOfBusiness) {
        assertClassOfBusinessAllUpdatablePropertiesEquals(expectedClassOfBusiness, getPersistedClassOfBusiness(expectedClassOfBusiness));
    }
}
