package za.co.afrikatek.brokingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Share.
 */
@Entity
@Table(name = "share")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Share implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "share_percentage", precision = 21, scale = 2, nullable = false)
    private BigDecimal sharePercentage;

    @Column(name = "total_sum_insured", precision = 21, scale = 2)
    private BigDecimal totalSumInsured;

    @Column(name = "limit_of_liability", precision = 21, scale = 2)
    private BigDecimal limitOfLiability;

    @Column(name = "gross_premium", precision = 21, scale = 2)
    private BigDecimal grossPremium;

    @Column(name = "ri_commission", precision = 21, scale = 2)
    private BigDecimal riCommission;

    @Column(name = "net_premium", precision = 21, scale = 2)
    private BigDecimal netPremium;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "brokerage", precision = 21, scale = 2, nullable = false)
    private BigDecimal brokerage;

    @Column(name = "brokerage_amount", precision = 21, scale = 2)
    private BigDecimal brokerageAmount;

    @Column(name = "net_payable", precision = 21, scale = 2)
    private BigDecimal netPayable;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "awardedBusinessShares", "addresses", "insuredContracts", "insurerContracts", "brokerContracts", "reInsurerContracts" },
        allowSetters = true
    )
    private BusinessPartner reInsurer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Share id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSharePercentage() {
        return this.sharePercentage;
    }

    public Share sharePercentage(BigDecimal sharePercentage) {
        this.setSharePercentage(sharePercentage);
        return this;
    }

    public void setSharePercentage(BigDecimal sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public BigDecimal getTotalSumInsured() {
        return this.totalSumInsured;
    }

    public Share totalSumInsured(BigDecimal totalSumInsured) {
        this.setTotalSumInsured(totalSumInsured);
        return this;
    }

    public void setTotalSumInsured(BigDecimal totalSumInsured) {
        this.totalSumInsured = totalSumInsured;
    }

    public BigDecimal getLimitOfLiability() {
        return this.limitOfLiability;
    }

    public Share limitOfLiability(BigDecimal limitOfLiability) {
        this.setLimitOfLiability(limitOfLiability);
        return this;
    }

    public void setLimitOfLiability(BigDecimal limitOfLiability) {
        this.limitOfLiability = limitOfLiability;
    }

    public BigDecimal getGrossPremium() {
        return this.grossPremium;
    }

    public Share grossPremium(BigDecimal grossPremium) {
        this.setGrossPremium(grossPremium);
        return this;
    }

    public void setGrossPremium(BigDecimal grossPremium) {
        this.grossPremium = grossPremium;
    }

    public BigDecimal getRiCommission() {
        return this.riCommission;
    }

    public Share riCommission(BigDecimal riCommission) {
        this.setRiCommission(riCommission);
        return this;
    }

    public void setRiCommission(BigDecimal riCommission) {
        this.riCommission = riCommission;
    }

    public BigDecimal getNetPremium() {
        return this.netPremium;
    }

    public Share netPremium(BigDecimal netPremium) {
        this.setNetPremium(netPremium);
        return this;
    }

    public void setNetPremium(BigDecimal netPremium) {
        this.netPremium = netPremium;
    }

    public BigDecimal getBrokerage() {
        return this.brokerage;
    }

    public Share brokerage(BigDecimal brokerage) {
        this.setBrokerage(brokerage);
        return this;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public BigDecimal getBrokerageAmount() {
        return this.brokerageAmount;
    }

    public Share brokerageAmount(BigDecimal brokerageAmount) {
        this.setBrokerageAmount(brokerageAmount);
        return this;
    }

    public void setBrokerageAmount(BigDecimal brokerageAmount) {
        this.brokerageAmount = brokerageAmount;
    }

    public BigDecimal getNetPayable() {
        return this.netPayable;
    }

    public Share netPayable(BigDecimal netPayable) {
        this.setNetPayable(netPayable);
        return this;
    }

    public void setNetPayable(BigDecimal netPayable) {
        this.netPayable = netPayable;
    }

    public BusinessPartner getReInsurer() {
        return this.reInsurer;
    }

    public void setReInsurer(BusinessPartner businessPartner) {
        this.reInsurer = businessPartner;
    }

    public Share reInsurer(BusinessPartner businessPartner) {
        this.setReInsurer(businessPartner);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Share)) {
            return false;
        }
        return getId() != null && getId().equals(((Share) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Share{" +
            "id=" + getId() +
            ", sharePercentage=" + getSharePercentage() +
            ", totalSumInsured=" + getTotalSumInsured() +
            ", limitOfLiability=" + getLimitOfLiability() +
            ", grossPremium=" + getGrossPremium() +
            ", riCommission=" + getRiCommission() +
            ", netPremium=" + getNetPremium() +
            ", brokerage=" + getBrokerage() +
            ", brokerageAmount=" + getBrokerageAmount() +
            ", netPayable=" + getNetPayable() +
            "}";
    }
}
