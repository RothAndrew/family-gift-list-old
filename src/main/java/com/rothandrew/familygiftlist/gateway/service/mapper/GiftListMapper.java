package com.rothandrew.familygiftlist.gateway.service.mapper;

import com.rothandrew.familygiftlist.gateway.domain.*;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GiftList and its DTO GiftListDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface GiftListMapper extends EntityMapper<GiftListDTO, GiftList> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    GiftListDTO toDto(GiftList giftList);

    @Mapping(target = "gifts", ignore = true)
    @Mapping(source = "userId", target = "user")
    GiftList toEntity(GiftListDTO giftListDTO);

    default GiftList fromId(Long id) {
        if (id == null) {
            return null;
        }
        GiftList giftList = new GiftList();
        giftList.setId(id);
        return giftList;
    }
}
