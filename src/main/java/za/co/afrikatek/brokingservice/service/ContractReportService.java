package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.ContractReport;
import za.co.afrikatek.brokingservice.repository.ContractReportRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.ContractReport}.
 */
@Service
@Transactional
public class ContractReportService {

    private final Logger log = LoggerFactory.getLogger(ContractReportService.class);

    private final ContractReportRepository contractReportRepository;

    public ContractReportService(ContractReportRepository contractReportRepository) {
        this.contractReportRepository = contractReportRepository;
    }

    /**
     * Save a contractReport.
     *
     * @param contractReport the entity to save.
     * @return the persisted entity.
     */
    public ContractReport save(ContractReport contractReport) {
        log.debug("Request to save ContractReport : {}", contractReport);
        return contractReportRepository.save(contractReport);
    }

    /**
     * Update a contractReport.
     *
     * @param contractReport the entity to save.
     * @return the persisted entity.
     */
    public ContractReport update(ContractReport contractReport) {
        log.debug("Request to update ContractReport : {}", contractReport);
        return contractReportRepository.save(contractReport);
    }

    /**
     * Partially update a contractReport.
     *
     * @param contractReport the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContractReport> partialUpdate(ContractReport contractReport) {
        log.debug("Request to partially update ContractReport : {}", contractReport);

        return contractReportRepository
            .findById(contractReport.getId())
            .map(existingContractReport -> {
                if (contractReport.getContractDocument() != null) {
                    existingContractReport.setContractDocument(contractReport.getContractDocument());
                }
                if (contractReport.getContractDocumentContentType() != null) {
                    existingContractReport.setContractDocumentContentType(contractReport.getContractDocumentContentType());
                }
                if (contractReport.getCreatedDate() != null) {
                    existingContractReport.setCreatedDate(contractReport.getCreatedDate());
                }

                return existingContractReport;
            })
            .map(contractReportRepository::save);
    }

    /**
     * Get all the contractReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractReport> findAll(Pageable pageable) {
        log.debug("Request to get all ContractReports");
        return contractReportRepository.findAll(pageable);
    }

    /**
     * Get one contractReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContractReport> findOne(Long id) {
        log.debug("Request to get ContractReport : {}", id);
        return contractReportRepository.findById(id);
    }

    /**
     * Delete the contractReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContractReport : {}", id);
        contractReportRepository.deleteById(id);
    }
}
