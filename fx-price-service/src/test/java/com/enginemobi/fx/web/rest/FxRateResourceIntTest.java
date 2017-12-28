package com.enginemobi.fx.web.rest;

import com.enginemobi.fx.FxpriceserviceApp;

import com.enginemobi.fx.domain.FxRate;
import com.enginemobi.fx.repository.FxRateRepository;
import com.enginemobi.fx.service.FxRateService;
import com.enginemobi.fx.service.dto.FxRateDTO;
import com.enginemobi.fx.service.mapper.FxRateMapper;
import com.enginemobi.fx.web.rest.errors.ExceptionTranslator;
import com.enginemobi.fx.service.dto.FxRateCriteria;
import com.enginemobi.fx.service.FxRateQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FxRateResource REST controller.
 *
 * @see FxRateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FxpriceserviceApp.class)
public class FxRateResourceIntTest {

    private static final Instant DEFAULT_EXTRACT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXTRACT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_ARRIVAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ARRIVAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CURRENCY_QUOTE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_QUOTE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_BASE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_BASE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_NON_BASE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_NON_BASE_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BID_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BID_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ASK_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ASK_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MID_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MID_PRICE = new BigDecimal(2);

    @Autowired
    private FxRateRepository fxRateRepository;

    @Autowired
    private FxRateMapper fxRateMapper;

    @Autowired
    private FxRateService fxRateService;

    @Autowired
    private FxRateQueryService fxRateQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFxRateMockMvc;

    private FxRate fxRate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FxRateResource fxRateResource = new FxRateResource(fxRateService, fxRateQueryService);
        this.restFxRateMockMvc = MockMvcBuilders.standaloneSetup(fxRateResource)
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
    public static FxRate createEntity(EntityManager em) {
        FxRate fxRate = new FxRate()
            .extractTime(DEFAULT_EXTRACT_TIME)
            .arrivalDate(DEFAULT_ARRIVAL_DATE)
            .currencyQuote(DEFAULT_CURRENCY_QUOTE)
            .currencyBaseCode(DEFAULT_CURRENCY_BASE_CODE)
            .currencyNonBaseCode(DEFAULT_CURRENCY_NON_BASE_CODE)
            .bidPrice(DEFAULT_BID_PRICE)
            .askPrice(DEFAULT_ASK_PRICE)
            .midPrice(DEFAULT_MID_PRICE);
        return fxRate;
    }

    @Before
    public void initTest() {
        fxRate = createEntity(em);
    }

