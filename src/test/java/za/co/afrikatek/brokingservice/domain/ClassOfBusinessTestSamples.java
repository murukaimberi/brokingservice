package za.co.afrikatek.brokingservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClassOfBusinessTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClassOfBusiness getClassOfBusinessSample1() {
        return new ClassOfBusiness().id(1L).name("name1");
    }

    public static ClassOfBusiness getClassOfBusinessSample2() {
        return new ClassOfBusiness().id(2L).name("name2");
    }

    public static ClassOfBusiness getClassOfBusinessRandomSampleGenerator() {
        return new ClassOfBusiness().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
