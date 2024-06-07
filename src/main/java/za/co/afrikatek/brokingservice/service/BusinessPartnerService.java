package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.BusinessPartner;
import za.co.afrikatek.brokingservice.repository.BusinessPartnerRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.BusinessPartner}.
 */
@Service
@Transactional
public class BusinessPartnerService {

    private final Logger log = LoggerFactory.getLogger(BusinessPartnerService.class);

    private final BusinessPartnerRepository businessPartnerRepository;

    public BusinessPartnerService(BusinessPartnerRepository businessPartnerRepository) {
        this.businessPartnerRepository = businessPartnerRepository;
    }

    /**
     * Save a businessPartner.
     *
     * @param businessPartner the entity to save.
     * @return the persisted entity.
     */
    public BusinessPartner save(BusinessPartner businessPartner) {
        log.debug("Request to save BusinessPartner : {}", businessPartner);
        return businessPartnerRepository.save(businessPartner);
    }

    /**
     * Update a businessPartner.
     *
     * @param businessPartner the entity to save.
     * @return the persisted entity.
     */
    public BusinessPartner update(BusinessPartner businessPartner) {
        log.debug("Request to update BusinessPartner : {}", businessPartner);
        return businessPartnerRepository.save(businessPartner);
    }

    /**
     * Partially update a businessPartner.
     *
     * @param businessPartner the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BusinessPartner> partialUpdate(BusinessPartner businessPartner) {
        log.debug("Request to partially update BusinessPartner : {}", businessPartner);

        return businessPartnerRepository
            .findById(businessPartner.getId())
            .map(existingBusinessPartner -> {
                if (businessPartner.getName() != null) {
                    existingBusinessPartner.setName(businessPartner.getName());
                }
                if (businessPartner.getDescription() != null) {
                    existingBusinessPartner.setDescription(businessPartner.getDescription());
                }
                if (businessPartner.getRepresentativeName() != null) {
                    existingBusinessPartner.setRepresentativeName(businessPartner.getRepresentativeName());
                }
                if (businessPartner.getEmail() != null) {
                    existingBusinessPartner.setEmail(businessPartner.getEmail());
                }
                if (businessPartner.getPhoneNumber() != null) {
                    existingBusinessPartner.setPhoneNumber(businessPartner.getPhoneNumber());
                }
                if (businessPartner.getAgentType() != null) {
                    existingBusinessPartner.setAgentType(businessPartner.getAgentType());
                }

                return existingBusinessPartner;
            })
            .map(businessPartnerRepository::save);
    }

    /**
     * Get all the businessPartners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessPartner> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessPartners");
        return businessPartnerRepository.findAll(pageable);
    }

    /**
     * Get one businessPartner by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BusinessPartner> findOne(Long id) {
        log.debug("Request to get BusinessPartner : {}", id);
        return businessPartnerRepository.findById(id);
    }

    /**
     * Delete the businessPartner by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessPartner : {}", id);
        businessPartnerRepository.deleteById(id);
    }
}
