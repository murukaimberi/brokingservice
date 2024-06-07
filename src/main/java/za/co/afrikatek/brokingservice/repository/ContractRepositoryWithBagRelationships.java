package za.co.afrikatek.brokingservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import za.co.afrikatek.brokingservice.domain.Contract;

public interface ContractRepositoryWithBagRelationships {
    Optional<Contract> fetchBagRelationships(Optional<Contract> contract);

    List<Contract> fetchBagRelationships(List<Contract> contracts);

    Page<Contract> fetchBagRelationships(Page<Contract> contracts);
}
