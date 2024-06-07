package za.co.afrikatek.brokingservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContractHistory.
 */
@Entity
@Table(name = "contract_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "contract_created_date", nullable = false)
    private ZonedDateTime contractCreatedDate;

    @NotNull
    @Column(name = "contract_active_date", nullable = false)
    private ZonedDateTime contractActiveDate;

    @Column(name = "contract_in_active_date")
    private ZonedDateTime contractInActiveDate;

    @Column(name = "contract_last_modified_date")
    private ZonedDateTime contractLastModifiedDate;

    @Lob
    @Column(name = "change_description", nullable = false)
    private String changeDescription;

    @ManyToOne(optional = false)
    @NotNull
    private User updated;

    @ManyToOne(fetch = FetchType.LAZY)
    private User approved;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContractHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getContractCreatedDate() {
        return this.contractCreatedDate;
    }

    public ContractHistory contractCreatedDate(ZonedDateTime contractCreatedDate) {
        this.setContractCreatedDate(contractCreatedDate);
        return this;
    }

    public void setContractCreatedDate(ZonedDateTime contractCreatedDate) {
        this.contractCreatedDate = contractCreatedDate;
    }

    public ZonedDateTime getContractActiveDate() {
        return this.contractActiveDate;
    }

    public ContractHistory contractActiveDate(ZonedDateTime contractActiveDate) {
        this.setContractActiveDate(contractActiveDate);
        return this;
    }

    public void setContractActiveDate(ZonedDateTime contractActiveDate) {
        this.contractActiveDate = contractActiveDate;
    }

    public ZonedDateTime getContractInActiveDate() {
        return this.contractInActiveDate;
    }

    public ContractHistory contractInActiveDate(ZonedDateTime contractInActiveDate) {
        this.setContractInActiveDate(contractInActiveDate);
        return this;
    }

    public void setContractInActiveDate(ZonedDateTime contractInActiveDate) {
        this.contractInActiveDate = contractInActiveDate;
    }

    public ZonedDateTime getContractLastModifiedDate() {
        return this.contractLastModifiedDate;
    }

    public ContractHistory contractLastModifiedDate(ZonedDateTime contractLastModifiedDate) {
        this.setContractLastModifiedDate(contractLastModifiedDate);
        return this;
    }

    public void setContractLastModifiedDate(ZonedDateTime contractLastModifiedDate) {
        this.contractLastModifiedDate = contractLastModifiedDate;
    }

    public String getChangeDescription() {
        return this.changeDescription;
    }

    public ContractHistory changeDescription(String changeDescription) {
        this.setChangeDescription(changeDescription);
        return this;
    }

    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }

    public User getUpdated() {
        return this.updated;
    }

    public void setUpdated(User user) {
        this.updated = user;
    }

    public ContractHistory updated(User user) {
        this.setUpdated(user);
        return this;
    }

    public User getApproved() {
        return this.approved;
    }

    public void setApproved(User user) {
        this.approved = user;
    }

    public ContractHistory approved(User user) {
        this.setApproved(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ContractHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractHistory{" +
            "id=" + getId() +
            ", contractCreatedDate='" + getContractCreatedDate() + "'" +
            ", contractActiveDate='" + getContractActiveDate() + "'" +
            ", contractInActiveDate='" + getContractInActiveDate() + "'" +
            ", contractLastModifiedDate='" + getContractLastModifiedDate() + "'" +
            ", changeDescription='" + getChangeDescription() + "'" +
            "}";
    }
}
