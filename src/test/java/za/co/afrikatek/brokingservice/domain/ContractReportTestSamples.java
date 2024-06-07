package za.co.afrikatek.brokingservice.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ContractReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContractReport getContractReportSample1() {
        return new ContractReport().id(1L);
    }

    public static ContractReport getContractReportSample2() {
        return new ContractReport().id(2L);
    }

    public static ContractReport getContractReportRandomSampleGenerator() {
        return new ContractReport().id(longCount.incrementAndGet());
    }
}
