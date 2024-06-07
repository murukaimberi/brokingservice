package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.ContractHistoryTestSamples.*;

import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class ContractHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractHistory.class);
        ContractHistory contractHistory1 = getContractHistorySample1();
        ContractHistory contractHistory2 = new ContractHistory();
        assertThat(contractHistory1).isNotEqualTo(contractHistory2);

        contractHistory2.setId(contractHistory1.getId());
        assertThat(contractHistory1).isEqualTo(contractHistory2);

        contractHistory2 = getContractHistorySample2();
        assertThat(contractHistory1).isNotEqualTo(contractHistory2);
    }
}
