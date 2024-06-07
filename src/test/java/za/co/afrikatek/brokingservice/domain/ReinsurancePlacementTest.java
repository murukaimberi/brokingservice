package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.ContractTestSamples.*;
import static za.co.afrikatek.brokingservice.domain.ReinsurancePlacementTestSamples.*;

import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class ReinsurancePlacementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReinsurancePlacement.class);
        ReinsurancePlacement reinsurancePlacement1 = getReinsurancePlacementSample1();
        ReinsurancePlacement reinsurancePlacement2 = new ReinsurancePlacement();
        assertThat(reinsurancePlacement1).isNotEqualTo(reinsurancePlacement2);

        reinsurancePlacement2.setId(reinsurancePlacement1.getId());
        assertThat(reinsurancePlacement1).isEqualTo(reinsurancePlacement2);

        reinsurancePlacement2 = getReinsurancePlacementSample2();
        assertThat(reinsurancePlacement1).isNotEqualTo(reinsurancePlacement2);
    }

    @Test
    void contractTest() {
        ReinsurancePlacement reinsurancePlacement = getReinsurancePlacementRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        reinsurancePlacement.setContract(contractBack);
        assertThat(reinsurancePlacement.getContract()).isEqualTo(contractBack);

        reinsurancePlacement.contract(null);
        assertThat(reinsurancePlacement.getContract()).isNull();
    }
}
