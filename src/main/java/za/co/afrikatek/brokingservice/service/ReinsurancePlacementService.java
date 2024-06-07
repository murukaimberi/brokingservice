package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.ReinsurancePlacement;
import za.co.afrikatek.brokingservice.repository.ReinsurancePlacementRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.ReinsurancePlacement}.
 */
@Service
@Transactional
public class ReinsurancePlacementService {

    private final Logger log = LoggerFactory.getLogger(ReinsurancePlacementService.class);

    private final ReinsurancePlacementRepository reinsurancePlacementRepository;

    public ReinsurancePlacementService(ReinsurancePlacementRepository reinsurancePlacementRepository) {
        this.reinsurancePlacementRepository = reinsurancePlacementRepository;
    }

    /**
     * Save a reinsurancePlacement.
     *
     * @param reinsurancePlacement the entity to save.
     * @return the persisted entity.
     */
    public ReinsurancePlacement save(ReinsurancePlacement reinsurancePlacement) {
        log.debug("Request to save ReinsurancePlacement : {}", reinsurancePlacement);
        return reinsurancePlacementRepository.save(reinsurancePlacement);
    }

    /**
     * Update a reinsurancePlacement.
     *
     * @param reinsurancePlacement the entity to save.
     * @return the persisted entity.
     */
    public ReinsurancePlacement update(ReinsurancePlacement reinsurancePlacement) {
        log.debug("Request to update ReinsurancePlacement : {}", reinsurancePlacement);
        return reinsurancePlacementRepository.save(reinsurancePlacement);
    }

    /**
     * Partially update a reinsurancePlacement.
     *
     * @param reinsurancePlacement the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReinsurancePlacement> partialUpdate(ReinsurancePlacement reinsurancePlacement) {
        log.debug("Request to partially update ReinsurancePlacement : {}", reinsurancePlacement);

        return reinsurancePlacementRepository
            .findById(reinsurancePlacement.getId())
            .map(existingReinsurancePlacement -> {
                if (reinsurancePlacement.getRiPercentageCommission() != null) {
                    existingReinsurancePlacement.setRiPercentageCommission(reinsurancePlacement.getRiPercentageCommission());
                }
                if (reinsurancePlacement.getRiPercentageShare() != null) {
                    existingReinsurancePlacement.setRiPercentageShare(reinsurancePlacement.getRiPercentageShare());
                }
                if (reinsurancePlacement.getRiTotalSumInsured() != null) {
                    existingReinsurancePlacement.setRiTotalSumInsured(reinsurancePlacement.getRiTotalSumInsured());
                }
                if (reinsurancePlacement.getRiLimitOfLiability() != null) {
                    existingReinsurancePlacement.setRiLimitOfLiability(reinsurancePlacement.getRiLimitOfLiability());
                }
                if (reinsurancePlacement.getGrossPremiumHundred() != null) {
                    existingReinsurancePlacement.setGrossPremiumHundred(reinsurancePlacement.getGrossPremiumHundred());
                }
                if (reinsurancePlacement.getRiPremium() != null) {
                    existingReinsurancePlacement.setRiPremium(reinsurancePlacement.getRiPremium());
                }
                if (reinsurancePlacement.getRiCommission() != null) {
                    existingReinsurancePlacement.setRiCommission(reinsurancePlacement.getRiCommission());
                }
                if (reinsurancePlacement.getNetDueFromInsurer() != null) {
                    existingReinsurancePlacement.setNetDueFromInsurer(reinsurancePlacement.getNetDueFromInsurer());
                }

                return existingReinsurancePlacement;
            })
            .map(reinsurancePlacementRepository::save);
    }

    /**
     * Get all the reinsurancePlacements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReinsurancePlacement> findAll(Pageable pageable) {
        log.debug("Request to get all ReinsurancePlacements");
        return reinsurancePlacementRepository.findAll(pageable);
    }

    /**
     * Get one reinsurancePlacement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReinsurancePlacement> findOne(Long id) {
        log.debug("Request to get ReinsurancePlacement : {}", id);
        return reinsurancePlacementRepository.findById(id);
    }

    /**
     * Delete the reinsurancePlacement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReinsurancePlacement : {}", id);
        reinsurancePlacementRepository.deleteById(id);
    }
}
