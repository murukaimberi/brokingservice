package za.co.afrikatek.brokingservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.afrikatek.brokingservice.domain.Share;

/**
 * Spring Data JPA repository for the Share entity.
 */
@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    default Optional<Share> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Share> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Share> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select share from Share share left join fetch share.reInsurer", countQuery = "select count(share) from Share share")
    Page<Share> findAllWithToOneRelationships(Pageable pageable);

    @Query("select share from Share share left join fetch share.reInsurer")
    List<Share> findAllWithToOneRelationships();

    @Query("select share from Share share left join fetch share.reInsurer where share.id =:id")
    Optional<Share> findOneWithToOneRelationships(@Param("id") Long id);
}
