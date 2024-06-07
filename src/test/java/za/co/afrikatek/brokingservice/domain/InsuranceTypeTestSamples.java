package za.co.afrikatek.brokingservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InsuranceTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InsuranceType getInsuranceTypeSample1() {
        return new InsuranceType().id(1L).name("name1");
    }

    public static InsuranceType getInsuranceTypeSample2() {
        return new InsuranceType().id(2L).name("name2");
    }

    public static InsuranceType getInsuranceTypeRandomSampleGenerator() {
        return new InsuranceType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
