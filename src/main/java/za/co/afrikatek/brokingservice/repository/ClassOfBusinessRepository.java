package za.co.afrikatek.brokingservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.afrikatek.brokingservice.domain.ClassOfBusiness;

/**
 * Spring Data JPA repository for the ClassOfBusiness entity.
 */
@Repository
public interface ClassOfBusinessRepository extends JpaRepository<ClassOfBusiness, Long> {
    default Optional<ClassOfBusiness> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ClassOfBusiness> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ClassOfBusiness> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select classOfBusiness from ClassOfBusiness classOfBusiness left join fetch classOfBusiness.insuranceType",
        countQuery = "select count(classOfBusiness) from ClassOfBusiness classOfBusiness"
    )
    Page<ClassOfBusiness> findAllWithToOneRelationships(Pageable pageable);

    @Query("select classOfBusiness from ClassOfBusiness classOfBusiness left join fetch classOfBusiness.insuranceType")
    List<ClassOfBusiness> findAllWithToOneRelationships();

    @Query(
        "select classOfBusiness from ClassOfBusiness classOfBusiness left join fetch classOfBusiness.insuranceType where classOfBusiness.id =:id"
    )
    Optional<ClassOfBusiness> findOneWithToOneRelationships(@Param("id") Long id);
}
