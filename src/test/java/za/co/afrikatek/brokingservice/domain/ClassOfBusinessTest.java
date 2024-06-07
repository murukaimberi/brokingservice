package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.ClassOfBusinessTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.ContractTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.InsuranceTypeTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.SubClassOfBusinessTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class ClassOfBusinessTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassOfBusiness.class);
        ClassOfBusiness classOfBusiness1 = getClassOfBusinessSample1();
        ClassOfBusiness classOfBusiness2 = new ClassOfBusiness();
        assertThat(classOfBusiness1).isNotEqualTo(classOfBusiness2);

        classOfBusiness2.setId(classOfBusiness1.getId());
        assertThat(classOfBusiness1).isEqualTo(classOfBusiness2);

        classOfBusiness2 = getClassOfBusinessSample2();
        assertThat(classOfBusiness1).isNotEqualTo(classOfBusiness2);
    }

    @Test
    void subClassOfBusinessesTest() {
        ClassOfBusiness classOfBusiness = getClassOfBusinessRandomSampleGenerator();
        SubClassOfBusiness subClassOfBusinessBack = getSubClassOfBusinessRandomSampleGenerator();

        classOfBusiness.addSubClassOfBusinesses(subClassOfBusinessBack);
        assertThat(classOfBusiness.getSubClassOfBusinesses()).containsOnly(subClassOfBusinessBack);
        assertThat(subClassOfBusinessBack.getClassOfBusiness()).isEqualTo(classOfBusiness);

        classOfBusiness.removeSubClassOfBusinesses(subClassOfBusinessBack);
        assertThat(classOfBusiness.getSubClassOfBusinesses()).doesNotContain(subClassOfBusinessBack);
        assertThat(subClassOfBusinessBack.getClassOfBusiness()).isNull();

        classOfBusiness.subClassOfBusinesses(new HashSet<>(Set.of(subClassOfBusinessBack)));
        assertThat(classOfBusiness.getSubClassOfBusinesses()).containsOnly(subClassOfBusinessBack);
        assertThat(subClassOfBusinessBack.getClassOfBusiness()).isEqualTo(classOfBusiness);

        classOfBusiness.setSubClassOfBusinesses(new HashSet<>());
        assertThat(classOfBusiness.getSubClassOfBusinesses()).doesNotContain(subClassOfBusinessBack);
        assertThat(subClassOfBusinessBack.getClassOfBusiness()).isNull();
    }

    @Test
    void contractsTest() {
        ClassOfBusiness classOfBusiness = getClassOfBusinessRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        classOfBusiness.addContracts(contractBack);
        assertThat(classOfBusiness.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getClassOfBusiness()).isEqualTo(classOfBusiness);

        classOfBusiness.removeContracts(contractBack);
        assertThat(classOfBusiness.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getClassOfBusiness()).isNull();

        classOfBusiness.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(classOfBusiness.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getClassOfBusiness()).isEqualTo(classOfBusiness);

        classOfBusiness.setContracts(new HashSet<>());
        assertThat(classOfBusiness.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getClassOfBusiness()).isNull();
    }

    @Test
    void insuranceTypeTest() {
        ClassOfBusiness classOfBusiness = getClassOfBusinessRandomSampleGenerator();
        InsuranceType insuranceTypeBack = getInsuranceTypeRandomSampleGenerator();

        classOfBusiness.setInsuranceType(insuranceTypeBack);
        assertThat(classOfBusiness.getInsuranceType()).isEqualTo(insuranceTypeBack);

        classOfBusiness.insuranceType(null);
        assertThat(classOfBusiness.getInsuranceType()).isNull();
    }
}
