package com.enginemobi.fx.web.rest;

import com.enginemobi.fx.FxpriceserviceApp;

import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.repository.CurrencyMapRepository;
import com.enginemobi.fx.service.CurrencyMapService;
import com.enginemobi.fx.service.dto.CurrencyMapDTO;
import com.enginemobi.fx.service.mapper.CurrencyMapMapper;
import com.enginemobi.fx.web.rest.errors.ExceptionTranslator;

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

import static com.enginemobi.fx.web.rest.TestUtil.createFormattingConversionService;
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

    private static final CurrencyRateProvider DEFAULT_PROVIDED_BY = CurrencyRateProvider.ONE_FORGE;
    private static final CurrencyRateProvider UPDATED_PROVIDED_BY = CurrencyRateProvider.CURRENCY_LAYER;

    @Autowired
    private CurrencyMapRepository currencyMapRepository;

    @Autowired
    private CurrencyMapMapper currencyMapMapper;

    @Autowired
    private CurrencyMapService currencyMapService;

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
        final CurrencyMapResource currencyMapResource = new CurrencyMapResource(currencyMapService);
        this.restCurrencyMapMockMvc = MockMvcBuilders.standaloneSetup(currencyMapResource)
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
    public static CurrencyMap createEntity(EntityManager em) {
        CurrencyMap currencyMap = new CurrencyMap()
            .currencyQuote(DEFAULT_CURRENCY_QUOTE)
            .currencyBaseCode(DEFAULT_CURRENCY_BASE_CODE)
            .currencyNonBaseCode(DEFAULT_CURRENCY_NON_BASE_CODE)
            .providedBy(DEFAULT_PROVIDED_BY);
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
        assertThat(testCurrencyMap.getProvidedBy()).isEqualTo(DEFAULT_PROVIDED_BY);
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

        // Validate the CurrencyMap in the database
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
            .andExpect(jsonPath("$.[*].providedBy").value(hasItem(DEFAULT_PROVIDED_BY.toString())));
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
            .andExpect(jsonPath("$.providedBy").value(DEFAULT_PROVIDED_BY.toString()));
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
        // Disconnect from session so that the updates on updatedCurrencyMap are not directly saved in db
        em.detach(updatedCurrencyMap);
        updatedCurrencyMap
            .currencyQuote(UPDATED_CURRENCY_QUOTE)
            .currencyBaseCode(UPDATED_CURRENCY_BASE_CODE)
            .currencyNonBaseCode(UPDATED_CURRENCY_NON_BASE_CODE)
            .providedBy(UPDATED_PROVIDED_BY);
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
        assertThat(testCurrencyMap.getProvidedBy()).isEqualTo(UPDATED_PROVIDED_BY);
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
