package com.rothandrew.familygiftlist.gateway.repository;

import com.rothandrew.familygiftlist.gateway.domain.GiftList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the GiftList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GiftListRepository extends JpaRepository<GiftList, Long>, JpaSpecificationExecutor<GiftList> {

    @Query("select gift_list from GiftList gift_list where gift_list.user.login = ?#{principal.username}")
    List<GiftList> findByUserIsCurrentUser();

}
