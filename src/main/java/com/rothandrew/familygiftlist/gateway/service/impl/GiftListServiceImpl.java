package com.rothandrew.familygiftlist.gateway.service.impl;

import com.rothandrew.familygiftlist.gateway.service.GiftListService;
import com.rothandrew.familygiftlist.gateway.domain.GiftList;
import com.rothandrew.familygiftlist.gateway.repository.GiftListRepository;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftListDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.GiftListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing GiftList.
 */
@Service
@Transactional
public class GiftListServiceImpl implements GiftListService {

    private final Logger log = LoggerFactory.getLogger(GiftListServiceImpl.class);

    private final GiftListRepository giftListRepository;

    private final GiftListMapper giftListMapper;

    public GiftListServiceImpl(GiftListRepository giftListRepository, GiftListMapper giftListMapper) {
        this.giftListRepository = giftListRepository;
        this.giftListMapper = giftListMapper;
    }

    /**
     * Save a giftList.
     *
     * @param giftListDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GiftListDTO save(GiftListDTO giftListDTO) {
        log.debug("Request to save GiftList : {}", giftListDTO);
        GiftList giftList = giftListMapper.toEntity(giftListDTO);
        giftList = giftListRepository.save(giftList);
        return giftListMapper.toDto(giftList);
    }

    /**
     * Get all the giftLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GiftListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GiftLists");
        return giftListRepository.findAll(pageable)
            .map(giftListMapper::toDto);
    }


    /**
     * Get one giftList by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GiftListDTO> findOne(Long id) {
        log.debug("Request to get GiftList : {}", id);
        return giftListRepository.findById(id)
            .map(giftListMapper::toDto);
    }

    /**
     * Delete the giftList by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GiftList : {}", id);
        giftListRepository.deleteById(id);
    }
}
