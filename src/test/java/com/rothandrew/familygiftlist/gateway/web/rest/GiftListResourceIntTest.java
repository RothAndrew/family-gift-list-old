package com.rothandrew.familygiftlist.gateway.web.rest;

import com.rothandrew.familygiftlist.gateway.FglgatewayApp;

import com.rothandrew.familygiftlist.gateway.domain.GiftList;
import com.rothandrew.familygiftlist.gateway.domain.Gift;
import com.rothandrew.familygiftlist.gateway.domain.User;
import com.rothandrew.familygiftlist.gateway.repository.GiftListRepository;
import com.rothandrew.familygiftlist.gateway.service.GiftListService;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftListDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.GiftListMapper;
import com.rothandrew.familygiftlist.gateway.web.rest.errors.ExceptionTranslator;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftListCriteria;
import com.rothandrew.familygiftlist.gateway.service.GiftListQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.rothandrew.familygiftlist.gateway.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GiftListResource REST controller.
 *
 * @see GiftListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FglgatewayApp.class)
public class GiftListResourceIntTest {

    @Autowired
    private GiftListRepository giftListRepository;

    @Autowired
    private GiftListMapper giftListMapper;
    
    @Autowired
    private GiftListService giftListService;

    @Autowired
    private GiftListQueryService giftListQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGiftListMockMvc;

    private GiftList giftList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GiftListResource giftListResource = new GiftListResource(giftListService, giftListQueryService);
        this.restGiftListMockMvc = MockMvcBuilders.standaloneSetup(giftListResource)
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
    public static GiftList createEntity(EntityManager em) {
        GiftList giftList = new GiftList();
        return giftList;
    }

    @Before
    public void initTest() {
        giftList = createEntity(em);
    }

    @Test
    @Transactional
    public void createGiftList() throws Exception {
        int databaseSizeBeforeCreate = giftListRepository.findAll().size();

        // Create the GiftList
        GiftListDTO giftListDTO = giftListMapper.toDto(giftList);
        restGiftListMockMvc.perform(post("/api/gift-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giftListDTO)))
            .andExpect(status().isCreated());

        // Validate the GiftList in the database
        List<GiftList> giftListList = giftListRepository.findAll();
        assertThat(giftListList).hasSize(databaseSizeBeforeCreate + 1);
        GiftList testGiftList = giftListList.get(giftListList.size() - 1);
    }

    @Test
    @Transactional
    public void createGiftListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = giftListRepository.findAll().size();

        // Create the GiftList with an existing ID
        giftList.setId(1L);
        GiftListDTO giftListDTO = giftListMapper.toDto(giftList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGiftListMockMvc.perform(post("/api/gift-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giftListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GiftList in the database
        List<GiftList> giftListList = giftListRepository.findAll();
        assertThat(giftListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGiftLists() throws Exception {
        // Initialize the database
        giftListRepository.saveAndFlush(giftList);

        // Get all the giftListList
        restGiftListMockMvc.perform(get("/api/gift-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(giftList.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getGiftList() throws Exception {
        // Initialize the database
        giftListRepository.saveAndFlush(giftList);

        // Get the giftList
        restGiftListMockMvc.perform(get("/api/gift-lists/{id}", giftList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(giftList.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllGiftListsByGiftsIsEqualToSomething() throws Exception {
        // Initialize the database
        Gift gifts = GiftResourceIntTest.createEntity(em);
        em.persist(gifts);
        em.flush();
        giftList.addGifts(gifts);
        giftListRepository.saveAndFlush(giftList);
        Long giftsId = gifts.getId();

        // Get all the giftListList where gifts equals to giftsId
        defaultGiftListShouldBeFound("giftsId.equals=" + giftsId);

        // Get all the giftListList where gifts equals to giftsId + 1
        defaultGiftListShouldNotBeFound("giftsId.equals=" + (giftsId + 1));
    }


    @Test
    @Transactional
    public void getAllGiftListsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        giftList.setUser(user);
        giftListRepository.saveAndFlush(giftList);
        Long userId = user.getId();

        // Get all the giftListList where user equals to userId
        defaultGiftListShouldBeFound("userId.equals=" + userId);

        // Get all the giftListList where user equals to userId + 1
        defaultGiftListShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGiftListShouldBeFound(String filter) throws Exception {
        restGiftListMockMvc.perform(get("/api/gift-lists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(giftList.getId().intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGiftListShouldNotBeFound(String filter) throws Exception {
        restGiftListMockMvc.perform(get("/api/gift-lists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingGiftList() throws Exception {
        // Get the giftList
        restGiftListMockMvc.perform(get("/api/gift-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGiftList() throws Exception {
        // Initialize the database
        giftListRepository.saveAndFlush(giftList);

        int databaseSizeBeforeUpdate = giftListRepository.findAll().size();

        // Update the giftList
        GiftList updatedGiftList = giftListRepository.findById(giftList.getId()).get();
        // Disconnect from session so that the updates on updatedGiftList are not directly saved in db
        em.detach(updatedGiftList);
        GiftListDTO giftListDTO = giftListMapper.toDto(updatedGiftList);

        restGiftListMockMvc.perform(put("/api/gift-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giftListDTO)))
            .andExpect(status().isOk());

        // Validate the GiftList in the database
        List<GiftList> giftListList = giftListRepository.findAll();
        assertThat(giftListList).hasSize(databaseSizeBeforeUpdate);
        GiftList testGiftList = giftListList.get(giftListList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingGiftList() throws Exception {
        int databaseSizeBeforeUpdate = giftListRepository.findAll().size();

        // Create the GiftList
        GiftListDTO giftListDTO = giftListMapper.toDto(giftList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiftListMockMvc.perform(put("/api/gift-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giftListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GiftList in the database
        List<GiftList> giftListList = giftListRepository.findAll();
        assertThat(giftListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGiftList() throws Exception {
        // Initialize the database
        giftListRepository.saveAndFlush(giftList);

        int databaseSizeBeforeDelete = giftListRepository.findAll().size();

        // Get the giftList
        restGiftListMockMvc.perform(delete("/api/gift-lists/{id}", giftList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GiftList> giftListList = giftListRepository.findAll();
        assertThat(giftListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GiftList.class);
        GiftList giftList1 = new GiftList();
        giftList1.setId(1L);
        GiftList giftList2 = new GiftList();
        giftList2.setId(giftList1.getId());
        assertThat(giftList1).isEqualTo(giftList2);
        giftList2.setId(2L);
        assertThat(giftList1).isNotEqualTo(giftList2);
        giftList1.setId(null);
        assertThat(giftList1).isNotEqualTo(giftList2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GiftListDTO.class);
        GiftListDTO giftListDTO1 = new GiftListDTO();
        giftListDTO1.setId(1L);
        GiftListDTO giftListDTO2 = new GiftListDTO();
        assertThat(giftListDTO1).isNotEqualTo(giftListDTO2);
        giftListDTO2.setId(giftListDTO1.getId());
        assertThat(giftListDTO1).isEqualTo(giftListDTO2);
        giftListDTO2.setId(2L);
        assertThat(giftListDTO1).isNotEqualTo(giftListDTO2);
        giftListDTO1.setId(null);
        assertThat(giftListDTO1).isNotEqualTo(giftListDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(giftListMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(giftListMapper.fromId(null)).isNull();
    }
}
