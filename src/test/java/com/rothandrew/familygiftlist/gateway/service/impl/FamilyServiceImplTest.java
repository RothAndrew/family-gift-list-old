package com.rothandrew.familygiftlist.gateway.service.impl;

import com.rothandrew.familygiftlist.gateway.config.Constants;
import com.rothandrew.familygiftlist.gateway.domain.Authority;
import com.rothandrew.familygiftlist.gateway.domain.Family;
import com.rothandrew.familygiftlist.gateway.domain.User;
import com.rothandrew.familygiftlist.gateway.repository.FamilyRepository;
import com.rothandrew.familygiftlist.gateway.security.AuthoritiesConstants;
import com.rothandrew.familygiftlist.gateway.service.FamilyService;
import com.rothandrew.familygiftlist.gateway.service.UserService;
import com.rothandrew.familygiftlist.gateway.service.dto.FamilyDTO;
import com.rothandrew.familygiftlist.gateway.service.dto.UserDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.FamilyMapper;
import com.rothandrew.familygiftlist.gateway.web.rest.FamilyResource;
import com.rothandrew.familygiftlist.gateway.web.rest.UserResource;
import com.rothandrew.familygiftlist.gateway.web.rest.errors.BadRequestAlertException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@SpringBootTest
public class FamilyServiceImplTest {

    @MockBean
    private FamilyRepository mockFamilyRepository;
    @MockBean
    private UserService mockUserService;
    @Autowired
    private FamilyService classUnderTest;
    @Autowired
    private FamilyMapper realFamilyMapper;

    @Test
    public void save_userNotPresent() {
        // GIVEN
        when(this.mockUserService.getUserWithAuthorities()).thenReturn(Optional.empty());
        FamilyDTO familyDTO = createSampleFamilyDTO();

        // WHEN
        try {
            this.classUnderTest.save(familyDTO);
            fail("Expected BadRequestAlertException");
        } catch (BadRequestAlertException e) {
            // THEN
            assertThat(e.getEntityName(), equalTo(UserResource.ENTITY_NAME));
            assertThat(e.getErrorKey(), equalTo(Constants.ERROR_KEY_UNABLE_TO_GET_CURRENT_USER));
        }
    }

    @Test
    public void save_userPresent_isAdmin() {
        // GIVEN
        User mockUser = mock(User.class);
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authoritySet.add(authority);
        when(mockUser.getAuthorities()).thenReturn(authoritySet);
        when(this.mockUserService.getUserWithAuthorities()).thenReturn(Optional.of(mockUser));

        FamilyDTO existingFamilyDTO = createSampleFamilyDTO();
        when(this.mockFamilyRepository.findOneWithEagerRelationships(existingFamilyDTO.getId())).thenReturn(Optional.of(this.realFamilyMapper.toEntity(existingFamilyDTO)));

        FamilyDTO modifiedFamilyDTO = createSampleFamilyDTO();
        modifiedFamilyDTO.setName("something else");
        UserDTO newUserDTO = createSampleUserDTO();
        newUserDTO.setId(newUserDTO.getId() + 1);
        newUserDTO.setLogin(newUserDTO.getLogin() + "1");
        Set<UserDTO> newOwners = modifiedFamilyDTO.getOwners();
        newOwners.add(newUserDTO);
        Set<UserDTO> newAdmins = modifiedFamilyDTO.getAdmins();
        newAdmins.add(newUserDTO);
        Set<UserDTO> newMembers = modifiedFamilyDTO.getMembers();
        newMembers.add(newUserDTO);
        modifiedFamilyDTO.setOwners(newOwners);
        modifiedFamilyDTO.setAdmins(newAdmins);
        modifiedFamilyDTO.setMembers(newMembers);

        // WHEN
        this.classUnderTest.save(modifiedFamilyDTO);

        // THEN
        verify(this.mockFamilyRepository, times(1)).save(this.realFamilyMapper.toEntity(modifiedFamilyDTO));
    }

    @Test
    public void save_userPresent_notAdmin_creatingFamily() {
        // GIVEN
        User mockUser = mock(User.class);
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authoritySet.add(authority);
        when(mockUser.getAuthorities()).thenReturn(authoritySet);
        when(this.mockUserService.getUserWithAuthorities()).thenReturn(Optional.of(mockUser));

        FamilyDTO familyDTO = createSampleFamilyDTO();
        familyDTO.setId(null);

        // WHEN
        this.classUnderTest.save(familyDTO);

        // THEN
        ArgumentCaptor<Family> captor = ArgumentCaptor.forClass(Family.class);
        verify(this.mockFamilyRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getId(), equalTo(familyDTO.getId()));
        assertThat(captor.getValue().getName(), equalTo(familyDTO.getName()));
        assertThat(captor.getValue().getMembers().size(), equalTo(familyDTO.getMembers().size()));
        assertThat(captor.getValue().getAdmins().size(), equalTo(familyDTO.getAdmins().size()));
        assertThat(captor.getValue().getOwners().size(), equalTo(familyDTO.getOwners().size()));
    }

