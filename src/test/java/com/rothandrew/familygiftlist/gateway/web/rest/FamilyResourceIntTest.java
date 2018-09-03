package com.rothandrew.familygiftlist.gateway.web.rest;

import com.rothandrew.familygiftlist.gateway.FglgatewayApp;

import com.rothandrew.familygiftlist.gateway.domain.Family;
import com.rothandrew.familygiftlist.gateway.domain.User;
import com.rothandrew.familygiftlist.gateway.repository.FamilyRepository;
import com.rothandrew.familygiftlist.gateway.service.FamilyService;
import com.rothandrew.familygiftlist.gateway.service.dto.FamilyDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.FamilyMapper;
import com.rothandrew.familygiftlist.gateway.web.rest.errors.ExceptionTranslator;
import com.rothandrew.familygiftlist.gateway.service.dto.FamilyCriteria;
import com.rothandrew.familygiftlist.gateway.service.FamilyQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.rothandrew.familygiftlist.gateway.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FamilyResource REST controller.
 *
 * @see FamilyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FglgatewayApp.class)
public class FamilyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private FamilyRepository familyRepository;

    @Mock
    private FamilyRepository familyRepositoryMock;

    @Autowired
    private FamilyMapper familyMapper;
    

    @Mock
    private FamilyService familyServiceMock;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private FamilyQueryService familyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFamilyMockMvc;

    private Family family;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FamilyResource familyResource = new FamilyResource(familyService, familyQueryService);
        this.restFamilyMockMvc = MockMvcBuilders.standaloneSetup(familyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Family createEntity(EntityManager em) {
        Family family = new Family()
            .name(DEFAULT_NAME);
        return family;
    }

    @Before
    public void initTest() {
        family = createEntity(em);
    }

    @Test
    @Transactional
    public void createFamily() throws Exception {
        int databaseSizeBeforeCreate = familyRepository.findAll().size();

        // Create the Family
        FamilyDTO familyDTO = familyMapper.toDto(family);
        restFamilyMockMvc.perform(post("/api/families")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyDTO)))
            .andExpect(status().isCreated());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeCreate + 1);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFamilyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = familyRepository.findAll().size();

        // Create the Family with an existing ID
        family.setId(1L);
        FamilyDTO familyDTO = familyMapper.toDto(family);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyMockMvc.perform(post("/api/families")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyRepository.findAll().size();
        // set the field null
        family.setName(null);

        // Create the Family, which fails.
        FamilyDTO familyDTO = familyMapper.toDto(family);

        restFamilyMockMvc.perform(post("/api/families")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyDTO)))
            .andExpect(status().isBadRequest());

        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFamilies() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get all the familyList
        restFamilyMockMvc.perform(get("/api/families?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(family.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    public void getAllFamiliesWithEagerRelationshipsIsEnabled() throws Exception {
        FamilyResource familyResource = new FamilyResource(familyServiceMock, familyQueryService);
        when(familyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restFamilyMockMvc = MockMvcBuilders.standaloneSetup(familyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFamilyMockMvc.perform(get("/api/families?eagerload=true"))
        .andExpect(status().isOk());

        verify(familyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllFamiliesWithEagerRelationshipsIsNotEnabled() throws Exception {
        FamilyResource familyResource = new FamilyResource(familyServiceMock, familyQueryService);
            when(familyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restFamilyMockMvc = MockMvcBuilders.standaloneSetup(familyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFamilyMockMvc.perform(get("/api/families?eagerload=true"))
        .andExpect(status().isOk());

            verify(familyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get the family
        restFamilyMockMvc.perform(get("/api/families/{id}", family.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(family.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllFamiliesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get all the familyList where name equals to DEFAULT_NAME
        defaultFamilyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the familyList where name equals to UPDATED_NAME
        defaultFamilyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFamiliesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get all the familyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFamilyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the familyList where name equals to UPDATED_NAME
        defaultFamilyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFamiliesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get all the familyList where name is not null
        defaultFamilyShouldBeFound("name.specified=true");

        // Get all the familyList where name is null
        defaultFamilyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllFamiliesByMembersIsEqualToSomething() throws Exception {
        // Initialize the database
        User members = UserResourceIntTest.createEntity(em);
        em.persist(members);
        em.flush();
        family.addMembers(members);
        familyRepository.saveAndFlush(family);
        Long membersId = members.getId();

        // Get all the familyList where members equals to membersId
        defaultFamilyShouldBeFound("membersId.equals=" + membersId);

        // Get all the familyList where members equals to membersId + 1
        defaultFamilyShouldNotBeFound("membersId.equals=" + (membersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFamilyShouldBeFound(String filter) throws Exception {
        restFamilyMockMvc.perform(get("/api/families?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(family.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFamilyShouldNotBeFound(String filter) throws Exception {
        restFamilyMockMvc.perform(get("/api/families?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingFamily() throws Exception {
        // Get the family
        restFamilyMockMvc.perform(get("/api/families/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Update the family
        Family updatedFamily = familyRepository.findById(family.getId()).get();
        // Disconnect from session so that the updates on updatedFamily are not directly saved in db
        em.detach(updatedFamily);
        updatedFamily
            .name(UPDATED_NAME);
        FamilyDTO familyDTO = familyMapper.toDto(updatedFamily);

        restFamilyMockMvc.perform(put("/api/families")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyDTO)))
            .andExpect(status().isOk());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
        Family testFamily = familyList.get(familyList.size() - 1);
        assertThat(testFamily.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingFamily() throws Exception {
        int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Create the Family
        FamilyDTO familyDTO = familyMapper.toDto(family);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMockMvc.perform(put("/api/families")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Family in the database
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        int databaseSizeBeforeDelete = familyRepository.findAll().size();

        // Get the family
        restFamilyMockMvc.perform(delete("/api/families/{id}", family.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Family> familyList = familyRepository.findAll();
        assertThat(familyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Family.class);
        Family family1 = new Family();
        family1.setId(1L);
        Family family2 = new Family();
        family2.setId(family1.getId());
        assertThat(family1).isEqualTo(family2);
        family2.setId(2L);
        assertThat(family1).isNotEqualTo(family2);
        family1.setId(null);
        assertThat(family1).isNotEqualTo(family2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyDTO.class);
        FamilyDTO familyDTO1 = new FamilyDTO();
        familyDTO1.setId(1L);
        FamilyDTO familyDTO2 = new FamilyDTO();
        assertThat(familyDTO1).isNotEqualTo(familyDTO2);
        familyDTO2.setId(familyDTO1.getId());
        assertThat(familyDTO1).isEqualTo(familyDTO2);
        familyDTO2.setId(2L);
        assertThat(familyDTO1).isNotEqualTo(familyDTO2);
        familyDTO1.setId(null);
        assertThat(familyDTO1).isNotEqualTo(familyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(familyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(familyMapper.fromId(null)).isNull();
    }
}
