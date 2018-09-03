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

import com.rothandrew.familygiftlist.gateway.domain.Gift;
import com.rothandrew.familygiftlist.gateway.domain.*; // for static metamodels
import com.rothandrew.familygiftlist.gateway.repository.GiftRepository;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftCriteria;

import com.rothandrew.familygiftlist.gateway.service.dto.GiftDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.GiftMapper;

/**
 * Service for executing complex queries for Gift entities in the database.
 * The main input is a {@link GiftCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GiftDTO} or a {@link Page} of {@link GiftDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GiftQueryService extends QueryService<Gift> {

    private final Logger log = LoggerFactory.getLogger(GiftQueryService.class);

    private final GiftRepository giftRepository;

    private final GiftMapper giftMapper;

    public GiftQueryService(GiftRepository giftRepository, GiftMapper giftMapper) {
        this.giftRepository = giftRepository;
        this.giftMapper = giftMapper;
    }

    /**
     * Return a {@link List} of {@link GiftDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GiftDTO> findByCriteria(GiftCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Gift> specification = createSpecification(criteria);
        return giftMapper.toDto(giftRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GiftDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GiftDTO> findByCriteria(GiftCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Gift> specification = createSpecification(criteria);
        return giftRepository.findAll(specification, page)
            .map(giftMapper::toDto);
    }

    /**
     * Function to convert GiftCriteria to a {@link Specification}
     */
    private Specification<Gift> createSpecification(GiftCriteria criteria) {
        Specification<Gift> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Gift_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Gift_.name));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Gift_.url));
            }
            if (criteria.getGiftListId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGiftListId(), Gift_.giftList, GiftList_.id));
            }
        }
        return specification;
    }

}
