package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.InsuranceType;
import za.co.afrikatek.brokingservice.repository.InsuranceTypeRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.InsuranceType}.
 */
@Service
@Transactional
public class InsuranceTypeService {

    private final Logger log = LoggerFactory.getLogger(InsuranceTypeService.class);

    private final InsuranceTypeRepository insuranceTypeRepository;

    public InsuranceTypeService(InsuranceTypeRepository insuranceTypeRepository) {
        this.insuranceTypeRepository = insuranceTypeRepository;
    }

    /**
     * Save a insuranceType.
     *
     * @param insuranceType the entity to save.
     * @return the persisted entity.
     */
    public InsuranceType save(InsuranceType insuranceType) {
        log.debug("Request to save InsuranceType : {}", insuranceType);
        return insuranceTypeRepository.save(insuranceType);
    }

    /**
     * Update a insuranceType.
     *
     * @param insuranceType the entity to save.
     * @return the persisted entity.
     */
    public InsuranceType update(InsuranceType insuranceType) {
        log.debug("Request to update InsuranceType : {}", insuranceType);
        return insuranceTypeRepository.save(insuranceType);
    }

    /**
     * Partially update a insuranceType.
     *
     * @param insuranceType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsuranceType> partialUpdate(InsuranceType insuranceType) {
        log.debug("Request to partially update InsuranceType : {}", insuranceType);

        return insuranceTypeRepository
            .findById(insuranceType.getId())
            .map(existingInsuranceType -> {
                if (insuranceType.getName() != null) {
                    existingInsuranceType.setName(insuranceType.getName());
                }

                return existingInsuranceType;
            })
            .map(insuranceTypeRepository::save);
    }

    /**
     * Get all the insuranceTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InsuranceType> findAll(Pageable pageable) {
        log.debug("Request to get all InsuranceTypes");
        return insuranceTypeRepository.findAll(pageable);
    }

    /**
     * Get one insuranceType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsuranceType> findOne(Long id) {
        log.debug("Request to get InsuranceType : {}", id);
        return insuranceTypeRepository.findById(id);
    }

    /**
     * Delete the insuranceType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InsuranceType : {}", id);
        insuranceTypeRepository.deleteById(id);
    }
}
