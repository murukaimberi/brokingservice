package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.AddressTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.BusinessPartnerTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.ContractTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.ShareTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class BusinessPartnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessPartner.class);
        BusinessPartner businessPartner1 = getBusinessPartnerSample1();
        BusinessPartner businessPartner2 = new BusinessPartner();
        assertThat(businessPartner1).isNotEqualTo(businessPartner2);

        businessPartner2.setId(businessPartner1.getId());
        assertThat(businessPartner1).isEqualTo(businessPartner2);

        businessPartner2 = getBusinessPartnerSample2();
        assertThat(businessPartner1).isNotEqualTo(businessPartner2);
    }

    @Test
    void awardedBusinessSharesTest() {
        BusinessPartner businessPartner = getBusinessPartnerRandomSampleGenerator();
        Share shareBack = getShareRandomSampleGenerator();

        businessPartner.addAwardedBusinessShares(shareBack);
        assertThat(businessPartner.getAwardedBusinessShares()).containsOnly(shareBack);
        assertThat(shareBack.getReInsurer()).isEqualTo(businessPartner);

        businessPartner.removeAwardedBusinessShares(shareBack);
        assertThat(businessPartner.getAwardedBusinessShares()).doesNotContain(shareBack);
        assertThat(shareBack.getReInsurer()).isNull();

        businessPartner.awardedBusinessShares(new HashSet<>(Set.of(shareBack)));
        assertThat(businessPartner.getAwardedBusinessShares()).containsOnly(shareBack);
        assertThat(shareBack.getReInsurer()).isEqualTo(businessPartner);

        businessPartner.setAwardedBusinessShares(new HashSet<>());
        assertThat(businessPartner.getAwardedBusinessShares()).doesNotContain(shareBack);
        assertThat(shareBack.getReInsurer()).isNull();
    }

    @Test
    void addressesTest() {
        BusinessPartner businessPartner = getBusinessPartnerRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        businessPartner.addAddresses(addressBack);
        assertThat(businessPartner.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCedent()).isEqualTo(businessPartner);

        businessPartner.removeAddresses(addressBack);
        assertThat(businessPartner.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCedent()).isNull();

        businessPartner.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(businessPartner.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCedent()).isEqualTo(businessPartner);

        businessPartner.setAddresses(new HashSet<>());
        assertThat(businessPartner.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCedent()).isNull();
    }

    @Test
    void insuredContractsTest() {
        BusinessPartner businessPartner = getBusinessPartnerRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        businessPartner.addInsuredContracts(contractBack);
        assertThat(businessPartner.getInsuredContracts()).containsOnly(contractBack);
        assertThat(contractBack.getInsured()).isEqualTo(businessPartner);

        businessPartner.removeInsuredContracts(contractBack);
        assertThat(businessPartner.getInsuredContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getInsured()).isNull();

        businessPartner.insuredContracts(new HashSet<>(Set.of(contractBack)));
        assertThat(businessPartner.getInsuredContracts()).containsOnly(contractBack);
        assertThat(contractBack.getInsured()).isEqualTo(businessPartner);

        businessPartner.setInsuredContracts(new HashSet<>());
        assertThat(businessPartner.getInsuredContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getInsured()).isNull();
    }

    @Test
    void insurerContractsTest() {
        BusinessPartner businessPartner = getBusinessPartnerRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        businessPartner.addInsurerContracts(contractBack);
        assertThat(businessPartner.getInsurerContracts()).containsOnly(contractBack);
        assertThat(contractBack.getInsurer()).isEqualTo(businessPartner);

        businessPartner.removeInsurerContracts(contractBack);
        assertThat(businessPartner.getInsurerContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getInsurer()).isNull();

        businessPartner.insurerContracts(new HashSet<>(Set.of(contractBack)));
        assertThat(businessPartner.getInsurerContracts()).containsOnly(contractBack);
        assertThat(contractBack.getInsurer()).isEqualTo(businessPartner);

        businessPartner.setInsurerContracts(new HashSet<>());
        assertThat(businessPartner.getInsurerContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getInsurer()).isNull();
    }

    @Test
    void brokerContractsTest() {
        BusinessPartner businessPartner = getBusinessPartnerRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        businessPartner.addBrokerContracts(contractBack);
        assertThat(businessPartner.getBrokerContracts()).containsOnly(contractBack);
        assertThat(contractBack.getBroker()).isEqualTo(businessPartner);

        businessPartner.removeBrokerContracts(contractBack);
        assertThat(businessPartner.getBrokerContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getBroker()).isNull();

        businessPartner.brokerContracts(new HashSet<>(Set.of(contractBack)));
        assertThat(businessPartner.getBrokerContracts()).containsOnly(contractBack);
        assertThat(contractBack.getBroker()).isEqualTo(businessPartner);

        businessPartner.setBrokerContracts(new HashSet<>());
        assertThat(businessPartner.getBrokerContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getBroker()).isNull();
    }

    @Test
    void reInsurerContractsTest() {
        BusinessPartner businessPartner = getBusinessPartnerRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        businessPartner.addReInsurerContracts(contractBack);
        assertThat(businessPartner.getReInsurerContracts()).containsOnly(contractBack);
        assertThat(contractBack.getReinsurers()).containsOnly(businessPartner);

        businessPartner.removeReInsurerContracts(contractBack);
        assertThat(businessPartner.getReInsurerContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getReinsurers()).doesNotContain(businessPartner);

        businessPartner.reInsurerContracts(new HashSet<>(Set.of(contractBack)));
        assertThat(businessPartner.getReInsurerContracts()).containsOnly(contractBack);
        assertThat(contractBack.getReinsurers()).containsOnly(businessPartner);

        businessPartner.setReInsurerContracts(new HashSet<>());
        assertThat(businessPartner.getReInsurerContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getReinsurers()).doesNotContain(businessPartner);
    }
}
