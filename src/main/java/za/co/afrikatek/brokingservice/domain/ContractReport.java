package za.co.afrikatek.brokingservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContractReport.
 */
@Entity
@Table(name = "contract_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "contract_document", nullable = false)
    private byte[] contractDocument;

    @NotNull
    @Column(name = "contract_document_content_type", nullable = false)
    private String contractDocumentContentType;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContractReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContractDocument() {
        return this.contractDocument;
    }

    public ContractReport contractDocument(byte[] contractDocument) {
        this.setContractDocument(contractDocument);
        return this;
    }

    public void setContractDocument(byte[] contractDocument) {
        this.contractDocument = contractDocument;
    }

    public String getContractDocumentContentType() {
        return this.contractDocumentContentType;
    }

    public ContractReport contractDocumentContentType(String contractDocumentContentType) {
        this.contractDocumentContentType = contractDocumentContentType;
        return this;
    }

    public void setContractDocumentContentType(String contractDocumentContentType) {
        this.contractDocumentContentType = contractDocumentContentType;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public ContractReport createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractReport)) {
            return false;
        }
        return getId() != null && getId().equals(((ContractReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractReport{" +
            "id=" + getId() +
            ", contractDocument='" + getContractDocument() + "'" +
            ", contractDocumentContentType='" + getContractDocumentContentType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
