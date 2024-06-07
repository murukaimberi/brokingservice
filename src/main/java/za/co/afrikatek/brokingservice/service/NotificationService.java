package za.co.afrikatek.brokingservice.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.afrikatek.brokingservice.domain.Notification;
import za.co.afrikatek.brokingservice.repository.NotificationRepository;

/**
 * Service Implementation for managing {@link za.co.afrikatek.brokingservice.domain.Notification}.
 */
@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Save a notification.
     *
     * @param notification the entity to save.
     * @return the persisted entity.
     */
    public Notification save(Notification notification) {
        log.debug("Request to save Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    /**
     * Update a notification.
     *
     * @param notification the entity to save.
     * @return the persisted entity.
     */
    public Notification update(Notification notification) {
        log.debug("Request to update Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    /**
     * Partially update a notification.
     *
     * @param notification the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Notification> partialUpdate(Notification notification) {
        log.debug("Request to partially update Notification : {}", notification);

        return notificationRepository
            .findById(notification.getId())
            .map(existingNotification -> {
                if (notification.getMessage() != null) {
                    existingNotification.setMessage(notification.getMessage());
                }
                if (notification.getNotificationDate() != null) {
                    existingNotification.setNotificationDate(notification.getNotificationDate());
                }
                if (notification.getDueDate() != null) {
                    existingNotification.setDueDate(notification.getDueDate());
                }
                if (notification.getNotificationStatus() != null) {
                    existingNotification.setNotificationStatus(notification.getNotificationStatus());
                }

                return existingNotification;
            })
            .map(notificationRepository::save);
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Notification> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable);
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Notification> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }
}
