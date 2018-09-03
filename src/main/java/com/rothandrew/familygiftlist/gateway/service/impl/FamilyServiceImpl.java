package com.rothandrew.familygiftlist.gateway.service.impl;

import com.rothandrew.familygiftlist.gateway.service.FamilyService;
import com.rothandrew.familygiftlist.gateway.domain.Family;
import com.rothandrew.familygiftlist.gateway.repository.FamilyRepository;
import com.rothandrew.familygiftlist.gateway.service.dto.FamilyDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.FamilyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Family.
 */
@Service
@Transactional
public class FamilyServiceImpl implements FamilyService {

    private final Logger log = LoggerFactory.getLogger(FamilyServiceImpl.class);

    private final FamilyRepository familyRepository;

    private final FamilyMapper familyMapper;

    public FamilyServiceImpl(FamilyRepository familyRepository, FamilyMapper familyMapper) {
        this.familyRepository = familyRepository;
        this.familyMapper = familyMapper;
    }

    /**
     * Save a family.
     *
     * @param familyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FamilyDTO save(FamilyDTO familyDTO) {
        log.debug("Request to save Family : {}", familyDTO);
        Family family = familyMapper.toEntity(familyDTO);
        family = familyRepository.save(family);
        return familyMapper.toDto(family);
    }

    /**
     * Get all the families.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FamilyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Families");
        return familyRepository.findAll(pageable)
            .map(familyMapper::toDto);
    }

    /**
     * Get all the Family with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<FamilyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return familyRepository.findAllWithEagerRelationships(pageable).map(familyMapper::toDto);
    }
    

    /**
     * Get one family by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FamilyDTO> findOne(Long id) {
        log.debug("Request to get Family : {}", id);
        return familyRepository.findOneWithEagerRelationships(id)
            .map(familyMapper::toDto);
    }

    /**
     * Delete the family by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Family : {}", id);
        familyRepository.deleteById(id);
    }
}
