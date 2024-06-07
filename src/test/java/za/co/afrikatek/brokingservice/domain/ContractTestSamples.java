package za.co.afrikatek.brokingservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContractTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Contract getContractSample1() {
        return new Contract().id(1L).currency("currency1").uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Contract getContractSample2() {
        return new Contract().id(2L).currency("currency2").uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Contract getContractRandomSampleGenerator() {
        return new Contract().id(longCount.incrementAndGet()).currency(UUID.randomUUID().toString()).uuid(UUID.randomUUID());
    }
}
