package com.rothandrew.familygiftlist.gateway.web.rest;

import com.rothandrew.familygiftlist.gateway.FglgatewayApp;

import com.rothandrew.familygiftlist.gateway.domain.Gift;
import com.rothandrew.familygiftlist.gateway.domain.GiftList;
import com.rothandrew.familygiftlist.gateway.repository.GiftRepository;
import com.rothandrew.familygiftlist.gateway.service.GiftService;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftDTO;
import com.rothandrew.familygiftlist.gateway.service.mapper.GiftMapper;
import com.rothandrew.familygiftlist.gateway.web.rest.errors.ExceptionTranslator;
import com.rothandrew.familygiftlist.gateway.service.dto.GiftCriteria;
import com.rothandrew.familygiftlist.gateway.service.GiftQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static com.rothandrew.familygiftlist.gateway.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GiftResource REST controller.
 *
 * @see GiftResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FglgatewayApp.class)
public class GiftResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private GiftMapper giftMapper;
    
    @Autowired
    private GiftService giftService;

    @Autowired
    private GiftQueryService giftQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGiftMockMvc;

    private Gift gift;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GiftResource giftResource = new GiftResource(giftService, giftQueryService);
        this.restGiftMockMvc = MockMvcBuilders.standaloneSetup(giftResource)
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
    public static Gift createEntity(EntityManager em) {
        Gift gift = new Gift()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL);
        return gift;
    }

    @Before
    public void initTest() {
        gift = createEntity(em);
    }

    @Test
    @Transactional
    public void createGift() throws Exception {
        int databaseSizeBeforeCreate = giftRepository.findAll().size();

        // Create the Gift
        GiftDTO giftDTO = giftMapper.toDto(gift);
        restGiftMockMvc.perform(post("/api/gifts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giftDTO)))
            .andExpect(status().isCreated());

        // Validate the Gift in the database
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeCreate + 1);
        Gift testGift = giftList.get(giftList.size() - 1);
        assertThat(testGift.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGift.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGift.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createGiftWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = giftRepository.findAll().size();

        // Create the Gift with an existing ID
        gift.setId(1L);
        GiftDTO giftDTO = giftMapper.toDto(gift);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGiftMockMvc.perform(post("/api/gifts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giftDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gift in the database
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = giftRepository.findAll().size();
        // set the field null
        gift.setName(null);

        // Create the Gift, which fails.
        GiftDTO giftDTO = giftMapper.toDto(gift);

        restGiftMockMvc.perform(post("/api/gifts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giftDTO)))
            .andExpect(status().isBadRequest());

        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGifts() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList
        restGiftMockMvc.perform(get("/api/gifts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gift.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getGift() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get the gift
        restGiftMockMvc.perform(get("/api/gifts/{id}", gift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gift.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllGiftsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where name equals to DEFAULT_NAME
        defaultGiftShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the giftList where name equals to UPDATED_NAME
        defaultGiftShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGiftsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGiftShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the giftList where name equals to UPDATED_NAME
        defaultGiftShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGiftsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where name is not null
        defaultGiftShouldBeFound("name.specified=true");

        // Get all the giftList where name is null
        defaultGiftShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllGiftsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where url equals to DEFAULT_URL
        defaultGiftShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the giftList where url equals to UPDATED_URL
        defaultGiftShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllGiftsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where url in DEFAULT_URL or UPDATED_URL
        defaultGiftShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the giftList where url equals to UPDATED_URL
        defaultGiftShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllGiftsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList where url is not null
        defaultGiftShouldBeFound("url.specified=true");

        // Get all the giftList where url is null
        defaultGiftShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllGiftsByGiftListIsEqualToSomething() throws Exception {
        // Initialize the database
        GiftList giftList = GiftListResourceIntTest.createEntity(em);
        em.persist(giftList);
        em.flush();
        gift.setGiftList(giftList);
        giftRepository.saveAndFlush(gift);
        Long giftListId = giftList.getId();

        // Get all the giftList where giftList equals to giftListId
        defaultGiftShouldBeFound("giftListId.equals=" + giftListId);

        // Get all the giftList where giftList equals to giftListId + 1
        defaultGiftShouldNotBeFound("giftListId.equals=" + (giftListId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGiftShouldBeFound(String filter) throws Exception {
        restGiftMockMvc.perform(get("/api/gifts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gift.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGiftShouldNotBeFound(String filter) throws Exception {
        restGiftMockMvc.perform(get("/api/gifts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingGift() throws Exception {
        // Get the gift
        restGiftMockMvc.perform(get("/api/gifts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGift() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        int databaseSizeBeforeUpdate = giftRepository.findAll().size();

        // Update the gift
        Gift updatedGift = giftRepository.findById(gift.getId()).get();
        // Disconnect from session so that the updates on updatedGift are not directly saved in db
        em.detach(updatedGift);
        updatedGift
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL);
        GiftDTO giftDTO = giftMapper.toDto(updatedGift);

        restGiftMockMvc.perform(put("/api/gifts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giftDTO)))
            .andExpect(status().isOk());

        // Validate the Gift in the database
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeUpdate);
        Gift testGift = giftList.get(giftList.size() - 1);
        assertThat(testGift.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGift.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGift.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingGift() throws Exception {
        int databaseSizeBeforeUpdate = giftRepository.findAll().size();

        // Create the Gift
        GiftDTO giftDTO = giftMapper.toDto(gift);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiftMockMvc.perform(put("/api/gifts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giftDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gift in the database
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGift() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        int databaseSizeBeforeDelete = giftRepository.findAll().size();

        // Get the gift
        restGiftMockMvc.perform(delete("/api/gifts/{id}", gift.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gift.class);
        Gift gift1 = new Gift();
        gift1.setId(1L);
        Gift gift2 = new Gift();
        gift2.setId(gift1.getId());
        assertThat(gift1).isEqualTo(gift2);
        gift2.setId(2L);
        assertThat(gift1).isNotEqualTo(gift2);
        gift1.setId(null);
        assertThat(gift1).isNotEqualTo(gift2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GiftDTO.class);
        GiftDTO giftDTO1 = new GiftDTO();
        giftDTO1.setId(1L);
        GiftDTO giftDTO2 = new GiftDTO();
        assertThat(giftDTO1).isNotEqualTo(giftDTO2);
        giftDTO2.setId(giftDTO1.getId());
        assertThat(giftDTO1).isEqualTo(giftDTO2);
        giftDTO2.setId(2L);
        assertThat(giftDTO1).isNotEqualTo(giftDTO2);
        giftDTO1.setId(null);
        assertThat(giftDTO1).isNotEqualTo(giftDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(giftMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(giftMapper.fromId(null)).isNull();
    }
}
