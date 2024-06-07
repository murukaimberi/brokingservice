package za.co.afrikatek.brokingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClassOfBusiness.
 */
@Entity
@Table(name = "class_of_business")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassOfBusiness implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 250)
    @Column(name = "name", length = 250)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classOfBusiness")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contracts", "classOfBusiness" }, allowSetters = true)
    private Set<SubClassOfBusiness> subClassOfBusinesses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classOfBusiness")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "insured", "insurer", "broker", "reinsurers", "classOfBusiness", "subClassOfBusiness", "country", "reinsurancePlacement",
        },
        allowSetters = true
    )
    private Set<Contract> contracts = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "classOfBusinesses" }, allowSetters = true)
    private InsuranceType insuranceType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClassOfBusiness id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ClassOfBusiness name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SubClassOfBusiness> getSubClassOfBusinesses() {
        return this.subClassOfBusinesses;
    }

    public void setSubClassOfBusinesses(Set<SubClassOfBusiness> subClassOfBusinesses) {
        if (this.subClassOfBusinesses != null) {
            this.subClassOfBusinesses.forEach(i -> i.setClassOfBusiness(null));
        }
        if (subClassOfBusinesses != null) {
            subClassOfBusinesses.forEach(i -> i.setClassOfBusiness(this));
        }
        this.subClassOfBusinesses = subClassOfBusinesses;
    }

    public ClassOfBusiness subClassOfBusinesses(Set<SubClassOfBusiness> subClassOfBusinesses) {
        this.setSubClassOfBusinesses(subClassOfBusinesses);
        return this;
    }

    public ClassOfBusiness addSubClassOfBusinesses(SubClassOfBusiness subClassOfBusiness) {
        this.subClassOfBusinesses.add(subClassOfBusiness);
        subClassOfBusiness.setClassOfBusiness(this);
        return this;
    }

    public ClassOfBusiness removeSubClassOfBusinesses(SubClassOfBusiness subClassOfBusiness) {
        this.subClassOfBusinesses.remove(subClassOfBusiness);
        subClassOfBusiness.setClassOfBusiness(null);
        return this;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setClassOfBusiness(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setClassOfBusiness(this));
        }
        this.contracts = contracts;
    }

    public ClassOfBusiness contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public ClassOfBusiness addContracts(Contract contract) {
        this.contracts.add(contract);
        contract.setClassOfBusiness(this);
        return this;
    }

    public ClassOfBusiness removeContracts(Contract contract) {
        this.contracts.remove(contract);
        contract.setClassOfBusiness(null);
        return this;
    }

    public InsuranceType getInsuranceType() {
        return this.insuranceType;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public ClassOfBusiness insuranceType(InsuranceType insuranceType) {
        this.setInsuranceType(insuranceType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassOfBusiness)) {
            return false;
        }
        return getId() != null && getId().equals(((ClassOfBusiness) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassOfBusiness{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
