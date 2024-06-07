package za.co.afrikatek.brokingservice.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.afrikatek.brokingservice.domain.BusinessPartner;

/**
 * Spring Data JPA repository for the BusinessPartner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessPartnerRepository extends JpaRepository<BusinessPartner, Long> {}
