package za.co.afrikatek.brokingservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.afrikatek.brokingservice.domain.Contract;

/**
 * Spring Data JPA repository for the Contract entity.
 *
 * When extending this class, extend ContractRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ContractRepository extends ContractRepositoryWithBagRelationships, JpaRepository<Contract, Long> {
    default Optional<Contract> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Contract> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Contract> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select contract from Contract contract left join fetch contract.insured left join fetch contract.insurer left join fetch contract.broker left join fetch contract.classOfBusiness left join fetch contract.subClassOfBusiness left join fetch contract.country",
        countQuery = "select count(contract) from Contract contract"
    )
    Page<Contract> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select contract from Contract contract left join fetch contract.insured left join fetch contract.insurer left join fetch contract.broker left join fetch contract.classOfBusiness left join fetch contract.subClassOfBusiness left join fetch contract.country"
    )
    List<Contract> findAllWithToOneRelationships();

    @Query(
        "select contract from Contract contract left join fetch contract.insured left join fetch contract.insurer left join fetch contract.broker left join fetch contract.classOfBusiness left join fetch contract.subClassOfBusiness left join fetch contract.country where contract.id =:id"
    )
    Optional<Contract> findOneWithToOneRelationships(@Param("id") Long id);
}
