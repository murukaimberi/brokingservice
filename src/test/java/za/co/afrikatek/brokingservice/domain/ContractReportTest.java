package za.co.afrikatek.brokingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static za.co.afrikatek.brokingservice.domain.ContractReportTestSamples.*;

import org.junit.jupiter.api.Test;
import za.co.afrikatek.brokingservice.web.rest.TestUtil;

class ContractReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractReport.class);
        ContractReport contractReport1 = getContractReportSample1();
        ContractReport contractReport2 = new ContractReport();
        assertThat(contractReport1).isNotEqualTo(contractReport2);

        contractReport2.setId(contractReport1.getId());
        assertThat(contractReport1).isEqualTo(contractReport2);

        contractReport2 = getContractReportSample2();
        assertThat(contractReport1).isNotEqualTo(contractReport2);
    }
}