    @Test
    public void save_userPresent_notAdmin_updatingFamily_existingFamilyNotFound() {
        // GIVEN
        User mockUser = mock(User.class);
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authoritySet.add(authority);
        when(mockUser.getAuthorities()).thenReturn(authoritySet);
        when(this.mockUserService.getUserWithAuthorities()).thenReturn(Optional.of(mockUser));

        FamilyDTO familyDTO = createSampleFamilyDTO();
        try {
            // WHEN
            this.classUnderTest.save(familyDTO);
            fail("Expected BadRequestAlertException");
        } catch (BadRequestAlertException e){
            // THEN
            assertThat(e.getEntityName(), equalTo(FamilyResource.ENTITY_NAME));
            assertThat(e.getErrorKey(), equalTo(Constants.ERROR_KEY_ENTITY_NOT_FOUND));
        }
    }

    @Test
    public void save_userPresent_notAdmin_updatingFamily_existingFamilyFound_notAdmin() {
        // GIVEN
        User mockUser = mock(User.class);
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authoritySet.add(authority);
        when(mockUser.getAuthorities()).thenReturn(authoritySet);
        when(mockUser.getId()).thenReturn(99L);
        when(this.mockUserService.getUserWithAuthorities()).thenReturn(Optional.of(mockUser));

        FamilyDTO existingFamilyDTO = createSampleFamilyDTO();
        when(this.mockFamilyRepository.findOneWithEagerRelationships(existingFamilyDTO.getId())).thenReturn(Optional.of(this.realFamilyMapper.toEntity(existingFamilyDTO)));

        FamilyDTO modifiedFamilyDTO = createSampleFamilyDTO();
        modifiedFamilyDTO.setName("something else");
        UserDTO newUserDTO = createSampleUserDTO();
        newUserDTO.setId(newUserDTO.getId() + 1);
        newUserDTO.setLogin(newUserDTO.getLogin() + "1");
        Set<UserDTO> newOwners = modifiedFamilyDTO.getOwners();
        newOwners.add(newUserDTO);
        Set<UserDTO> newAdmins = modifiedFamilyDTO.getAdmins();
        newAdmins.add(newUserDTO);
        Set<UserDTO> newMembers = modifiedFamilyDTO.getMembers();
        newMembers.add(newUserDTO);
        modifiedFamilyDTO.setOwners(newOwners);
        modifiedFamilyDTO.setAdmins(newAdmins);
        modifiedFamilyDTO.setMembers(newMembers);

        try {
            // WHEN
            this.classUnderTest.save(modifiedFamilyDTO);
            fail("Expected BadRequestAlertException");
        } catch (BadRequestAlertException e){
            // THEN
            assertThat(e.getEntityName(), equalTo(FamilyResource.ENTITY_NAME));
            assertThat(e.getErrorKey(), equalTo(Constants.ERROR_KEY_NOT_ALLOWED_TO_MODIFY_ENTITY));
        }
    }

