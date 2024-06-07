package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.AddressTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.BusinessPartnerTestSamples.*;

import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class AddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = getAddressSample1();
        Address address2 = new Address();
        assertThat(address1).isNotEqualTo(address2);

        address2.setId(address1.getId());
        assertThat(address1).isEqualTo(address2);

        address2 = getAddressSample2();
        assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    void cedentTest() {
        Address address = getAddressRandomSampleGenerator();
        BusinessPartner businessPartnerBack = getBusinessPartnerRandomSampleGenerator();

        address.setCedent(businessPartnerBack);
        assertThat(address.getCedent()).isEqualTo(businessPartnerBack);

        address.cedent(null);
        assertThat(address.getCedent()).isNull();
    }
}
