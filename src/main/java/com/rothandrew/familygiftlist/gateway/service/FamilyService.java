package com.rothandrew.familygiftlist.gateway.service;

import com.rothandrew.familygiftlist.gateway.service.dto.FamilyDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Family.
 */
public interface FamilyService {

    /**
     * Save a family.
     *
     * @param familyDTO the entity to save
     * @return the persisted entity
     */
    FamilyDTO save(FamilyDTO familyDTO);

    /**
     * Get all the families.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FamilyDTO> findAll(Pageable pageable);

    /**
     * Get all the Family with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<FamilyDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" family.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FamilyDTO> findOne(Long id);

    /**
     * Delete the "id" family.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
