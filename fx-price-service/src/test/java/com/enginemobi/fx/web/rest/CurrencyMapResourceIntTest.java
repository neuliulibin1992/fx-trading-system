package com.enginemobi.fx.web.rest;

import com.enginemobi.fx.FxpriceserviceApp;

import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.repository.CurrencyMapRepository;
import com.enginemobi.fx.service.CurrencyMapService;
import com.enginemobi.fx.service.dto.CurrencyMapDTO;
import com.enginemobi.fx.service.mapper.CurrencyMapMapper;
import com.enginemobi.fx.web.rest.errors.ExceptionTranslator;
import com.enginemobi.fx.service.dto.CurrencyMapCriteria;
import com.enginemobi.fx.service.CurrencyMapQueryService;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
/**
 * Test class for the CurrencyMapResource REST controller.
 *
 * @see CurrencyMapResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FxpriceserviceApp.class)
public class CurrencyMapResourceIntTest {

    private static final String DEFAULT_CURRENCY_QUOTE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_QUOTE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_BASE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_BASE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_NON_BASE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_NON_BASE_CODE = "BBBBBBBBBB";

    private static final CurrencyRateProvider DEFAULT_RATE_PROVIDER = CurrencyRateProvider.ONE_FORGE;
    private static final CurrencyRateProvider UPDATED_RATE_PROVIDER = CurrencyRateProvider.CURRENCY_LAYER;

    @Autowired
    private CurrencyMapRepository currencyMapRepository;

    @Autowired
    private CurrencyMapMapper currencyMapMapper;

    @Autowired
    private CurrencyMapService currencyMapService;

    @Autowired
    private CurrencyMapQueryService currencyMapQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCurrencyMapMockMvc;

    private CurrencyMap currencyMap;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CurrencyMapResource currencyMapResource = new CurrencyMapResource(currencyMapService, currencyMapQueryService);
        this.restCurrencyMapMockMvc = MockMvcBuilders.standaloneSetup(currencyMapResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyMap createEntity(EntityManager em) {
        CurrencyMap currencyMap = new CurrencyMap()
            .currencyQuote(DEFAULT_CURRENCY_QUOTE)
            .currencyBaseCode(DEFAULT_CURRENCY_BASE_CODE)
            .currencyNonBaseCode(DEFAULT_CURRENCY_NON_BASE_CODE)
            .rateProvider(DEFAULT_RATE_PROVIDER);
        return currencyMap;
    }

    @Before
    public void initTest() {
        currencyMap = createEntity(em);
    }

    @Test
    @Transactional
    public void createCurrencyMap() throws Exception {
        int databaseSizeBeforeCreate = currencyMapRepository.findAll().size();

        // Create the CurrencyMap
        CurrencyMapDTO currencyMapDTO = currencyMapMapper.toDto(currencyMap);
        restCurrencyMapMockMvc.perform(post("/api/currency-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyMapDTO)))
            .andExpect(status().isCreated());

        // Validate the CurrencyMap in the database
        List<CurrencyMap> currencyMapList = currencyMapRepository.findAll();
        assertThat(currencyMapList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyMap testCurrencyMap = currencyMapList.get(currencyMapList.size() - 1);
        assertThat(testCurrencyMap.getCurrencyQuote()).isEqualTo(DEFAULT_CURRENCY_QUOTE);
        assertThat(testCurrencyMap.getCurrencyBaseCode()).isEqualTo(DEFAULT_CURRENCY_BASE_CODE);
        assertThat(testCurrencyMap.getCurrencyNonBaseCode()).isEqualTo(DEFAULT_CURRENCY_NON_BASE_CODE);
        assertThat(testCurrencyMap.getRateProvider()).isEqualTo(DEFAULT_RATE_PROVIDER);
    }

    @Test
    @Transactional
    public void createCurrencyMapWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = currencyMapRepository.findAll().size();

        // Create the CurrencyMap with an existing ID
        currencyMap.setId(1L);
        CurrencyMapDTO currencyMapDTO = currencyMapMapper.toDto(currencyMap);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyMapMockMvc.perform(post("/api/currency-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyMapDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CurrencyMap> currencyMapList = currencyMapRepository.findAll();
        assertThat(currencyMapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCurrencyMaps() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList
        restCurrencyMapMockMvc.perform(get("/api/currency-maps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyMap.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyQuote").value(hasItem(DEFAULT_CURRENCY_QUOTE.toString())))
            .andExpect(jsonPath("$.[*].currencyBaseCode").value(hasItem(DEFAULT_CURRENCY_BASE_CODE.toString())))
            .andExpect(jsonPath("$.[*].currencyNonBaseCode").value(hasItem(DEFAULT_CURRENCY_NON_BASE_CODE.toString())))
            .andExpect(jsonPath("$.[*].rateProvider").value(hasItem(DEFAULT_RATE_PROVIDER.toString())));
    }

    @Test
    @Transactional
    public void getCurrencyMap() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get the currencyMap
        restCurrencyMapMockMvc.perform(get("/api/currency-maps/{id}", currencyMap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(currencyMap.getId().intValue()))
            .andExpect(jsonPath("$.currencyQuote").value(DEFAULT_CURRENCY_QUOTE.toString()))
            .andExpect(jsonPath("$.currencyBaseCode").value(DEFAULT_CURRENCY_BASE_CODE.toString()))
            .andExpect(jsonPath("$.currencyNonBaseCode").value(DEFAULT_CURRENCY_NON_BASE_CODE.toString()))
            .andExpect(jsonPath("$.rateProvider").value(DEFAULT_RATE_PROVIDER.toString()));
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByCurrencyQuoteIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where currencyQuote equals to DEFAULT_CURRENCY_QUOTE
        defaultCurrencyMapShouldBeFound("currencyQuote.equals=" + DEFAULT_CURRENCY_QUOTE);

        // Get all the currencyMapList where currencyQuote equals to UPDATED_CURRENCY_QUOTE
        defaultCurrencyMapShouldNotBeFound("currencyQuote.equals=" + UPDATED_CURRENCY_QUOTE);
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByCurrencyQuoteIsInShouldWork() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where currencyQuote in DEFAULT_CURRENCY_QUOTE or UPDATED_CURRENCY_QUOTE
        defaultCurrencyMapShouldBeFound("currencyQuote.in=" + DEFAULT_CURRENCY_QUOTE + "," + UPDATED_CURRENCY_QUOTE);

        // Get all the currencyMapList where currencyQuote equals to UPDATED_CURRENCY_QUOTE
        defaultCurrencyMapShouldNotBeFound("currencyQuote.in=" + UPDATED_CURRENCY_QUOTE);
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByCurrencyQuoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where currencyQuote is not null
        defaultCurrencyMapShouldBeFound("currencyQuote.specified=true");

        // Get all the currencyMapList where currencyQuote is null
        defaultCurrencyMapShouldNotBeFound("currencyQuote.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByCurrencyBaseCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where currencyBaseCode equals to DEFAULT_CURRENCY_BASE_CODE
        defaultCurrencyMapShouldBeFound("currencyBaseCode.equals=" + DEFAULT_CURRENCY_BASE_CODE);

        // Get all the currencyMapList where currencyBaseCode equals to UPDATED_CURRENCY_BASE_CODE
        defaultCurrencyMapShouldNotBeFound("currencyBaseCode.equals=" + UPDATED_CURRENCY_BASE_CODE);
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByCurrencyBaseCodeIsInShouldWork() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where currencyBaseCode in DEFAULT_CURRENCY_BASE_CODE or UPDATED_CURRENCY_BASE_CODE
        defaultCurrencyMapShouldBeFound("currencyBaseCode.in=" + DEFAULT_CURRENCY_BASE_CODE + "," + UPDATED_CURRENCY_BASE_CODE);

        // Get all the currencyMapList where currencyBaseCode equals to UPDATED_CURRENCY_BASE_CODE
        defaultCurrencyMapShouldNotBeFound("currencyBaseCode.in=" + UPDATED_CURRENCY_BASE_CODE);
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByCurrencyBaseCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where currencyBaseCode is not null
        defaultCurrencyMapShouldBeFound("currencyBaseCode.specified=true");

        // Get all the currencyMapList where currencyBaseCode is null
        defaultCurrencyMapShouldNotBeFound("currencyBaseCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByCurrencyNonBaseCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where currencyNonBaseCode equals to DEFAULT_CURRENCY_NON_BASE_CODE
        defaultCurrencyMapShouldBeFound("currencyNonBaseCode.equals=" + DEFAULT_CURRENCY_NON_BASE_CODE);

        // Get all the currencyMapList where currencyNonBaseCode equals to UPDATED_CURRENCY_NON_BASE_CODE
        defaultCurrencyMapShouldNotBeFound("currencyNonBaseCode.equals=" + UPDATED_CURRENCY_NON_BASE_CODE);
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByCurrencyNonBaseCodeIsInShouldWork() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where currencyNonBaseCode in DEFAULT_CURRENCY_NON_BASE_CODE or UPDATED_CURRENCY_NON_BASE_CODE
        defaultCurrencyMapShouldBeFound("currencyNonBaseCode.in=" + DEFAULT_CURRENCY_NON_BASE_CODE + "," + UPDATED_CURRENCY_NON_BASE_CODE);

        // Get all the currencyMapList where currencyNonBaseCode equals to UPDATED_CURRENCY_NON_BASE_CODE
        defaultCurrencyMapShouldNotBeFound("currencyNonBaseCode.in=" + UPDATED_CURRENCY_NON_BASE_CODE);
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByCurrencyNonBaseCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where currencyNonBaseCode is not null
        defaultCurrencyMapShouldBeFound("currencyNonBaseCode.specified=true");

        // Get all the currencyMapList where currencyNonBaseCode is null
        defaultCurrencyMapShouldNotBeFound("currencyNonBaseCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByRateProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where rateProvider equals to DEFAULT_RATE_PROVIDER
        defaultCurrencyMapShouldBeFound("rateProvider.equals=" + DEFAULT_RATE_PROVIDER);

        // Get all the currencyMapList where rateProvider equals to UPDATED_RATE_PROVIDER
        defaultCurrencyMapShouldNotBeFound("rateProvider.equals=" + UPDATED_RATE_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByRateProviderIsInShouldWork() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where rateProvider in DEFAULT_RATE_PROVIDER or UPDATED_RATE_PROVIDER
        defaultCurrencyMapShouldBeFound("rateProvider.in=" + DEFAULT_RATE_PROVIDER + "," + UPDATED_RATE_PROVIDER);

        // Get all the currencyMapList where rateProvider equals to UPDATED_RATE_PROVIDER
        defaultCurrencyMapShouldNotBeFound("rateProvider.in=" + UPDATED_RATE_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllCurrencyMapsByRateProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);

        // Get all the currencyMapList where rateProvider is not null
        defaultCurrencyMapShouldBeFound("rateProvider.specified=true");

        // Get all the currencyMapList where rateProvider is null
        defaultCurrencyMapShouldNotBeFound("rateProvider.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCurrencyMapShouldBeFound(String filter) throws Exception {
        restCurrencyMapMockMvc.perform(get("/api/currency-maps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyMap.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyQuote").value(hasItem(DEFAULT_CURRENCY_QUOTE.toString())))
            .andExpect(jsonPath("$.[*].currencyBaseCode").value(hasItem(DEFAULT_CURRENCY_BASE_CODE.toString())))
            .andExpect(jsonPath("$.[*].currencyNonBaseCode").value(hasItem(DEFAULT_CURRENCY_NON_BASE_CODE.toString())))
            .andExpect(jsonPath("$.[*].rateProvider").value(hasItem(DEFAULT_RATE_PROVIDER.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCurrencyMapShouldNotBeFound(String filter) throws Exception {
        restCurrencyMapMockMvc.perform(get("/api/currency-maps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCurrencyMap() throws Exception {
        // Get the currencyMap
        restCurrencyMapMockMvc.perform(get("/api/currency-maps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrencyMap() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);
        int databaseSizeBeforeUpdate = currencyMapRepository.findAll().size();

        // Update the currencyMap
        CurrencyMap updatedCurrencyMap = currencyMapRepository.findOne(currencyMap.getId());
        updatedCurrencyMap
            .currencyQuote(UPDATED_CURRENCY_QUOTE)
            .currencyBaseCode(UPDATED_CURRENCY_BASE_CODE)
            .currencyNonBaseCode(UPDATED_CURRENCY_NON_BASE_CODE)
            .rateProvider(UPDATED_RATE_PROVIDER);
        CurrencyMapDTO currencyMapDTO = currencyMapMapper.toDto(updatedCurrencyMap);

        restCurrencyMapMockMvc.perform(put("/api/currency-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyMapDTO)))
            .andExpect(status().isOk());

        // Validate the CurrencyMap in the database
        List<CurrencyMap> currencyMapList = currencyMapRepository.findAll();
        assertThat(currencyMapList).hasSize(databaseSizeBeforeUpdate);
        CurrencyMap testCurrencyMap = currencyMapList.get(currencyMapList.size() - 1);
        assertThat(testCurrencyMap.getCurrencyQuote()).isEqualTo(UPDATED_CURRENCY_QUOTE);
        assertThat(testCurrencyMap.getCurrencyBaseCode()).isEqualTo(UPDATED_CURRENCY_BASE_CODE);
        assertThat(testCurrencyMap.getCurrencyNonBaseCode()).isEqualTo(UPDATED_CURRENCY_NON_BASE_CODE);
        assertThat(testCurrencyMap.getRateProvider()).isEqualTo(UPDATED_RATE_PROVIDER);
    }

    @Test
    @Transactional
    public void updateNonExistingCurrencyMap() throws Exception {
        int databaseSizeBeforeUpdate = currencyMapRepository.findAll().size();

        // Create the CurrencyMap
        CurrencyMapDTO currencyMapDTO = currencyMapMapper.toDto(currencyMap);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCurrencyMapMockMvc.perform(put("/api/currency-maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyMapDTO)))
            .andExpect(status().isCreated());

        // Validate the CurrencyMap in the database
        List<CurrencyMap> currencyMapList = currencyMapRepository.findAll();
        assertThat(currencyMapList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCurrencyMap() throws Exception {
        // Initialize the database
        currencyMapRepository.saveAndFlush(currencyMap);
        int databaseSizeBeforeDelete = currencyMapRepository.findAll().size();

        // Get the currencyMap
        restCurrencyMapMockMvc.perform(delete("/api/currency-maps/{id}", currencyMap.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CurrencyMap> currencyMapList = currencyMapRepository.findAll();
        assertThat(currencyMapList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyMap.class);
        CurrencyMap currencyMap1 = new CurrencyMap();
        currencyMap1.setId(1L);
        CurrencyMap currencyMap2 = new CurrencyMap();
        currencyMap2.setId(currencyMap1.getId());
        assertThat(currencyMap1).isEqualTo(currencyMap2);
        currencyMap2.setId(2L);
        assertThat(currencyMap1).isNotEqualTo(currencyMap2);
        currencyMap1.setId(null);
        assertThat(currencyMap1).isNotEqualTo(currencyMap2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyMapDTO.class);
        CurrencyMapDTO currencyMapDTO1 = new CurrencyMapDTO();
        currencyMapDTO1.setId(1L);
        CurrencyMapDTO currencyMapDTO2 = new CurrencyMapDTO();
        assertThat(currencyMapDTO1).isNotEqualTo(currencyMapDTO2);
        currencyMapDTO2.setId(currencyMapDTO1.getId());
        assertThat(currencyMapDTO1).isEqualTo(currencyMapDTO2);
        currencyMapDTO2.setId(2L);
        assertThat(currencyMapDTO1).isNotEqualTo(currencyMapDTO2);
        currencyMapDTO1.setId(null);
        assertThat(currencyMapDTO1).isNotEqualTo(currencyMapDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(currencyMapMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(currencyMapMapper.fromId(null)).isNull();
    }
}
