package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.BusinessPartnerTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.ShareTestSamples.*;

import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class ShareTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Share.class);
        Share share1 = getShareSample1();
        Share share2 = new Share();
        assertThat(share1).isNotEqualTo(share2);

        share2.setId(share1.getId());
        assertThat(share1).isEqualTo(share2);

        share2 = getShareSample2();
        assertThat(share1).isNotEqualTo(share2);
    }

    @Test
    void reInsurerTest() {
        Share share = getShareRandomSampleGenerator();
        BusinessPartner businessPartnerBack = getBusinessPartnerRandomSampleGenerator();

        share.setReInsurer(businessPartnerBack);
        assertThat(share.getReInsurer()).isEqualTo(businessPartnerBack);

        share.reInsurer(null);
        assertThat(share.getReInsurer()).isNull();
    }
}
