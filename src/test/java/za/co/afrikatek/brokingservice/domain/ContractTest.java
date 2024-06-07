package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.BusinessPartnerTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.ClassOfBusinessTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.ContractTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.CountryTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.ReinsurancePlacementTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.SubClassOfBusinessTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class ContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = getContractSample1();
        Contract contract2 = new Contract();
        assertThat(contract1).isNotEqualTo(contract2);

        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);

        contract2 = getContractSample2();
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    void insuredTest() {
        Contract contract = getContractRandomSampleGenerator();
        BusinessPartner businessPartnerBack = getBusinessPartnerRandomSampleGenerator();

        contract.setInsured(businessPartnerBack);
        assertThat(contract.getInsured()).isEqualTo(businessPartnerBack);

        contract.insured(null);
        assertThat(contract.getInsured()).isNull();
    }

    @Test
    void insurerTest() {
        Contract contract = getContractRandomSampleGenerator();
        BusinessPartner businessPartnerBack = getBusinessPartnerRandomSampleGenerator();

        contract.setInsurer(businessPartnerBack);
        assertThat(contract.getInsurer()).isEqualTo(businessPartnerBack);

        contract.insurer(null);
        assertThat(contract.getInsurer()).isNull();
    }

    @Test
    void brokerTest() {
        Contract contract = getContractRandomSampleGenerator();
        BusinessPartner businessPartnerBack = getBusinessPartnerRandomSampleGenerator();

        contract.setBroker(businessPartnerBack);
        assertThat(contract.getBroker()).isEqualTo(businessPartnerBack);

        contract.broker(null);
        assertThat(contract.getBroker()).isNull();
    }

    @Test
    void reinsurersTest() {
        Contract contract = getContractRandomSampleGenerator();
        BusinessPartner businessPartnerBack = getBusinessPartnerRandomSampleGenerator();

        contract.addReinsurers(businessPartnerBack);
        assertThat(contract.getReinsurers()).containsOnly(businessPartnerBack);

        contract.removeReinsurers(businessPartnerBack);
        assertThat(contract.getReinsurers()).doesNotContain(businessPartnerBack);

        contract.reinsurers(new HashSet<>(Set.of(businessPartnerBack)));
        assertThat(contract.getReinsurers()).containsOnly(businessPartnerBack);

        contract.setReinsurers(new HashSet<>());
        assertThat(contract.getReinsurers()).doesNotContain(businessPartnerBack);
    }

    @Test
    void classOfBusinessTest() {
        Contract contract = getContractRandomSampleGenerator();
        ClassOfBusiness classOfBusinessBack = getClassOfBusinessRandomSampleGenerator();

        contract.setClassOfBusiness(classOfBusinessBack);
        assertThat(contract.getClassOfBusiness()).isEqualTo(classOfBusinessBack);

        contract.classOfBusiness(null);
        assertThat(contract.getClassOfBusiness()).isNull();
    }

    @Test
    void subClassOfBusinessTest() {
        Contract contract = getContractRandomSampleGenerator();
        SubClassOfBusiness subClassOfBusinessBack = getSubClassOfBusinessRandomSampleGenerator();

        contract.setSubClassOfBusiness(subClassOfBusinessBack);
        assertThat(contract.getSubClassOfBusiness()).isEqualTo(subClassOfBusinessBack);

        contract.subClassOfBusiness(null);
        assertThat(contract.getSubClassOfBusiness()).isNull();
    }

    @Test
    void countryTest() {
        Contract contract = getContractRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        contract.setCountry(countryBack);
        assertThat(contract.getCountry()).isEqualTo(countryBack);

        contract.country(null);
        assertThat(contract.getCountry()).isNull();
    }

    @Test
    void reinsurancePlacementTest() {
        Contract contract = getContractRandomSampleGenerator();
        ReinsurancePlacement reinsurancePlacementBack = getReinsurancePlacementRandomSampleGenerator();

        contract.setReinsurancePlacement(reinsurancePlacementBack);
        assertThat(contract.getReinsurancePlacement()).isEqualTo(reinsurancePlacementBack);
        assertThat(reinsurancePlacementBack.getContract()).isEqualTo(contract);

        contract.reinsurancePlacement(null);
        assertThat(contract.getReinsurancePlacement()).isNull();
        assertThat(reinsurancePlacementBack.getContract()).isNull();
    }
}
