package com.rothandrew.familygiftlist.gateway.repository;

import com.rothandrew.familygiftlist.gateway.domain.Family;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Family entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilyRepository extends JpaRepository<Family, Long>, JpaSpecificationExecutor<Family> {

    @Query(value = "select distinct family from Family family left join fetch family.members left join fetch family.owners left join fetch family.admins",
        countQuery = "select count(distinct family) from Family family")
    Page<Family> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct family from Family family left join fetch family.members left join fetch family.owners left join fetch family.admins")
    List<Family> findAllWithEagerRelationships();

    @Query("select family from Family family left join fetch family.members left join fetch family.owners left join fetch family.admins where family.id =:id")
    Optional<Family> findOneWithEagerRelationships(@Param("id") Long id);

}
