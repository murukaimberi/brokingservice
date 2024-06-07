package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.ContractTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.CountryTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void contractTest() {
        Country country = getCountryRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        country.addContract(contractBack);
        assertThat(country.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getCountry()).isEqualTo(country);

        country.removeContract(contractBack);
        assertThat(country.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getCountry()).isNull();

        country.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(country.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getCountry()).isEqualTo(country);

        country.setContracts(new HashSet<>());
        assertThat(country.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getCountry()).isNull();
    }
}
