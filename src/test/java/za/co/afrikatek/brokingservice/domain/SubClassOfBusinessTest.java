package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.ClassOfBusinessTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.ContractTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.SubClassOfBusinessTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class SubClassOfBusinessTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubClassOfBusiness.class);
        SubClassOfBusiness subClassOfBusiness1 = getSubClassOfBusinessSample1();
        SubClassOfBusiness subClassOfBusiness2 = new SubClassOfBusiness();
        assertThat(subClassOfBusiness1).isNotEqualTo(subClassOfBusiness2);

        subClassOfBusiness2.setId(subClassOfBusiness1.getId());
        assertThat(subClassOfBusiness1).isEqualTo(subClassOfBusiness2);

        subClassOfBusiness2 = getSubClassOfBusinessSample2();
        assertThat(subClassOfBusiness1).isNotEqualTo(subClassOfBusiness2);
    }

    @Test
    void contractsTest() {
        SubClassOfBusiness subClassOfBusiness = getSubClassOfBusinessRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        subClassOfBusiness.addContracts(contractBack);
        assertThat(subClassOfBusiness.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getSubClassOfBusiness()).isEqualTo(subClassOfBusiness);

        subClassOfBusiness.removeContracts(contractBack);
        assertThat(subClassOfBusiness.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getSubClassOfBusiness()).isNull();

        subClassOfBusiness.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(subClassOfBusiness.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getSubClassOfBusiness()).isEqualTo(subClassOfBusiness);

        subClassOfBusiness.setContracts(new HashSet<>());
        assertThat(subClassOfBusiness.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getSubClassOfBusiness()).isNull();
    }

    @Test
    void classOfBusinessTest() {
        SubClassOfBusiness subClassOfBusiness = getSubClassOfBusinessRandomSampleGenerator();
        ClassOfBusiness classOfBusinessBack = getClassOfBusinessRandomSampleGenerator();

        subClassOfBusiness.setClassOfBusiness(classOfBusinessBack);
        assertThat(subClassOfBusiness.getClassOfBusiness()).isEqualTo(classOfBusinessBack);

        subClassOfBusiness.classOfBusiness(null);
        assertThat(subClassOfBusiness.getClassOfBusiness()).isNull();
    }
}
