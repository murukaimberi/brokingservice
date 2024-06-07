package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.SubClassOfBusiness;
import za.co.afrikatek.brokingservice.repository.SubClassOfBusinessRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.SubClassOfBusiness}.
 */
@Service
@Transactional
public class SubClassOfBusinessService {

    private final Logger log = LoggerFactory.getLogger(SubClassOfBusinessService.class);

    private final SubClassOfBusinessRepository subClassOfBusinessRepository;

    public SubClassOfBusinessService(SubClassOfBusinessRepository subClassOfBusinessRepository) {
        this.subClassOfBusinessRepository = subClassOfBusinessRepository;
    }

    /**
     * Save a subClassOfBusiness.
     *
     * @param subClassOfBusiness the entity to save.
     * @return the persisted entity.
     */
    public SubClassOfBusiness save(SubClassOfBusiness subClassOfBusiness) {
        log.debug("Request to save SubClassOfBusiness : {}", subClassOfBusiness);
        return subClassOfBusinessRepository.save(subClassOfBusiness);
    }

    /**
     * Update a subClassOfBusiness.
     *
     * @param subClassOfBusiness the entity to save.
     * @return the persisted entity.
     */
    public SubClassOfBusiness update(SubClassOfBusiness subClassOfBusiness) {
        log.debug("Request to update SubClassOfBusiness : {}", subClassOfBusiness);
        return subClassOfBusinessRepository.save(subClassOfBusiness);
    }

    /**
     * Partially update a subClassOfBusiness.
     *
     * @param subClassOfBusiness the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubClassOfBusiness> partialUpdate(SubClassOfBusiness subClassOfBusiness) {
        log.debug("Request to partially update SubClassOfBusiness : {}", subClassOfBusiness);

        return subClassOfBusinessRepository
            .findById(subClassOfBusiness.getId())
            .map(existingSubClassOfBusiness -> {
                if (subClassOfBusiness.getName() != null) {
                    existingSubClassOfBusiness.setName(subClassOfBusiness.getName());
                }

                return existingSubClassOfBusiness;
            })
            .map(subClassOfBusinessRepository::save);
    }

    /**
     * Get all the subClassOfBusinesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubClassOfBusiness> findAll(Pageable pageable) {
        log.debug("Request to get all SubClassOfBusinesses");
        return subClassOfBusinessRepository.findAll(pageable);
    }

    /**
     * Get all the subClassOfBusinesses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SubClassOfBusiness> findAllWithEagerRelationships(Pageable pageable) {
        return subClassOfBusinessRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one subClassOfBusiness by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubClassOfBusiness> findOne(Long id) {
        log.debug("Request to get SubClassOfBusiness : {}", id);
        return subClassOfBusinessRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the subClassOfBusiness by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubClassOfBusiness : {}", id);
        subClassOfBusinessRepository.deleteById(id);
    }
}
