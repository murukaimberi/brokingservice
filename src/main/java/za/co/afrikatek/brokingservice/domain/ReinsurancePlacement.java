package za.co.afrikatek.brokingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReinsurancePlacement.
 */
@Entity
@Table(name = "reinsurance_placement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReinsurancePlacement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "ri_percentage_commission", precision = 21, scale = 2, nullable = false)
    private BigDecimal riPercentageCommission;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "ri_percentage_share", precision = 21, scale = 2, nullable = false)
    private BigDecimal riPercentageShare;

    @Column(name = "ri_total_sum_insured", precision = 21, scale = 2)
    private BigDecimal riTotalSumInsured;

    @Column(name = "ri_limit_of_liability", precision = 21, scale = 2)
    private BigDecimal riLimitOfLiability;

    @NotNull
    @Column(name = "gross_premium_hundred", precision = 21, scale = 2, nullable = false)
    private BigDecimal grossPremiumHundred;

    @Column(name = "ri_premium", precision = 21, scale = 2)
    private BigDecimal riPremium;

    @Column(name = "ri_commission", precision = 21, scale = 2)
    private BigDecimal riCommission;

    @Column(name = "net_due_from_insurer", precision = 21, scale = 2)
    private BigDecimal netDueFromInsurer;

    @JsonIgnoreProperties(
        value = {
            "insured", "insurer", "broker", "reinsurers", "classOfBusiness", "subClassOfBusiness", "country", "reinsurancePlacement",
        },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Contract contract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReinsurancePlacement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRiPercentageCommission() {
        return this.riPercentageCommission;
    }

    public ReinsurancePlacement riPercentageCommission(BigDecimal riPercentageCommission) {
        this.setRiPercentageCommission(riPercentageCommission);
        return this;
    }

    public void setRiPercentageCommission(BigDecimal riPercentageCommission) {
        this.riPercentageCommission = riPercentageCommission;
    }

    public BigDecimal getRiPercentageShare() {
        return this.riPercentageShare;
    }

    public ReinsurancePlacement riPercentageShare(BigDecimal riPercentageShare) {
        this.setRiPercentageShare(riPercentageShare);
        return this;
    }

    public void setRiPercentageShare(BigDecimal riPercentageShare) {
        this.riPercentageShare = riPercentageShare;
    }

    public BigDecimal getRiTotalSumInsured() {
        return this.riTotalSumInsured;
    }

    public ReinsurancePlacement riTotalSumInsured(BigDecimal riTotalSumInsured) {
        this.setRiTotalSumInsured(riTotalSumInsured);
        return this;
    }

    public void setRiTotalSumInsured(BigDecimal riTotalSumInsured) {
        this.riTotalSumInsured = riTotalSumInsured;
    }

    public BigDecimal getRiLimitOfLiability() {
        return this.riLimitOfLiability;
    }

    public ReinsurancePlacement riLimitOfLiability(BigDecimal riLimitOfLiability) {
        this.setRiLimitOfLiability(riLimitOfLiability);
        return this;
    }

    public void setRiLimitOfLiability(BigDecimal riLimitOfLiability) {
        this.riLimitOfLiability = riLimitOfLiability;
    }

    public BigDecimal getGrossPremiumHundred() {
        return this.grossPremiumHundred;
    }

    public ReinsurancePlacement grossPremiumHundred(BigDecimal grossPremiumHundred) {
        this.setGrossPremiumHundred(grossPremiumHundred);
        return this;
    }

    public void setGrossPremiumHundred(BigDecimal grossPremiumHundred) {
        this.grossPremiumHundred = grossPremiumHundred;
    }

    public BigDecimal getRiPremium() {
        return this.riPremium;
    }

    public ReinsurancePlacement riPremium(BigDecimal riPremium) {
        this.setRiPremium(riPremium);
        return this;
    }

    public void setRiPremium(BigDecimal riPremium) {
        this.riPremium = riPremium;
    }

    public BigDecimal getRiCommission() {
        return this.riCommission;
    }

    public ReinsurancePlacement riCommission(BigDecimal riCommission) {
        this.setRiCommission(riCommission);
        return this;
    }

    public void setRiCommission(BigDecimal riCommission) {
        this.riCommission = riCommission;
    }

    public BigDecimal getNetDueFromInsurer() {
        return this.netDueFromInsurer;
    }

    public ReinsurancePlacement netDueFromInsurer(BigDecimal netDueFromInsurer) {
        this.setNetDueFromInsurer(netDueFromInsurer);
        return this;
    }

    public void setNetDueFromInsurer(BigDecimal netDueFromInsurer) {
        this.netDueFromInsurer = netDueFromInsurer;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public ReinsurancePlacement contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReinsurancePlacement)) {
            return false;
        }
        return getId() != null && getId().equals(((ReinsurancePlacement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReinsurancePlacement{" +
            "id=" + getId() +
            ", riPercentageCommission=" + getRiPercentageCommission() +
            ", riPercentageShare=" + getRiPercentageShare() +
            ", riTotalSumInsured=" + getRiTotalSumInsured() +
            ", riLimitOfLiability=" + getRiLimitOfLiability() +
            ", grossPremiumHundred=" + getGrossPremiumHundred() +
            ", riPremium=" + getRiPremium() +
            ", riCommission=" + getRiCommission() +
            ", netDueFromInsurer=" + getNetDueFromInsurer() +
            "}";
    }
}
