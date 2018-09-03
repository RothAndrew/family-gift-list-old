package com.rothandrew.familygiftlist.gateway.service;

import com.rothandrew.familygiftlist.gateway.service.dto.GiftDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Gift.
 */
public interface GiftService {

    /**
     * Save a gift.
     *
     * @param giftDTO the entity to save
     * @return the persisted entity
     */
    GiftDTO save(GiftDTO giftDTO);

    /**
     * Get all the gifts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GiftDTO> findAll(Pageable pageable);


    /**
     * Get the "id" gift.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GiftDTO> findOne(Long id);

    /**
     * Delete the "id" gift.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
