package za.co.afrikatek.brokingservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AddressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Address getAddressSample1() {
        return new Address().id(1L).city("city1").province("province1").state("state1").zipCode("zipCode1").country("country1");
    }

    public static Address getAddressSample2() {
        return new Address().id(2L).city("city2").province("province2").state("state2").zipCode("zipCode2").country("country2");
    }

    public static Address getAddressRandomSampleGenerator() {
        return new Address()
            .id(longCount.incrementAndGet())
            .city(UUID.randomUUID().toString())
            .province(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString());
    }
}
