package za.co.afrikatek.brokingservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import za.co.afrikatek.brokingservice.domain.enumeration.NotificationStatus;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "notification_date", nullable = false)
    private ZonedDateTime notificationDate;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private ZonedDateTime dueDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_status", nullable = false)
    private NotificationStatus notificationStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getNotificationDate() {
        return this.notificationDate;
    }

    public Notification notificationDate(ZonedDateTime notificationDate) {
        this.setNotificationDate(notificationDate);
        return this;
    }

    public void setNotificationDate(ZonedDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    public ZonedDateTime getDueDate() {
        return this.dueDate;
    }

    public Notification dueDate(ZonedDateTime dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public NotificationStatus getNotificationStatus() {
        return this.notificationStatus;
    }

    public Notification notificationStatus(NotificationStatus notificationStatus) {
        this.setNotificationStatus(notificationStatus);
        return this;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", notificationDate='" + getNotificationDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", notificationStatus='" + getNotificationStatus() + "'" +
            "}";
    }
}
