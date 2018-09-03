package com.rothandrew.familygiftlist.gateway.service.impl;

import com.rothandrew.familygiftlist.gateway.service.GiftService;
import com.rothandrew.familygiftlist.gateway.domain.Gift;
import com.rothandrew.familygiftlist.gateway.repository.GiftRepository;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.GiftMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Gift.
 */
@Service
@Transactional
public class GiftServiceImpl implements GiftService {

    private final Logger log = LoggerFactory.getLogger(GiftServiceImpl.class);

    private final GiftRepository giftRepository;

    private final GiftMapper giftMapper;

    public GiftServiceImpl(GiftRepository giftRepository, GiftMapper giftMapper) {
        this.giftRepository = giftRepository;
        this.giftMapper = giftMapper;
    }

    /**
     * Save a gift.
     *
     * @param giftDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GiftDTO save(GiftDTO giftDTO) {
        log.debug("Request to save Gift : {}", giftDTO);
        Gift gift = giftMapper.toEntity(giftDTO);
        gift = giftRepository.save(gift);
        return giftMapper.toDto(gift);
    }

    /**
     * Get all the gifts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GiftDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Gifts");
        return giftRepository.findAll(pageable)
            .map(giftMapper::toDto);
    }


    /**
     * Get one gift by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GiftDTO> findOne(Long id) {
        log.debug("Request to get Gift : {}", id);
        return giftRepository.findById(id)
            .map(giftMapper::toDto);
    }

    /**
     * Delete the gift by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gift : {}", id);
        giftRepository.deleteById(id);
    }
}