    @Test
    public void save_userPresent_notAdmin_updatingFamily_existingFamilyFound_isAdmin_isOwner() {
        // GIVEN
        User mockUser = mock(User.class);
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authoritySet.add(authority);
        when(mockUser.getAuthorities()).thenReturn(authoritySet);
        when(mockUser.getId()).thenReturn(1L);
        when(this.mockUserService.getUserWithAuthorities()).thenReturn(Optional.of(mockUser));

        FamilyDTO existingFamilyDTO = createSampleFamilyDTO();
        when(this.mockFamilyRepository.findOneWithEagerRelationships(existingFamilyDTO.getId())).thenReturn(Optional.of(this.realFamilyMapper.toEntity(existingFamilyDTO)));

        FamilyDTO modifiedFamilyDTO = createSampleFamilyDTO();
        modifiedFamilyDTO.setName("something else");
        UserDTO newUserDTO = createSampleUserDTO();
        newUserDTO.setId(newUserDTO.getId() + 1);
        newUserDTO.setLogin(newUserDTO.getLogin() + "1");
        Set<UserDTO> newOwners = modifiedFamilyDTO.getOwners();
        newOwners.add(newUserDTO);
        Set<UserDTO> newAdmins = modifiedFamilyDTO.getAdmins();
        newAdmins.add(newUserDTO);
        Set<UserDTO> newMembers = modifiedFamilyDTO.getMembers();
        newMembers.add(newUserDTO);
        modifiedFamilyDTO.setOwners(newOwners);
        modifiedFamilyDTO.setAdmins(newAdmins);
        modifiedFamilyDTO.setMembers(newMembers);

        // WHEN
        this.classUnderTest.save(modifiedFamilyDTO);

        // THEN
        ArgumentCaptor<Family> captor = ArgumentCaptor.forClass(Family.class);
        verify(this.mockFamilyRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getId(), equalTo(modifiedFamilyDTO.getId()));
        assertThat(captor.getValue().getName(), equalTo(modifiedFamilyDTO.getName()));
        assertThat(captor.getValue().getMembers().size(), equalTo(modifiedFamilyDTO.getMembers().size()));
        assertThat(captor.getValue().getAdmins().size(), equalTo(modifiedFamilyDTO.getAdmins().size()));
        assertThat(captor.getValue().getOwners().size(), equalTo(modifiedFamilyDTO.getOwners().size()));
    }

    @Test
    public void save_userPresent_notAdmin_updatingFamily_existingFamilyFound_isAdmin_notOwner() {
        // GIVEN
        User mockUser = mock(User.class);
        Set<Authority> authoritySet = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authoritySet.add(authority);
        when(mockUser.getAuthorities()).thenReturn(authoritySet);
        when(mockUser.getId()).thenReturn(99L);
        when(this.mockUserService.getUserWithAuthorities()).thenReturn(Optional.of(mockUser));

        FamilyDTO existingFamilyDTO = createSampleFamilyDTO();
        UserDTO newAdmin = createSampleUserDTO();
        newAdmin.setId(99L);
        existingFamilyDTO.getAdmins().add(newAdmin);
        when(this.mockFamilyRepository.findOneWithEagerRelationships(existingFamilyDTO.getId())).thenReturn(Optional.of(this.realFamilyMapper.toEntity(existingFamilyDTO)));

        FamilyDTO modifiedFamilyDTO = createSampleFamilyDTO();
        modifiedFamilyDTO.setName("something else");
        UserDTO newUserDTO = createSampleUserDTO();
        newUserDTO.setId(newUserDTO.getId() + 1);
        newUserDTO.setLogin(newUserDTO.getLogin() + "1");
        Set<UserDTO> newOwners = modifiedFamilyDTO.getOwners();
        newOwners.add(newUserDTO);
        Set<UserDTO> newAdmins = modifiedFamilyDTO.getAdmins();
        newAdmins.add(newUserDTO);
        Set<UserDTO> newMembers = modifiedFamilyDTO.getMembers();
        newMembers.add(newUserDTO);
        modifiedFamilyDTO.setOwners(newOwners);
        modifiedFamilyDTO.setAdmins(newAdmins);
        modifiedFamilyDTO.setMembers(newMembers);

        // WHEN
        this.classUnderTest.save(modifiedFamilyDTO);

        // THEN
        ArgumentCaptor<Family> captor = ArgumentCaptor.forClass(Family.class);
        verify(this.mockFamilyRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getId(), equalTo(modifiedFamilyDTO.getId()));
        assertThat(captor.getValue().getName(), equalTo(modifiedFamilyDTO.getName()));
        assertThat(captor.getValue().getMembers().size(), equalTo(existingFamilyDTO.getMembers().size()));
        assertThat(captor.getValue().getAdmins().size(), equalTo(existingFamilyDTO.getAdmins().size()));
        assertThat(captor.getValue().getOwners().size(), equalTo(existingFamilyDTO.getOwners().size()));
    }

    private FamilyDTO createSampleFamilyDTO() {
        FamilyDTO f = new FamilyDTO();
        f.setId(1L);
        f.setName("foobar");
        Set<UserDTO> userSet = new HashSet<>();
        userSet.add(createSampleUserDTO());
        f.setMembers(userSet);
        f.setAdmins(userSet);
        f.setOwners(userSet);
        return f;
    }

    private UserDTO createSampleUserDTO() {
        UserDTO u = new UserDTO();
        u.setId(1L);
        u.setLogin("bob");
        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        u.setAuthorities(authorities);
        return u;
    }
}
