package za.co.afrikatek.brokingservice.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ReinsurancePlacementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReinsurancePlacement getReinsurancePlacementSample1() {
        return new ReinsurancePlacement().id(1L);
    }

    public static ReinsurancePlacement getReinsurancePlacementSample2() {
        return new ReinsurancePlacement().id(2L);
    }

    public static ReinsurancePlacement getReinsurancePlacementRandomSampleGenerator() {
        return new ReinsurancePlacement().id(longCount.incrementAndGet());
    }
}
