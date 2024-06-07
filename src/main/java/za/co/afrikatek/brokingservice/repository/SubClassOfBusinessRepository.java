package za.co.afrikatek.brokingservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.afrikatek.brokingservice.domain.SubClassOfBusiness;

/**
 * Spring Data JPA repository for the SubClassOfBusiness entity.
 */
@Repository
public interface SubClassOfBusinessRepository extends JpaRepository<SubClassOfBusiness, Long> {
    default Optional<SubClassOfBusiness> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SubClassOfBusiness> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SubClassOfBusiness> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select subClassOfBusiness from SubClassOfBusiness subClassOfBusiness left join fetch subClassOfBusiness.classOfBusiness",
        countQuery = "select count(subClassOfBusiness) from SubClassOfBusiness subClassOfBusiness"
    )
    Page<SubClassOfBusiness> findAllWithToOneRelationships(Pageable pageable);

    @Query("select subClassOfBusiness from SubClassOfBusiness subClassOfBusiness left join fetch subClassOfBusiness.classOfBusiness")
    List<SubClassOfBusiness> findAllWithToOneRelationships();

    @Query(
        "select subClassOfBusiness from SubClassOfBusiness subClassOfBusiness left join fetch subClassOfBusiness.classOfBusiness where subClassOfBusiness.id =:id"
    )
    Optional<SubClassOfBusiness> findOneWithToOneRelationships(@Param("id") Long id);
}
