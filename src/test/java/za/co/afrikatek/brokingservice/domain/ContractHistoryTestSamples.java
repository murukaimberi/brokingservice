package za.co.afrikatek.brokingservice.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ContractHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContractHistory getContractHistorySample1() {
        return new ContractHistory().id(1L);
    }

    public static ContractHistory getContractHistorySample2() {
        return new ContractHistory().id(2L);
    }

    public static ContractHistory getContractHistoryRandomSampleGenerator() {
        return new ContractHistory().id(longCount.incrementAndGet());
    }
}
