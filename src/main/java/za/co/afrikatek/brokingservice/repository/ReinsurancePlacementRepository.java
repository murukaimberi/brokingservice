package za.co.afrikatek.brokingservice.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.afrikatek.brokingservice.domain.ReinsurancePlacement;

/**
 * Spring Data JPA repository for the ReinsurancePlacement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReinsurancePlacementRepository extends JpaRepository<ReinsurancePlacement, Long> {}
