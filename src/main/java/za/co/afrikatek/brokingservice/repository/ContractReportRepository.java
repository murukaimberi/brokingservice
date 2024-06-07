package za.co.afrikatek.brokingservice.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import za.co.afrikatek.brokingservice.domain.ContractReport;

/**
 * Spring Data JPA repository for the ContractReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractReportRepository extends JpaRepository<ContractReport, Long> {}
