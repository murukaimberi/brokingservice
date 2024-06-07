package za.co.afrikatek.brokingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import za.co.afrikatek.brokingservice.domain.enumeration.ContractStatus;
import za.co.afrikatek.brokingservice.domain.enumeration.ContractSubType;
import za.co.afrikatek.brokingservice.domain.enumeration.ContractType;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ContractType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type", nullable = false)
    private ContractSubType subType;

    @NotNull
    @Column(name = "inception", nullable = false)
    private ZonedDateTime inception;

    @NotNull
    @Column(name = "expiry", nullable = false)
    private ZonedDateTime expiry;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @NotNull
    @Column(name = "total_sum_insured", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalSumInsured;

    @NotNull
    @Column(name = "limit_of_liability", precision = 21, scale = 2, nullable = false)
    private BigDecimal limitOfLiability;

    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "awardedBusinessShares", "addresses", "insuredContracts", "insurerContracts", "brokerContracts", "reInsurerContracts" },
        allowSetters = true
    )
    private BusinessPartner insured;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "awardedBusinessShares", "addresses", "insuredContracts", "insurerContracts", "brokerContracts", "reInsurerContracts" },
        allowSetters = true
    )
    private BusinessPartner insurer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "awardedBusinessShares", "addresses", "insuredContracts", "insurerContracts", "brokerContracts", "reInsurerContracts" },
        allowSetters = true
    )
    private BusinessPartner broker;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_contract__reinsurers",
        joinColumns = @JoinColumn(name = "contract_id"),
        inverseJoinColumns = @JoinColumn(name = "reinsurers_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "awardedBusinessShares", "addresses", "insuredContracts", "insurerContracts", "brokerContracts", "reInsurerContracts" },
        allowSetters = true
    )
    private Set<BusinessPartner> reinsurers = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "subClassOfBusinesses", "contracts", "insuranceType" }, allowSetters = true)
    private ClassOfBusiness classOfBusiness;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "contracts", "classOfBusiness" }, allowSetters = true)
    private SubClassOfBusiness subClassOfBusiness;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "contracts" }, allowSetters = true)
    private Country country;

    @JsonIgnoreProperties(value = { "contract" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contract")
    private ReinsurancePlacement reinsurancePlacement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContractType getType() {
        return this.type;
    }

    public Contract type(ContractType type) {
        this.setType(type);
        return this;
    }

    public void setType(ContractType type) {
        this.type = type;
    }

    public ContractSubType getSubType() {
        return this.subType;
    }

    public Contract subType(ContractSubType subType) {
        this.setSubType(subType);
        return this;
    }

    public void setSubType(ContractSubType subType) {
        this.subType = subType;
    }

    public ZonedDateTime getInception() {
        return this.inception;
    }

    public Contract inception(ZonedDateTime inception) {
        this.setInception(inception);
        return this;
    }

    public void setInception(ZonedDateTime inception) {
        this.inception = inception;
    }

    public ZonedDateTime getExpiry() {
        return this.expiry;
    }

    public Contract expiry(ZonedDateTime expiry) {
        this.setExpiry(expiry);
        return this;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Contract currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalSumInsured() {
        return this.totalSumInsured;
    }

    public Contract totalSumInsured(BigDecimal totalSumInsured) {
        this.setTotalSumInsured(totalSumInsured);
        return this;
    }

    public void setTotalSumInsured(BigDecimal totalSumInsured) {
        this.totalSumInsured = totalSumInsured;
    }

    public BigDecimal getLimitOfLiability() {
        return this.limitOfLiability;
    }

    public Contract limitOfLiability(BigDecimal limitOfLiability) {
        this.setLimitOfLiability(limitOfLiability);
        return this;
    }

    public void setLimitOfLiability(BigDecimal limitOfLiability) {
        this.limitOfLiability = limitOfLiability;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Contract uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ContractStatus getStatus() {
        return this.status;
    }

    public Contract status(ContractStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Contract active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BusinessPartner getInsured() {
        return this.insured;
    }

    public void setInsured(BusinessPartner businessPartner) {
        this.insured = businessPartner;
    }

    public Contract insured(BusinessPartner businessPartner) {
        this.setInsured(businessPartner);
        return this;
    }

    public BusinessPartner getInsurer() {
        return this.insurer;
    }

    public void setInsurer(BusinessPartner businessPartner) {
        this.insurer = businessPartner;
    }

    public Contract insurer(BusinessPartner businessPartner) {
        this.setInsurer(businessPartner);
        return this;
    }

    public BusinessPartner getBroker() {
        return this.broker;
    }

    public void setBroker(BusinessPartner businessPartner) {
        this.broker = businessPartner;
    }

    public Contract broker(BusinessPartner businessPartner) {
        this.setBroker(businessPartner);
        return this;
    }

    public Set<BusinessPartner> getReinsurers() {
        return this.reinsurers;
    }

    public void setReinsurers(Set<BusinessPartner> businessPartners) {
        this.reinsurers = businessPartners;
    }

    public Contract reinsurers(Set<BusinessPartner> businessPartners) {
        this.setReinsurers(businessPartners);
        return this;
    }

    public Contract addReinsurers(BusinessPartner businessPartner) {
        this.reinsurers.add(businessPartner);
        return this;
    }

    public Contract removeReinsurers(BusinessPartner businessPartner) {
        this.reinsurers.remove(businessPartner);
        return this;
    }

    public ClassOfBusiness getClassOfBusiness() {
        return this.classOfBusiness;
    }

    public void setClassOfBusiness(ClassOfBusiness classOfBusiness) {
        this.classOfBusiness = classOfBusiness;
    }

    public Contract classOfBusiness(ClassOfBusiness classOfBusiness) {
        this.setClassOfBusiness(classOfBusiness);
        return this;
    }

    public SubClassOfBusiness getSubClassOfBusiness() {
        return this.subClassOfBusiness;
    }

    public void setSubClassOfBusiness(SubClassOfBusiness subClassOfBusiness) {
        this.subClassOfBusiness = subClassOfBusiness;
    }

    public Contract subClassOfBusiness(SubClassOfBusiness subClassOfBusiness) {
        this.setSubClassOfBusiness(subClassOfBusiness);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Contract country(Country country) {
        this.setCountry(country);
        return this;
    }

    public ReinsurancePlacement getReinsurancePlacement() {
        return this.reinsurancePlacement;
    }

    public void setReinsurancePlacement(ReinsurancePlacement reinsurancePlacement) {
        if (this.reinsurancePlacement != null) {
            this.reinsurancePlacement.setContract(null);
        }
        if (reinsurancePlacement != null) {
            reinsurancePlacement.setContract(this);
        }
        this.reinsurancePlacement = reinsurancePlacement;
    }

    public Contract reinsurancePlacement(ReinsurancePlacement reinsurancePlacement) {
        this.setReinsurancePlacement(reinsurancePlacement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return getId() != null && getId().equals(((Contract) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", subType='" + getSubType() + "'" +
            ", inception='" + getInception() + "'" +
            ", expiry='" + getExpiry() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", totalSumInsured=" + getTotalSumInsured() +
            ", limitOfLiability=" + getLimitOfLiability() +
            ", uuid='" + getUuid() + "'" +
            ", status='" + getStatus() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
