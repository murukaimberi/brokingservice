package za.co.afrikatek.brokingservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import za.co.afrikatek.brokingservice.domain.Contract;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ContractRepositoryWithBagRelationshipsImpl implements ContractRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CONTRACTS_PARAMETER = "contracts";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Contract> fetchBagRelationships(Optional<Contract> contract) {
        return contract.map(this::fetchReinsurers);
    }

    @Override
    public Page<Contract> fetchBagRelationships(Page<Contract> contracts) {
        return new PageImpl<>(fetchBagRelationships(contracts.getContent()), contracts.getPageable(), contracts.getTotalElements());
    }

    @Override
    public List<Contract> fetchBagRelationships(List<Contract> contracts) {
        return Optional.of(contracts).map(this::fetchReinsurers).orElse(Collections.emptyList());
    }

    Contract fetchReinsurers(Contract result) {
        return entityManager
            .createQuery(
                "select contract from Contract contract left join fetch contract.reinsurers where contract.id = :id",
                Contract.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Contract> fetchReinsurers(List<Contract> contracts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, contracts.size()).forEach(index -> order.put(contracts.get(index).getId(), index));
        List<Contract> result = entityManager
            .createQuery(
                "select contract from Contract contract left join fetch contract.reinsurers where contract in :contracts",
                Contract.class
            )
            .setParameter(CONTRACTS_PARAMETER, contracts)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
