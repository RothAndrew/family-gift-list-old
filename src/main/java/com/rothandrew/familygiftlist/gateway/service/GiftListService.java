package com.rothandrew.familygiftlist.gateway.service;

import com.rothandrew.familygiftlist.gateway.service.dto.GiftListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing GiftList.
 */
public interface GiftListService {

    /**
     * Save a giftList.
     *
     * @param giftListDTO the entity to save
     * @return the persisted entity
     */
    GiftListDTO save(GiftListDTO giftListDTO);

    /**
     * Get all the giftLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GiftListDTO> findAll(Pageable pageable);


    /**
     * Get the "id" giftList.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GiftListDTO> findOne(Long id);

    /**
     * Delete the "id" giftList.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
