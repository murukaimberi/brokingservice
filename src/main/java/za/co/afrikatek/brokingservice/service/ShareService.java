package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.Share;
import za.co.afrikatek.brokingservice.repository.ShareRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.Share}.
 */
@Service
@Transactional
public class ShareService {

    private final Logger log = LoggerFactory.getLogger(ShareService.class);

    private final ShareRepository shareRepository;

    public ShareService(ShareRepository shareRepository) {
        this.shareRepository = shareRepository;
    }

    /**
     * Save a share.
     *
     * @param share the entity to save.
     * @return the persisted entity.
     */
    public Share save(Share share) {
        log.debug("Request to save Share : {}", share);
        return shareRepository.save(share);
    }

    /**
     * Update a share.
     *
     * @param share the entity to save.
     * @return the persisted entity.
     */
    public Share update(Share share) {
        log.debug("Request to update Share : {}", share);
        return shareRepository.save(share);
    }

    /**
     * Partially update a share.
     *
     * @param share the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Share> partialUpdate(Share share) {
        log.debug("Request to partially update Share : {}", share);

        return shareRepository
            .findById(share.getId())
            .map(existingShare -> {
                if (share.getSharePercentage() != null) {
                    existingShare.setSharePercentage(share.getSharePercentage());
                }
                if (share.getTotalSumInsured() != null) {
                    existingShare.setTotalSumInsured(share.getTotalSumInsured());
                }
                if (share.getLimitOfLiability() != null) {
                    existingShare.setLimitOfLiability(share.getLimitOfLiability());
                }
                if (share.getGrossPremium() != null) {
                    existingShare.setGrossPremium(share.getGrossPremium());
                }
                if (share.getRiCommission() != null) {
                    existingShare.setRiCommission(share.getRiCommission());
                }
                if (share.getNetPremium() != null) {
                    existingShare.setNetPremium(share.getNetPremium());
                }
                if (share.getBrokerage() != null) {
                    existingShare.setBrokerage(share.getBrokerage());
                }
                if (share.getBrokerageAmount() != null) {
                    existingShare.setBrokerageAmount(share.getBrokerageAmount());
                }
                if (share.getNetPayable() != null) {
                    existingShare.setNetPayable(share.getNetPayable());
                }

                return existingShare;
            })
            .map(shareRepository::save);
    }

    /**
     * Get all the shares.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Share> findAll(Pageable pageable) {
        log.debug("Request to get all Shares");
        return shareRepository.findAll(pageable);
    }

    /**
     * Get all the shares with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Share> findAllWithEagerRelationships(Pageable pageable) {
        return shareRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one share by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Share> findOne(Long id) {
        log.debug("Request to get Share : {}", id);
        return shareRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the share by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Share : {}", id);
        shareRepository.deleteById(id);
    }
}
