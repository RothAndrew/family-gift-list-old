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

import com.rothandrew.familygiftlist.gateway.domain.GiftList;
import com.rothandrew.familygiftlist.gateway.domain.*; // for static metamodels
import com.rothandrew.familygiftlist.gateway.repository.GiftListRepository;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftListCriteria;

import com.rothandrew.familygiftlist.gateway.service.dto.GiftListDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.GiftListMapper;

/**
 * Service for executing complex queries for GiftList entities in the database.
 * The main input is a {@link GiftListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GiftListDTO} or a {@link Page} of {@link GiftListDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GiftListQueryService extends QueryService<GiftList> {

    private final Logger log = LoggerFactory.getLogger(GiftListQueryService.class);

    private final GiftListRepository giftListRepository;

    private final GiftListMapper giftListMapper;

    public GiftListQueryService(GiftListRepository giftListRepository, GiftListMapper giftListMapper) {
        this.giftListRepository = giftListRepository;
        this.giftListMapper = giftListMapper;
    }

    /**
     * Return a {@link List} of {@link GiftListDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GiftListDTO> findByCriteria(GiftListCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GiftList> specification = createSpecification(criteria);
        return giftListMapper.toDto(giftListRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GiftListDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GiftListDTO> findByCriteria(GiftListCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GiftList> specification = createSpecification(criteria);
        return giftListRepository.findAll(specification, page)
            .map(giftListMapper::toDto);
    }

    /**
     * Function to convert GiftListCriteria to a {@link Specification}
     */
    private Specification<GiftList> createSpecification(GiftListCriteria criteria) {
        Specification<GiftList> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GiftList_.id));
            }
            if (criteria.getGiftsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGiftsId(), GiftList_.gifts, Gift_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), GiftList_.user, User_.id));
            }
        }
        return specification;
    }

}
