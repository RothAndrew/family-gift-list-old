package com.rothandrew.familygiftlist.gateway.service.mapper;

import com.rothandrew.familygiftlist.gateway.domain.*;
import com.rothandrew.familygiftlist.gateway.service.dto.FamilyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Family and its DTO FamilyDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FamilyMapper extends EntityMapper<FamilyDTO, Family> {



    default Family fromId(Long id) {
        if (id == null) {
            return null;
        }
        Family family = new Family();
        family.setId(id);
        return family;
    }
}
