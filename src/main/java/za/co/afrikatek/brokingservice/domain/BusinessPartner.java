package za.co.afrikatek.brokingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import za.co.afrikatek.brokingservice.domain.enumeration.InsuranceAgentType;

/**
 * A BusinessPartner.
 */
@Entity
@Table(name = "business_partner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessPartner implements Serializable {

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

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Size(min = 3, max = 150)
    @Column(name = "representative_name", length = 150, nullable = false)
    private String representativeName;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "agent_type", nullable = false)
    private InsuranceAgentType agentType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reInsurer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reInsurer" }, allowSetters = true)
    private Set<Share> awardedBusinessShares = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cedent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cedent" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "insured")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "insured", "insurer", "broker", "reinsurers", "classOfBusiness", "subClassOfBusiness", "country", "reinsurancePlacement",
        },
        allowSetters = true
    )
    private Set<Contract> insuredContracts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "insurer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "insured", "insurer", "broker", "reinsurers", "classOfBusiness", "subClassOfBusiness", "country", "reinsurancePlacement",
        },
        allowSetters = true
    )
    private Set<Contract> insurerContracts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "broker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "insured", "insurer", "broker", "reinsurers", "classOfBusiness", "subClassOfBusiness", "country", "reinsurancePlacement",
        },
        allowSetters = true
    )
    private Set<Contract> brokerContracts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "reinsurers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "insured", "insurer", "broker", "reinsurers", "classOfBusiness", "subClassOfBusiness", "country", "reinsurancePlacement",
        },
        allowSetters = true
    )
    private Set<Contract> reInsurerContracts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusinessPartner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BusinessPartner name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public BusinessPartner description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepresentativeName() {
        return this.representativeName;
    }

    public BusinessPartner representativeName(String representativeName) {
        this.setRepresentativeName(representativeName);
        return this;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getEmail() {
        return this.email;
    }

    public BusinessPartner email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public BusinessPartner phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public InsuranceAgentType getAgentType() {
        return this.agentType;
    }

    public BusinessPartner agentType(InsuranceAgentType agentType) {
        this.setAgentType(agentType);
        return this;
    }

    public void setAgentType(InsuranceAgentType agentType) {
        this.agentType = agentType;
    }

    public Set<Share> getAwardedBusinessShares() {
        return this.awardedBusinessShares;
    }

    public void setAwardedBusinessShares(Set<Share> shares) {
        if (this.awardedBusinessShares != null) {
            this.awardedBusinessShares.forEach(i -> i.setReInsurer(null));
        }
        if (shares != null) {
            shares.forEach(i -> i.setReInsurer(this));
        }
        this.awardedBusinessShares = shares;
    }

    public BusinessPartner awardedBusinessShares(Set<Share> shares) {
        this.setAwardedBusinessShares(shares);
        return this;
    }

    public BusinessPartner addAwardedBusinessShares(Share share) {
        this.awardedBusinessShares.add(share);
        share.setReInsurer(this);
        return this;
    }

    public BusinessPartner removeAwardedBusinessShares(Share share) {
        this.awardedBusinessShares.remove(share);
        share.setReInsurer(null);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCedent(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setCedent(this));
        }
        this.addresses = addresses;
    }

    public BusinessPartner addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public BusinessPartner addAddresses(Address address) {
        this.addresses.add(address);
        address.setCedent(this);
        return this;
    }

    public BusinessPartner removeAddresses(Address address) {
        this.addresses.remove(address);
        address.setCedent(null);
        return this;
    }

    public Set<Contract> getInsuredContracts() {
        return this.insuredContracts;
    }

    public void setInsuredContracts(Set<Contract> contracts) {
        if (this.insuredContracts != null) {
            this.insuredContracts.forEach(i -> i.setInsured(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setInsured(this));
        }
        this.insuredContracts = contracts;
    }

    public BusinessPartner insuredContracts(Set<Contract> contracts) {
        this.setInsuredContracts(contracts);
        return this;
    }

    public BusinessPartner addInsuredContracts(Contract contract) {
        this.insuredContracts.add(contract);
        contract.setInsured(this);
        return this;
    }

    public BusinessPartner removeInsuredContracts(Contract contract) {
        this.insuredContracts.remove(contract);
        contract.setInsured(null);
        return this;
    }

    public Set<Contract> getInsurerContracts() {
        return this.insurerContracts;
    }

    public void setInsurerContracts(Set<Contract> contracts) {
        if (this.insurerContracts != null) {
            this.insurerContracts.forEach(i -> i.setInsurer(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setInsurer(this));
        }
        this.insurerContracts = contracts;
    }

    public BusinessPartner insurerContracts(Set<Contract> contracts) {
        this.setInsurerContracts(contracts);
        return this;
    }

    public BusinessPartner addInsurerContracts(Contract contract) {
        this.insurerContracts.add(contract);
        contract.setInsurer(this);
        return this;
    }

    public BusinessPartner removeInsurerContracts(Contract contract) {
        this.insurerContracts.remove(contract);
        contract.setInsurer(null);
        return this;
    }

    public Set<Contract> getBrokerContracts() {
        return this.brokerContracts;
    }

    public void setBrokerContracts(Set<Contract> contracts) {
        if (this.brokerContracts != null) {
            this.brokerContracts.forEach(i -> i.setBroker(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setBroker(this));
        }
        this.brokerContracts = contracts;
    }

    public BusinessPartner brokerContracts(Set<Contract> contracts) {
        this.setBrokerContracts(contracts);
        return this;
    }

    public BusinessPartner addBrokerContracts(Contract contract) {
        this.brokerContracts.add(contract);
        contract.setBroker(this);
        return this;
    }

    public BusinessPartner removeBrokerContracts(Contract contract) {
        this.brokerContracts.remove(contract);
        contract.setBroker(null);
        return this;
    }

    public Set<Contract> getReInsurerContracts() {
        return this.reInsurerContracts;
    }

    public void setReInsurerContracts(Set<Contract> contracts) {
        if (this.reInsurerContracts != null) {
            this.reInsurerContracts.forEach(i -> i.removeReinsurers(this));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.addReinsurers(this));
        }
        this.reInsurerContracts = contracts;
    }

    public BusinessPartner reInsurerContracts(Set<Contract> contracts) {
        this.setReInsurerContracts(contracts);
        return this;
    }

    public BusinessPartner addReInsurerContracts(Contract contract) {
        this.reInsurerContracts.add(contract);
        contract.getReinsurers().add(this);
        return this;
    }

    public BusinessPartner removeReInsurerContracts(Contract contract) {
        this.reInsurerContracts.remove(contract);
        contract.getReinsurers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessPartner)) {
            return false;
        }
        return getId() != null && getId().equals(((BusinessPartner) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessPartner{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", representativeName='" + getRepresentativeName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", agentType='" + getAgentType() + "'" +
            "}";
    }
}
