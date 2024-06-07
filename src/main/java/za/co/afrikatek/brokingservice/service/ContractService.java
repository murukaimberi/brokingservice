package za.co.afrikatek.brokingservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.Contract;
import za.co.afrikatek.brokingservice.repository.ContractRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.Contract}.
 */
@Service
@Transactional
public class ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractService.class);

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    /**
     * Save a contract.
     *
     * @param contract the entity to save.
     * @return the persisted entity.
     */
    public Contract save(Contract contract) {
        log.debug("Request to save Contract : {}", contract);
        return contractRepository.save(contract);
    }

    /**
     * Update a contract.
     *
     * @param contract the entity to save.
     * @return the persisted entity.
     */
    public Contract update(Contract contract) {
        log.debug("Request to update Contract : {}", contract);
        return contractRepository.save(contract);
    }

    /**
     * Partially update a contract.
     *
     * @param contract the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Contract> partialUpdate(Contract contract) {
        log.debug("Request to partially update Contract : {}", contract);

        return contractRepository
            .findById(contract.getId())
            .map(existingContract -> {
                if (contract.getType() != null) {
                    existingContract.setType(contract.getType());
                }
                if (contract.getSubType() != null) {
                    existingContract.setSubType(contract.getSubType());
                }
                if (contract.getInception() != null) {
                    existingContract.setInception(contract.getInception());
                }
                if (contract.getExpiry() != null) {
                    existingContract.setExpiry(contract.getExpiry());
                }
                if (contract.getCurrency() != null) {
                    existingContract.setCurrency(contract.getCurrency());
                }
                if (contract.getTotalSumInsured() != null) {
                    existingContract.setTotalSumInsured(contract.getTotalSumInsured());
                }
                if (contract.getLimitOfLiability() != null) {
                    existingContract.setLimitOfLiability(contract.getLimitOfLiability());
                }
                if (contract.getUuid() != null) {
                    existingContract.setUuid(contract.getUuid());
                }
                if (contract.getStatus() != null) {
                    existingContract.setStatus(contract.getStatus());
                }
                if (contract.getActive() != null) {
                    existingContract.setActive(contract.getActive());
                }

                return existingContract;
            })
            .map(contractRepository::save);
    }

    /**
     * Get all the contracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Contract> findAll(Pageable pageable) {
        log.debug("Request to get all Contracts");
        return contractRepository.findAll(pageable);
    }

    /**
     * Get all the contracts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Contract> findAllWithEagerRelationships(Pageable pageable) {
        return contractRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the contracts where ReinsurancePlacement is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Contract> findAllWhereReinsurancePlacementIsNull() {
        log.debug("Request to get all contracts where ReinsurancePlacement is null");
        return StreamSupport.stream(contractRepository.findAll().spliterator(), false)
            .filter(contract -> contract.getReinsurancePlacement() == null)
            .toList();
    }

    /**
     * Get one contract by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Contract> findOne(Long id) {
        log.debug("Request to get Contract : {}", id);
        return contractRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the contract by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Contract : {}", id);
        contractRepository.deleteById(id);
    }
}
