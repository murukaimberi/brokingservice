package za.co.afrikatek.brokingservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubClassOfBusinessTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubClassOfBusiness getSubClassOfBusinessSample1() {
        return new SubClassOfBusiness().id(1L).name("name1");
    }

    public static SubClassOfBusiness getSubClassOfBusinessSample2() {
        return new SubClassOfBusiness().id(2L).name("name2");
    }

    public static SubClassOfBusiness getSubClassOfBusinessRandomSampleGenerator() {
        return new SubClassOfBusiness().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