    @Test
    @Transactional
    public void createFxRate() throws Exception {
        int databaseSizeBeforeCreate = fxRateRepository.findAll().size();

        // Create the FxRate
        FxRateDTO fxRateDTO = fxRateMapper.toDto(fxRate);
        restFxRateMockMvc.perform(post("/api/fx-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRateDTO)))
            .andExpect(status().isCreated());

        // Validate the FxRate in the database
        List<FxRate> fxRateList = fxRateRepository.findAll();
        assertThat(fxRateList).hasSize(databaseSizeBeforeCreate + 1);
        FxRate testFxRate = fxRateList.get(fxRateList.size() - 1);
        assertThat(testFxRate.getExtractTime()).isEqualTo(DEFAULT_EXTRACT_TIME);
        assertThat(testFxRate.getArrivalDate()).isEqualTo(DEFAULT_ARRIVAL_DATE);
        assertThat(testFxRate.getCurrencyQuote()).isEqualTo(DEFAULT_CURRENCY_QUOTE);
        assertThat(testFxRate.getCurrencyBaseCode()).isEqualTo(DEFAULT_CURRENCY_BASE_CODE);
        assertThat(testFxRate.getCurrencyNonBaseCode()).isEqualTo(DEFAULT_CURRENCY_NON_BASE_CODE);
        assertThat(testFxRate.getBidPrice()).isEqualTo(DEFAULT_BID_PRICE);
        assertThat(testFxRate.getAskPrice()).isEqualTo(DEFAULT_ASK_PRICE);
        assertThat(testFxRate.getMidPrice()).isEqualTo(DEFAULT_MID_PRICE);
    }

    @Test
    @Transactional
    public void createFxRateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fxRateRepository.findAll().size();

        // Create the FxRate with an existing ID
        fxRate.setId(1L);
        FxRateDTO fxRateDTO = fxRateMapper.toDto(fxRate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFxRateMockMvc.perform(post("/api/fx-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FxRate> fxRateList = fxRateRepository.findAll();
        assertThat(fxRateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFxRates() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList
        restFxRateMockMvc.perform(get("/api/fx-rates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].extractTime").value(hasItem(DEFAULT_EXTRACT_TIME.toString())))
            .andExpect(jsonPath("$.[*].arrivalDate").value(hasItem(DEFAULT_ARRIVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].currencyQuote").value(hasItem(DEFAULT_CURRENCY_QUOTE.toString())))
            .andExpect(jsonPath("$.[*].currencyBaseCode").value(hasItem(DEFAULT_CURRENCY_BASE_CODE.toString())))
            .andExpect(jsonPath("$.[*].currencyNonBaseCode").value(hasItem(DEFAULT_CURRENCY_NON_BASE_CODE.toString())))
            .andExpect(jsonPath("$.[*].bidPrice").value(hasItem(DEFAULT_BID_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].askPrice").value(hasItem(DEFAULT_ASK_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].midPrice").value(hasItem(DEFAULT_MID_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getFxRate() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get the fxRate
        restFxRateMockMvc.perform(get("/api/fx-rates/{id}", fxRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fxRate.getId().intValue()))
            .andExpect(jsonPath("$.extractTime").value(DEFAULT_EXTRACT_TIME.toString()))
            .andExpect(jsonPath("$.arrivalDate").value(DEFAULT_ARRIVAL_DATE.toString()))
            .andExpect(jsonPath("$.currencyQuote").value(DEFAULT_CURRENCY_QUOTE.toString()))
            .andExpect(jsonPath("$.currencyBaseCode").value(DEFAULT_CURRENCY_BASE_CODE.toString()))
            .andExpect(jsonPath("$.currencyNonBaseCode").value(DEFAULT_CURRENCY_NON_BASE_CODE.toString()))
            .andExpect(jsonPath("$.bidPrice").value(DEFAULT_BID_PRICE.intValue()))
            .andExpect(jsonPath("$.askPrice").value(DEFAULT_ASK_PRICE.intValue()))
            .andExpect(jsonPath("$.midPrice").value(DEFAULT_MID_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getAllFxRatesByExtractTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where extractTime equals to DEFAULT_EXTRACT_TIME
        defaultFxRateShouldBeFound("extractTime.equals=" + DEFAULT_EXTRACT_TIME);

        // Get all the fxRateList where extractTime equals to UPDATED_EXTRACT_TIME
        defaultFxRateShouldNotBeFound("extractTime.equals=" + UPDATED_EXTRACT_TIME);
    }

    @Test
    @Transactional
    public void getAllFxRatesByExtractTimeIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where extractTime in DEFAULT_EXTRACT_TIME or UPDATED_EXTRACT_TIME
        defaultFxRateShouldBeFound("extractTime.in=" + DEFAULT_EXTRACT_TIME + "," + UPDATED_EXTRACT_TIME);

        // Get all the fxRateList where extractTime equals to UPDATED_EXTRACT_TIME
        defaultFxRateShouldNotBeFound("extractTime.in=" + UPDATED_EXTRACT_TIME);
    }

    @Test
    @Transactional
    public void getAllFxRatesByExtractTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where extractTime is not null
        defaultFxRateShouldBeFound("extractTime.specified=true");

        // Get all the fxRateList where extractTime is null
        defaultFxRateShouldNotBeFound("extractTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllFxRatesByArrivalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where arrivalDate equals to DEFAULT_ARRIVAL_DATE
        defaultFxRateShouldBeFound("arrivalDate.equals=" + DEFAULT_ARRIVAL_DATE);

        // Get all the fxRateList where arrivalDate equals to UPDATED_ARRIVAL_DATE
        defaultFxRateShouldNotBeFound("arrivalDate.equals=" + UPDATED_ARRIVAL_DATE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByArrivalDateIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where arrivalDate in DEFAULT_ARRIVAL_DATE or UPDATED_ARRIVAL_DATE
        defaultFxRateShouldBeFound("arrivalDate.in=" + DEFAULT_ARRIVAL_DATE + "," + UPDATED_ARRIVAL_DATE);

        // Get all the fxRateList where arrivalDate equals to UPDATED_ARRIVAL_DATE
        defaultFxRateShouldNotBeFound("arrivalDate.in=" + UPDATED_ARRIVAL_DATE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByArrivalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where arrivalDate is not null
        defaultFxRateShouldBeFound("arrivalDate.specified=true");

        // Get all the fxRateList where arrivalDate is null
        defaultFxRateShouldNotBeFound("arrivalDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFxRatesByArrivalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where arrivalDate greater than or equals to DEFAULT_ARRIVAL_DATE
        defaultFxRateShouldBeFound("arrivalDate.greaterOrEqualThan=" + DEFAULT_ARRIVAL_DATE);

        // Get all the fxRateList where arrivalDate greater than or equals to UPDATED_ARRIVAL_DATE
        defaultFxRateShouldNotBeFound("arrivalDate.greaterOrEqualThan=" + UPDATED_ARRIVAL_DATE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByArrivalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where arrivalDate less than or equals to DEFAULT_ARRIVAL_DATE
        defaultFxRateShouldNotBeFound("arrivalDate.lessThan=" + DEFAULT_ARRIVAL_DATE);

        // Get all the fxRateList where arrivalDate less than or equals to UPDATED_ARRIVAL_DATE
        defaultFxRateShouldBeFound("arrivalDate.lessThan=" + UPDATED_ARRIVAL_DATE);
    }


    @Test
    @Transactional
    public void getAllFxRatesByCurrencyQuoteIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where currencyQuote equals to DEFAULT_CURRENCY_QUOTE
        defaultFxRateShouldBeFound("currencyQuote.equals=" + DEFAULT_CURRENCY_QUOTE);

        // Get all the fxRateList where currencyQuote equals to UPDATED_CURRENCY_QUOTE
        defaultFxRateShouldNotBeFound("currencyQuote.equals=" + UPDATED_CURRENCY_QUOTE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByCurrencyQuoteIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where currencyQuote in DEFAULT_CURRENCY_QUOTE or UPDATED_CURRENCY_QUOTE
        defaultFxRateShouldBeFound("currencyQuote.in=" + DEFAULT_CURRENCY_QUOTE + "," + UPDATED_CURRENCY_QUOTE);

        // Get all the fxRateList where currencyQuote equals to UPDATED_CURRENCY_QUOTE
        defaultFxRateShouldNotBeFound("currencyQuote.in=" + UPDATED_CURRENCY_QUOTE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByCurrencyQuoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where currencyQuote is not null
        defaultFxRateShouldBeFound("currencyQuote.specified=true");

        // Get all the fxRateList where currencyQuote is null
        defaultFxRateShouldNotBeFound("currencyQuote.specified=false");
    }

    @Test
    @Transactional
    public void getAllFxRatesByCurrencyBaseCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where currencyBaseCode equals to DEFAULT_CURRENCY_BASE_CODE
        defaultFxRateShouldBeFound("currencyBaseCode.equals=" + DEFAULT_CURRENCY_BASE_CODE);

        // Get all the fxRateList where currencyBaseCode equals to UPDATED_CURRENCY_BASE_CODE
        defaultFxRateShouldNotBeFound("currencyBaseCode.equals=" + UPDATED_CURRENCY_BASE_CODE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByCurrencyBaseCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where currencyBaseCode in DEFAULT_CURRENCY_BASE_CODE or UPDATED_CURRENCY_BASE_CODE
        defaultFxRateShouldBeFound("currencyBaseCode.in=" + DEFAULT_CURRENCY_BASE_CODE + "," + UPDATED_CURRENCY_BASE_CODE);

        // Get all the fxRateList where currencyBaseCode equals to UPDATED_CURRENCY_BASE_CODE
        defaultFxRateShouldNotBeFound("currencyBaseCode.in=" + UPDATED_CURRENCY_BASE_CODE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByCurrencyBaseCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where currencyBaseCode is not null
        defaultFxRateShouldBeFound("currencyBaseCode.specified=true");

        // Get all the fxRateList where currencyBaseCode is null
        defaultFxRateShouldNotBeFound("currencyBaseCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllFxRatesByCurrencyNonBaseCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where currencyNonBaseCode equals to DEFAULT_CURRENCY_NON_BASE_CODE
        defaultFxRateShouldBeFound("currencyNonBaseCode.equals=" + DEFAULT_CURRENCY_NON_BASE_CODE);

        // Get all the fxRateList where currencyNonBaseCode equals to UPDATED_CURRENCY_NON_BASE_CODE
        defaultFxRateShouldNotBeFound("currencyNonBaseCode.equals=" + UPDATED_CURRENCY_NON_BASE_CODE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByCurrencyNonBaseCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where currencyNonBaseCode in DEFAULT_CURRENCY_NON_BASE_CODE or UPDATED_CURRENCY_NON_BASE_CODE
        defaultFxRateShouldBeFound("currencyNonBaseCode.in=" + DEFAULT_CURRENCY_NON_BASE_CODE + "," + UPDATED_CURRENCY_NON_BASE_CODE);

        // Get all the fxRateList where currencyNonBaseCode equals to UPDATED_CURRENCY_NON_BASE_CODE
        defaultFxRateShouldNotBeFound("currencyNonBaseCode.in=" + UPDATED_CURRENCY_NON_BASE_CODE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByCurrencyNonBaseCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where currencyNonBaseCode is not null
        defaultFxRateShouldBeFound("currencyNonBaseCode.specified=true");

        // Get all the fxRateList where currencyNonBaseCode is null
        defaultFxRateShouldNotBeFound("currencyNonBaseCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllFxRatesByBidPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where bidPrice equals to DEFAULT_BID_PRICE
        defaultFxRateShouldBeFound("bidPrice.equals=" + DEFAULT_BID_PRICE);

        // Get all the fxRateList where bidPrice equals to UPDATED_BID_PRICE
        defaultFxRateShouldNotBeFound("bidPrice.equals=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByBidPriceIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where bidPrice in DEFAULT_BID_PRICE or UPDATED_BID_PRICE
        defaultFxRateShouldBeFound("bidPrice.in=" + DEFAULT_BID_PRICE + "," + UPDATED_BID_PRICE);

        // Get all the fxRateList where bidPrice equals to UPDATED_BID_PRICE
        defaultFxRateShouldNotBeFound("bidPrice.in=" + UPDATED_BID_PRICE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByBidPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where bidPrice is not null
        defaultFxRateShouldBeFound("bidPrice.specified=true");

        // Get all the fxRateList where bidPrice is null
        defaultFxRateShouldNotBeFound("bidPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllFxRatesByAskPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where askPrice equals to DEFAULT_ASK_PRICE
        defaultFxRateShouldBeFound("askPrice.equals=" + DEFAULT_ASK_PRICE);

        // Get all the fxRateList where askPrice equals to UPDATED_ASK_PRICE
        defaultFxRateShouldNotBeFound("askPrice.equals=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByAskPriceIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where askPrice in DEFAULT_ASK_PRICE or UPDATED_ASK_PRICE
        defaultFxRateShouldBeFound("askPrice.in=" + DEFAULT_ASK_PRICE + "," + UPDATED_ASK_PRICE);

        // Get all the fxRateList where askPrice equals to UPDATED_ASK_PRICE
        defaultFxRateShouldNotBeFound("askPrice.in=" + UPDATED_ASK_PRICE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByAskPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where askPrice is not null
        defaultFxRateShouldBeFound("askPrice.specified=true");

        // Get all the fxRateList where askPrice is null
        defaultFxRateShouldNotBeFound("askPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllFxRatesByMidPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where midPrice equals to DEFAULT_MID_PRICE
        defaultFxRateShouldBeFound("midPrice.equals=" + DEFAULT_MID_PRICE);

        // Get all the fxRateList where midPrice equals to UPDATED_MID_PRICE
        defaultFxRateShouldNotBeFound("midPrice.equals=" + UPDATED_MID_PRICE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByMidPriceIsInShouldWork() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where midPrice in DEFAULT_MID_PRICE or UPDATED_MID_PRICE
        defaultFxRateShouldBeFound("midPrice.in=" + DEFAULT_MID_PRICE + "," + UPDATED_MID_PRICE);

        // Get all the fxRateList where midPrice equals to UPDATED_MID_PRICE
        defaultFxRateShouldNotBeFound("midPrice.in=" + UPDATED_MID_PRICE);
    }

    @Test
    @Transactional
    public void getAllFxRatesByMidPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);

        // Get all the fxRateList where midPrice is not null
        defaultFxRateShouldBeFound("midPrice.specified=true");

        // Get all the fxRateList where midPrice is null
        defaultFxRateShouldNotBeFound("midPrice.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFxRateShouldBeFound(String filter) throws Exception {
        restFxRateMockMvc.perform(get("/api/fx-rates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fxRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].extractTime").value(hasItem(DEFAULT_EXTRACT_TIME.toString())))
            .andExpect(jsonPath("$.[*].arrivalDate").value(hasItem(DEFAULT_ARRIVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].currencyQuote").value(hasItem(DEFAULT_CURRENCY_QUOTE.toString())))
            .andExpect(jsonPath("$.[*].currencyBaseCode").value(hasItem(DEFAULT_CURRENCY_BASE_CODE.toString())))
            .andExpect(jsonPath("$.[*].currencyNonBaseCode").value(hasItem(DEFAULT_CURRENCY_NON_BASE_CODE.toString())))
            .andExpect(jsonPath("$.[*].bidPrice").value(hasItem(DEFAULT_BID_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].askPrice").value(hasItem(DEFAULT_ASK_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].midPrice").value(hasItem(DEFAULT_MID_PRICE.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFxRateShouldNotBeFound(String filter) throws Exception {
        restFxRateMockMvc.perform(get("/api/fx-rates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingFxRate() throws Exception {
        // Get the fxRate
        restFxRateMockMvc.perform(get("/api/fx-rates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFxRate() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);
        int databaseSizeBeforeUpdate = fxRateRepository.findAll().size();

        // Update the fxRate
        FxRate updatedFxRate = fxRateRepository.findOne(fxRate.getId());
        updatedFxRate
            .extractTime(UPDATED_EXTRACT_TIME)
            .arrivalDate(UPDATED_ARRIVAL_DATE)
            .currencyQuote(UPDATED_CURRENCY_QUOTE)
            .currencyBaseCode(UPDATED_CURRENCY_BASE_CODE)
            .currencyNonBaseCode(UPDATED_CURRENCY_NON_BASE_CODE)
            .bidPrice(UPDATED_BID_PRICE)
            .askPrice(UPDATED_ASK_PRICE)
            .midPrice(UPDATED_MID_PRICE);
        FxRateDTO fxRateDTO = fxRateMapper.toDto(updatedFxRate);

        restFxRateMockMvc.perform(put("/api/fx-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRateDTO)))
            .andExpect(status().isOk());

        // Validate the FxRate in the database
        List<FxRate> fxRateList = fxRateRepository.findAll();
        assertThat(fxRateList).hasSize(databaseSizeBeforeUpdate);
        FxRate testFxRate = fxRateList.get(fxRateList.size() - 1);
        assertThat(testFxRate.getExtractTime()).isEqualTo(UPDATED_EXTRACT_TIME);
        assertThat(testFxRate.getArrivalDate()).isEqualTo(UPDATED_ARRIVAL_DATE);
        assertThat(testFxRate.getCurrencyQuote()).isEqualTo(UPDATED_CURRENCY_QUOTE);
        assertThat(testFxRate.getCurrencyBaseCode()).isEqualTo(UPDATED_CURRENCY_BASE_CODE);
        assertThat(testFxRate.getCurrencyNonBaseCode()).isEqualTo(UPDATED_CURRENCY_NON_BASE_CODE);
        assertThat(testFxRate.getBidPrice()).isEqualTo(UPDATED_BID_PRICE);
        assertThat(testFxRate.getAskPrice()).isEqualTo(UPDATED_ASK_PRICE);
        assertThat(testFxRate.getMidPrice()).isEqualTo(UPDATED_MID_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingFxRate() throws Exception {
        int databaseSizeBeforeUpdate = fxRateRepository.findAll().size();

        // Create the FxRate
        FxRateDTO fxRateDTO = fxRateMapper.toDto(fxRate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFxRateMockMvc.perform(put("/api/fx-rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fxRateDTO)))
            .andExpect(status().isCreated());

        // Validate the FxRate in the database
        List<FxRate> fxRateList = fxRateRepository.findAll();
        assertThat(fxRateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFxRate() throws Exception {
        // Initialize the database
        fxRateRepository.saveAndFlush(fxRate);
        int databaseSizeBeforeDelete = fxRateRepository.findAll().size();

        // Get the fxRate
        restFxRateMockMvc.perform(delete("/api/fx-rates/{id}", fxRate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FxRate> fxRateList = fxRateRepository.findAll();
        assertThat(fxRateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FxRate.class);
        FxRate fxRate1 = new FxRate();
        fxRate1.setId(1L);
        FxRate fxRate2 = new FxRate();
        fxRate2.setId(fxRate1.getId());
        assertThat(fxRate1).isEqualTo(fxRate2);
        fxRate2.setId(2L);
        assertThat(fxRate1).isNotEqualTo(fxRate2);
        fxRate1.setId(null);
        assertThat(fxRate1).isNotEqualTo(fxRate2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FxRateDTO.class);
        FxRateDTO fxRateDTO1 = new FxRateDTO();
        fxRateDTO1.setId(1L);
        FxRateDTO fxRateDTO2 = new FxRateDTO();
        assertThat(fxRateDTO1).isNotEqualTo(fxRateDTO2);
        fxRateDTO2.setId(fxRateDTO1.getId());
        assertThat(fxRateDTO1).isEqualTo(fxRateDTO2);
        fxRateDTO2.setId(2L);
        assertThat(fxRateDTO1).isNotEqualTo(fxRateDTO2);
        fxRateDTO1.setId(null);
        assertThat(fxRateDTO1).isNotEqualTo(fxRateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fxRateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(fxRateMapper.fromId(null)).isNull();
    }
}
