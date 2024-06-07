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
 * A SubClassOfBusiness.
 */
@Entity
@Table(name = "sub_class_of_business")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubClassOfBusiness implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 250)
    @Column(name = "name", length = 250)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subClassOfBusiness")
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
    @JsonIgnoreProperties(value = { "subClassOfBusinesses", "contracts", "insuranceType" }, allowSetters = true)
    private ClassOfBusiness classOfBusiness;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubClassOfBusiness id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SubClassOfBusiness name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setSubClassOfBusiness(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setSubClassOfBusiness(this));
        }
        this.contracts = contracts;
    }

    public SubClassOfBusiness contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public SubClassOfBusiness addContracts(Contract contract) {
        this.contracts.add(contract);
        contract.setSubClassOfBusiness(this);
        return this;
    }

    public SubClassOfBusiness removeContracts(Contract contract) {
        this.contracts.remove(contract);
        contract.setSubClassOfBusiness(null);
        return this;
    }

    public ClassOfBusiness getClassOfBusiness() {
        return this.classOfBusiness;
    }

    public void setClassOfBusiness(ClassOfBusiness classOfBusiness) {
        this.classOfBusiness = classOfBusiness;
    }

    public SubClassOfBusiness classOfBusiness(ClassOfBusiness classOfBusiness) {
        this.setClassOfBusiness(classOfBusiness);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubClassOfBusiness)) {
            return false;
        }
        return getId() != null && getId().equals(((SubClassOfBusiness) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubClassOfBusiness{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
