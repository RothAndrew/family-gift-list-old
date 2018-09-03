package com.rothandrew.familygiftlist.gateway.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.rothandrew.familygiftlist.gateway.domain.Family;
import com.rothandrew.familygiftlist.gateway.domain.*; // for static metamodels
import com.rothandrew.familygiftlist.gateway.repository.FamilyRepository;
import com.rothandrew.familygiftlist.gateway.service.dto.FamilyCriteria;

import com.rothandrew.familygiftlist.gateway.service.dto.FamilyDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.FamilyMapper;

/**
 * Service for executing complex queries for Family entities in the database.
 * The main input is a {@link FamilyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FamilyDTO} or a {@link Page} of {@link FamilyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FamilyQueryService extends QueryService<Family> {

    private final Logger log = LoggerFactory.getLogger(FamilyQueryService.class);

    private final FamilyRepository familyRepository;

    private final FamilyMapper familyMapper;

    public FamilyQueryService(FamilyRepository familyRepository, FamilyMapper familyMapper) {
        this.familyRepository = familyRepository;
        this.familyMapper = familyMapper;
    }

    /**
     * Return a {@link List} of {@link FamilyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FamilyDTO> findByCriteria(FamilyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Family> specification = createSpecification(criteria);
        return familyMapper.toDto(familyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FamilyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FamilyDTO> findByCriteria(FamilyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Family> specification = createSpecification(criteria);
        return familyRepository.findAll(specification, page)
            .map(familyMapper::toDto);
    }

    /**
     * Function to convert FamilyCriteria to a {@link Specification}
     */
    private Specification<Family> createSpecification(FamilyCriteria criteria) {
        Specification<Family> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Family_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Family_.name));
            }
            if (criteria.getMembersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMembersId(), Family_.members, User_.id));
            }
        }
        return specification;
    }

}
