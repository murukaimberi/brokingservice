package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.ClassOfBusinessTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.InsuranceTypeTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class InsuranceTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceType.class);
        InsuranceType insuranceType1 = getInsuranceTypeSample1();
        InsuranceType insuranceType2 = new InsuranceType();
        assertThat(insuranceType1).isNotEqualTo(insuranceType2);

        insuranceType2.setId(insuranceType1.getId());
        assertThat(insuranceType1).isEqualTo(insuranceType2);

        insuranceType2 = getInsuranceTypeSample2();
        assertThat(insuranceType1).isNotEqualTo(insuranceType2);
    }

    @Test
    void classOfBusinessesTest() {
        InsuranceType insuranceType = getInsuranceTypeRandomSampleGenerator();
        ClassOfBusiness classOfBusinessBack = getClassOfBusinessRandomSampleGenerator();

        insuranceType.addClassOfBusinesses(classOfBusinessBack);
        assertThat(insuranceType.getClassOfBusinesses()).containsOnly(classOfBusinessBack);
        assertThat(classOfBusinessBack.getInsuranceType()).isEqualTo(insuranceType);

        insuranceType.removeClassOfBusinesses(classOfBusinessBack);
        assertThat(insuranceType.getClassOfBusinesses()).doesNotContain(classOfBusinessBack);
        assertThat(classOfBusinessBack.getInsuranceType()).isNull();

        insuranceType.classOfBusinesses(new HashSet<>(Set.of(classOfBusinessBack)));
        assertThat(insuranceType.getClassOfBusinesses()).containsOnly(classOfBusinessBack);
        assertThat(classOfBusinessBack.getInsuranceType()).isEqualTo(insuranceType);

        insuranceType.setClassOfBusinesses(new HashSet<>());
        assertThat(insuranceType.getClassOfBusinesses()).doesNotContain(classOfBusinessBack);
        assertThat(classOfBusinessBack.getInsuranceType()).isNull();
    }
}
