package za.co.afrikatek.brokingservice.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.afrikatek.brokingservice.domain.InsuranceType;

/**
 * Spring Data JPA repository for the InsuranceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceTypeRepository extends JpaRepository<InsuranceType, Long> {}
