package za.co.afrikatek.brokingservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BusinessPartnerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BusinessPartner getBusinessPartnerSample1() {
        return new BusinessPartner()
            .id(1L)
            .name("name1")
            .representativeName("representativeName1")
            .email("email1")
            .phoneNumber("phoneNumber1");
    }

    public static BusinessPartner getBusinessPartnerSample2() {
        return new BusinessPartner()
            .id(2L)
            .name("name2")
            .representativeName("representativeName2")
            .email("email2")
            .phoneNumber("phoneNumber2");
    }

    public static BusinessPartner getBusinessPartnerRandomSampleGenerator() {
        return new BusinessPartner()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .representativeName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
