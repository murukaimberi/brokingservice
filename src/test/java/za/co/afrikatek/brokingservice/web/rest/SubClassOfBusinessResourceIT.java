package za.co.afrikatek.brokingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.afrikatek.brokingservice.domain.SubClassOfBusinessAsserts.*;
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
import za.co.afrikatek.brokingservice.domain.SubClassOfBusiness;
import za.co.afrikatek.brokingservice.repository.SubClassOfBusinessRepository;
import za.co.afrikatek.brokingservice.service.SubClassOfBusinessService;

/**
 * Integration tests for the {@link SubClassOfBusinessResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubClassOfBusinessResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sub-class-of-businesses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubClassOfBusinessRepository subClassOfBusinessRepository;

    @Mock
    private SubClassOfBusinessRepository subClassOfBusinessRepositoryMock;

    @Mock
    private SubClassOfBusinessService subClassOfBusinessServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubClassOfBusinessMockMvc;

    private SubClassOfBusiness subClassOfBusiness;

    private SubClassOfBusiness insertedSubClassOfBusiness;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubClassOfBusiness createEntity(EntityManager em) {
        SubClassOfBusiness subClassOfBusiness = new SubClassOfBusiness().name(DEFAULT_NAME);
        // Add required entity
        ClassOfBusiness classOfBusiness;
        if (TestUtil.findAll(em, ClassOfBusiness.class).isEmpty()) {
            classOfBusiness = ClassOfBusinessResourceIT.createEntity(em);
            em.persist(classOfBusiness);
            em.flush();
        } else {
            classOfBusiness = TestUtil.findAll(em, ClassOfBusiness.class).get(0);
        }
        subClassOfBusiness.setClassOfBusiness(classOfBusiness);
        return subClassOfBusiness;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubClassOfBusiness createUpdatedEntity(EntityManager em) {
        SubClassOfBusiness subClassOfBusiness = new SubClassOfBusiness().name(UPDATED_NAME);
        // Add required entity
        ClassOfBusiness classOfBusiness;
        if (TestUtil.findAll(em, ClassOfBusiness.class).isEmpty()) {
            classOfBusiness = ClassOfBusinessResourceIT.createUpdatedEntity(em);
            em.persist(classOfBusiness);
            em.flush();
        } else {
            classOfBusiness = TestUtil.findAll(em, ClassOfBusiness.class).get(0);
        }
        subClassOfBusiness.setClassOfBusiness(classOfBusiness);
        return subClassOfBusiness;
    }

    @BeforeEach
    public void initTest() {
        subClassOfBusiness = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubClassOfBusiness != null) {
            subClassOfBusinessRepository.delete(insertedSubClassOfBusiness);
            insertedSubClassOfBusiness = null;
        }
    }

    @Test
    @Transactional
    void createSubClassOfBusiness() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubClassOfBusiness
        var returnedSubClassOfBusiness = om.readValue(
            restSubClassOfBusinessMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subClassOfBusiness)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubClassOfBusiness.class
        );

        // Validate the SubClassOfBusiness in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSubClassOfBusinessUpdatableFieldsEquals(
            returnedSubClassOfBusiness,
            getPersistedSubClassOfBusiness(returnedSubClassOfBusiness)
        );

        insertedSubClassOfBusiness = returnedSubClassOfBusiness;
    }

    @Test
    @Transactional
    void createSubClassOfBusinessWithExistingId() throws Exception {
        // Create the SubClassOfBusiness with an existing ID
        subClassOfBusiness.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubClassOfBusinessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subClassOfBusiness)))
            .andExpect(status().isBadRequest());

        // Validate the SubClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubClassOfBusinesses() throws Exception {
        // Initialize the database
        insertedSubClassOfBusiness = subClassOfBusinessRepository.saveAndFlush(subClassOfBusiness);

        // Get all the subClassOfBusinessList
        restSubClassOfBusinessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subClassOfBusiness.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubClassOfBusinessesWithEagerRelationshipsIsEnabled() throws Exception {
        when(subClassOfBusinessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubClassOfBusinessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subClassOfBusinessServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubClassOfBusinessesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(subClassOfBusinessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubClassOfBusinessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(subClassOfBusinessRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSubClassOfBusiness() throws Exception {
        // Initialize the database
        insertedSubClassOfBusiness = subClassOfBusinessRepository.saveAndFlush(subClassOfBusiness);

        // Get the subClassOfBusiness
        restSubClassOfBusinessMockMvc
            .perform(get(ENTITY_API_URL_ID, subClassOfBusiness.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subClassOfBusiness.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSubClassOfBusiness() throws Exception {
        // Get the subClassOfBusiness
        restSubClassOfBusinessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubClassOfBusiness() throws Exception {
        // Initialize the database
        insertedSubClassOfBusiness = subClassOfBusinessRepository.saveAndFlush(subClassOfBusiness);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subClassOfBusiness
        SubClassOfBusiness updatedSubClassOfBusiness = subClassOfBusinessRepository.findById(subClassOfBusiness.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubClassOfBusiness are not directly saved in db
        em.detach(updatedSubClassOfBusiness);
        updatedSubClassOfBusiness.name(UPDATED_NAME);

        restSubClassOfBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubClassOfBusiness.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSubClassOfBusiness))
            )
            .andExpect(status().isOk());

        // Validate the SubClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubClassOfBusinessToMatchAllProperties(updatedSubClassOfBusiness);
    }

    @Test
    @Transactional
    void putNonExistingSubClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subClassOfBusiness.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubClassOfBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subClassOfBusiness.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subClassOfBusiness))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subClassOfBusiness.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubClassOfBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subClassOfBusiness))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subClassOfBusiness.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubClassOfBusinessMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subClassOfBusiness)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubClassOfBusinessWithPatch() throws Exception {
        // Initialize the database
        insertedSubClassOfBusiness = subClassOfBusinessRepository.saveAndFlush(subClassOfBusiness);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subClassOfBusiness using partial update
        SubClassOfBusiness partialUpdatedSubClassOfBusiness = new SubClassOfBusiness();
        partialUpdatedSubClassOfBusiness.setId(subClassOfBusiness.getId());

        restSubClassOfBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubClassOfBusiness.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubClassOfBusiness))
            )
            .andExpect(status().isOk());

        // Validate the SubClassOfBusiness in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubClassOfBusinessUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubClassOfBusiness, subClassOfBusiness),
            getPersistedSubClassOfBusiness(subClassOfBusiness)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubClassOfBusinessWithPatch() throws Exception {
        // Initialize the database
        insertedSubClassOfBusiness = subClassOfBusinessRepository.saveAndFlush(subClassOfBusiness);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subClassOfBusiness using partial update
        SubClassOfBusiness partialUpdatedSubClassOfBusiness = new SubClassOfBusiness();
        partialUpdatedSubClassOfBusiness.setId(subClassOfBusiness.getId());

        partialUpdatedSubClassOfBusiness.name(UPDATED_NAME);

        restSubClassOfBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubClassOfBusiness.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubClassOfBusiness))
            )
            .andExpect(status().isOk());

        // Validate the SubClassOfBusiness in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubClassOfBusinessUpdatableFieldsEquals(
            partialUpdatedSubClassOfBusiness,
            getPersistedSubClassOfBusiness(partialUpdatedSubClassOfBusiness)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subClassOfBusiness.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubClassOfBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subClassOfBusiness.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subClassOfBusiness))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subClassOfBusiness.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubClassOfBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subClassOfBusiness))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubClassOfBusiness() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subClassOfBusiness.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubClassOfBusinessMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subClassOfBusiness)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubClassOfBusiness in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubClassOfBusiness() throws Exception {
        // Initialize the database
        insertedSubClassOfBusiness = subClassOfBusinessRepository.saveAndFlush(subClassOfBusiness);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subClassOfBusiness
        restSubClassOfBusinessMockMvc
            .perform(delete(ENTITY_API_URL_ID, subClassOfBusiness.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subClassOfBusinessRepository.count();
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

    protected SubClassOfBusiness getPersistedSubClassOfBusiness(SubClassOfBusiness subClassOfBusiness) {
        return subClassOfBusinessRepository.findById(subClassOfBusiness.getId()).orElseThrow();
    }

    protected void assertPersistedSubClassOfBusinessToMatchAllProperties(SubClassOfBusiness expectedSubClassOfBusiness) {
        assertSubClassOfBusinessAllPropertiesEquals(expectedSubClassOfBusiness, getPersistedSubClassOfBusiness(expectedSubClassOfBusiness));
    }

    protected void assertPersistedSubClassOfBusinessToMatchUpdatableProperties(SubClassOfBusiness expectedSubClassOfBusiness) {
        assertSubClassOfBusinessAllUpdatablePropertiesEquals(
            expectedSubClassOfBusiness,
            getPersistedSubClassOfBusiness(expectedSubClassOfBusiness)
        );
    }
}
