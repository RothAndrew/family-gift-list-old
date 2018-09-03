package com.rothandrew.familygiftlist.gateway.service.mapper;

import com.rothandrew.familygiftlist.gateway.domain.*;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Gift and its DTO GiftDTO.
 */
@Mapper(componentModel = "spring", uses = {GiftListMapper.class})
public interface GiftMapper extends EntityMapper<GiftDTO, Gift> {

    @Mapping(source = "giftList.id", target = "giftListId")
    GiftDTO toDto(Gift gift);

    @Mapping(source = "giftListId", target = "giftList")
    Gift toEntity(GiftDTO giftDTO);

    default Gift fromId(Long id) {
        if (id == null) {
            return null;
        }
        Gift gift = new Gift();
        gift.setId(id);
        return gift;
    }
}
