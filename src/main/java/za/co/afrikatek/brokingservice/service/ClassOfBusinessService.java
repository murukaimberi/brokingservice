package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.ClassOfBusiness;
import za.co.afrikatek.brokingservice.repository.ClassOfBusinessRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.ClassOfBusiness}.
 */
@Service
@Transactional
public class ClassOfBusinessService {

    private final Logger log = LoggerFactory.getLogger(ClassOfBusinessService.class);

    private final ClassOfBusinessRepository classOfBusinessRepository;

    public ClassOfBusinessService(ClassOfBusinessRepository classOfBusinessRepository) {
        this.classOfBusinessRepository = classOfBusinessRepository;
    }

    /**
     * Save a classOfBusiness.
     *
     * @param classOfBusiness the entity to save.
     * @return the persisted entity.
     */
    public ClassOfBusiness save(ClassOfBusiness classOfBusiness) {
        log.debug("Request to save ClassOfBusiness : {}", classOfBusiness);
        return classOfBusinessRepository.save(classOfBusiness);
    }

    /**
     * Update a classOfBusiness.
     *
     * @param classOfBusiness the entity to save.
     * @return the persisted entity.
     */
    public ClassOfBusiness update(ClassOfBusiness classOfBusiness) {
        log.debug("Request to update ClassOfBusiness : {}", classOfBusiness);
        return classOfBusinessRepository.save(classOfBusiness);
    }

    /**
     * Partially update a classOfBusiness.
     *
     * @param classOfBusiness the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClassOfBusiness> partialUpdate(ClassOfBusiness classOfBusiness) {
        log.debug("Request to partially update ClassOfBusiness : {}", classOfBusiness);

        return classOfBusinessRepository
            .findById(classOfBusiness.getId())
            .map(existingClassOfBusiness -> {
                if (classOfBusiness.getName() != null) {
                    existingClassOfBusiness.setName(classOfBusiness.getName());
                }

                return existingClassOfBusiness;
            })
            .map(classOfBusinessRepository::save);
    }

    /**
     * Get all the classOfBusinesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassOfBusiness> findAll(Pageable pageable) {
        log.debug("Request to get all ClassOfBusinesses");
        return classOfBusinessRepository.findAll(pageable);
    }

    /**
     * Get all the classOfBusinesses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ClassOfBusiness> findAllWithEagerRelationships(Pageable pageable) {
        return classOfBusinessRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one classOfBusiness by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClassOfBusiness> findOne(Long id) {
        log.debug("Request to get ClassOfBusiness : {}", id);
        return classOfBusinessRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the classOfBusiness by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ClassOfBusiness : {}", id);
        classOfBusinessRepository.deleteById(id);
    }
}
