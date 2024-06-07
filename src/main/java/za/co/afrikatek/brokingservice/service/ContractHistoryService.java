package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.ContractHistory;
import za.co.afrikatek.brokingservice.repository.ContractHistoryRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.ContractHistory}.
 */
@Service
@Transactional
public class ContractHistoryService {

    private final Logger log = LoggerFactory.getLogger(ContractHistoryService.class);

    private final ContractHistoryRepository contractHistoryRepository;

    public ContractHistoryService(ContractHistoryRepository contractHistoryRepository) {
        this.contractHistoryRepository = contractHistoryRepository;
    }

    /**
     * Save a contractHistory.
     *
     * @param contractHistory the entity to save.
     * @return the persisted entity.
     */
    public ContractHistory save(ContractHistory contractHistory) {
        log.debug("Request to save ContractHistory : {}", contractHistory);
        return contractHistoryRepository.save(contractHistory);
    }

    /**
     * Update a contractHistory.
     *
     * @param contractHistory the entity to save.
     * @return the persisted entity.
     */
    public ContractHistory update(ContractHistory contractHistory) {
        log.debug("Request to update ContractHistory : {}", contractHistory);
        return contractHistoryRepository.save(contractHistory);
    }

    /**
     * Partially update a contractHistory.
     *
     * @param contractHistory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContractHistory> partialUpdate(ContractHistory contractHistory) {
        log.debug("Request to partially update ContractHistory : {}", contractHistory);

        return contractHistoryRepository
            .findById(contractHistory.getId())
            .map(existingContractHistory -> {
                if (contractHistory.getContractCreatedDate() != null) {
                    existingContractHistory.setContractCreatedDate(contractHistory.getContractCreatedDate());
                }
                if (contractHistory.getContractActiveDate() != null) {
                    existingContractHistory.setContractActiveDate(contractHistory.getContractActiveDate());
                }
                if (contractHistory.getContractInActiveDate() != null) {
                    existingContractHistory.setContractInActiveDate(contractHistory.getContractInActiveDate());
                }
                if (contractHistory.getContractLastModifiedDate() != null) {
                    existingContractHistory.setContractLastModifiedDate(contractHistory.getContractLastModifiedDate());
                }
                if (contractHistory.getChangeDescription() != null) {
                    existingContractHistory.setChangeDescription(contractHistory.getChangeDescription());
                }

                return existingContractHistory;
            })
            .map(contractHistoryRepository::save);
    }

    /**
     * Get all the contractHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractHistory> findAll(Pageable pageable) {
        log.debug("Request to get all ContractHistories");
        return contractHistoryRepository.findAll(pageable);
    }

    /**
     * Get all the contractHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ContractHistory> findAllWithEagerRelationships(Pageable pageable) {
        return contractHistoryRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one contractHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContractHistory> findOne(Long id) {
        log.debug("Request to get ContractHistory : {}", id);
        return contractHistoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the contractHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContractHistory : {}", id);
        contractHistoryRepository.deleteById(id);
    }
}
