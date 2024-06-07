package za.co.afrikatek.brokingservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.afrikatek.brokingservice.domain.ContractHistory;

/**
 * Spring Data JPA repository for the ContractHistory entity.
 */
@Repository
public interface ContractHistoryRepository extends JpaRepository<ContractHistory, Long> {
    @Query("select contractHistory from ContractHistory contractHistory where contractHistory.updated.login = ?#{authentication.name}")
    List<ContractHistory> findByUpdatedIsCurrentUser();

    @Query("select contractHistory from ContractHistory contractHistory where contractHistory.approved.login = ?#{authentication.name}")
    List<ContractHistory> findByApprovedIsCurrentUser();

    default Optional<ContractHistory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ContractHistory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ContractHistory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select contractHistory from ContractHistory contractHistory left join fetch contractHistory.updated left join fetch contractHistory.approved",
        countQuery = "select count(contractHistory) from ContractHistory contractHistory"
    )
    Page<ContractHistory> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select contractHistory from ContractHistory contractHistory left join fetch contractHistory.updated left join fetch contractHistory.approved"
    )
    List<ContractHistory> findAllWithToOneRelationships();

    @Query(
        "select contractHistory from ContractHistory contractHistory left join fetch contractHistory.updated left join fetch contractHistory.approved where contractHistory.id =:id"
    )
    Optional<ContractHistory> findOneWithToOneRelationships(@Param("id") Long id);
}
