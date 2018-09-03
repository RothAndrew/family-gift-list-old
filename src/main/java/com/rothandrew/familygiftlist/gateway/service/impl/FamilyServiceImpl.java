package com.rothandrew.familygiftlist.gateway.service.impl;

import com.rothandrew.familygiftlist.gateway.config.Constants;
import com.rothandrew.familygiftlist.gateway.domain.User;
import com.rothandrew.familygiftlist.gateway.security.AuthoritiesConstants;
import com.rothandrew.familygiftlist.gateway.security.SecurityUtils;
import com.rothandrew.familygiftlist.gateway.service.FamilyService;
import com.rothandrew.familygiftlist.gateway.domain.Family;
import com.rothandrew.familygiftlist.gateway.repository.FamilyRepository;
import com.rothandrew.familygiftlist.gateway.service.UserService;
import com.rothandrew.familygiftlist.gateway.service.dto.FamilyDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.FamilyMapper;
import com.rothandrew.familygiftlist.gateway.web.rest.FamilyResource;
import com.rothandrew.familygiftlist.gateway.web.rest.UserResource;
import com.rothandrew.familygiftlist.gateway.web.rest.errors.BadRequestAlertException;
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

    private final UserService userService;

    public FamilyServiceImpl(FamilyRepository familyRepository, FamilyMapper familyMapper, UserService userService) {
        this.familyRepository = familyRepository;
        this.familyMapper = familyMapper;
        this.userService = userService;
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

        Optional<User> optionalCurrentUser = this.userService.getUserWithAuthorities();
        if (!optionalCurrentUser.isPresent()){
            throw new BadRequestAlertException("Unable to get current user", UserResource.ENTITY_NAME, Constants.ERROR_KEY_UNABLE_TO_GET_CURRENT_USER);
        }

        if (optionalCurrentUser.get().getAuthorities().stream().noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN))){
            // Current user is not an admin
            if (familyDTO.getId() == null){
                // A Family is being created
                // For now, don't lock anything down for creating families.
            } else {
                // a Family is being updated
                // Only allow processing to continue if the user is an owner or admin of the existing Family
                Optional<FamilyDTO> optionalExistingFamily = this.familyRepository.findOneWithEagerRelationships(familyDTO.getId()).map(familyMapper::toDto);
                if (!optionalExistingFamily.isPresent()){
                    throw new BadRequestAlertException("Existing family not found", FamilyResource.ENTITY_NAME, Constants.ERROR_KEY_ENTITY_NOT_FOUND);
                }
                boolean isAdmin = optionalExistingFamily.get().getAdmins().stream().anyMatch(user -> user.getId().equals(optionalCurrentUser.get().getId()));
                boolean isOwner = optionalExistingFamily.get().getOwners().stream().anyMatch(user -> user.getId().equals(optionalCurrentUser.get().getId()));
                if (!isAdmin){
                    throw new BadRequestAlertException("You are not allowed to modify this entity", FamilyResource.ENTITY_NAME, Constants.ERROR_KEY_NOT_ALLOWED_TO_MODIFY_ENTITY);
                }
                if (!isOwner){
                    // Don't allow the user to change owners or admins unless they themselves are an owner
                    familyDTO.setOwners(optionalExistingFamily.get().getOwners());
                    familyDTO.setAdmins(optionalExistingFamily.get().getAdmins());
                }
            }
        }

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
