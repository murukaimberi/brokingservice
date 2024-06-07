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
 * A InsuranceType.
 */
@Entity
@Table(name = "insurance_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 150)
    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "insuranceType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subClassOfBusinesses", "contracts", "insuranceType" }, allowSetters = true)
    private Set<ClassOfBusiness> classOfBusinesses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InsuranceType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public InsuranceType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ClassOfBusiness> getClassOfBusinesses() {
        return this.classOfBusinesses;
    }

    public void setClassOfBusinesses(Set<ClassOfBusiness> classOfBusinesses) {
        if (this.classOfBusinesses != null) {
            this.classOfBusinesses.forEach(i -> i.setInsuranceType(null));
        }
        if (classOfBusinesses != null) {
            classOfBusinesses.forEach(i -> i.setInsuranceType(this));
        }
        this.classOfBusinesses = classOfBusinesses;
    }

    public InsuranceType classOfBusinesses(Set<ClassOfBusiness> classOfBusinesses) {
        this.setClassOfBusinesses(classOfBusinesses);
        return this;
    }

    public InsuranceType addClassOfBusinesses(ClassOfBusiness classOfBusiness) {
        this.classOfBusinesses.add(classOfBusiness);
        classOfBusiness.setInsuranceType(this);
        return this;
    }

    public InsuranceType removeClassOfBusinesses(ClassOfBusiness classOfBusiness) {
        this.classOfBusinesses.remove(classOfBusiness);
        classOfBusiness.setInsuranceType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceType)) {
            return false;
        }
        return getId() != null && getId().equals(((InsuranceType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
