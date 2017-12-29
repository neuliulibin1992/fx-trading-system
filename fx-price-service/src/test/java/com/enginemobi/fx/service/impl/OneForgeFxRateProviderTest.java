package com.enginemobi.fx.service.impl;

import com.enginemobi.fx.config.ApplicationProperties;
import com.enginemobi.fx.service.CurrencyMapService;
import com.enginemobi.fx.service.FxRateService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class OneForgeFxRateProviderTest {

    private OneForgeFxRateProvider oneForgeFxRateProvider;

    @Mock
    private ApplicationProperties appProperties;

    @Mock
    private CurrencyMapService currencyMapService;

    @Mock
    private FxRateService fxRateService;

    @Mock
    private RestTemplate restTemplate;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        oneForgeFxRateProvider = new OneForgeFxRateProvider(appProperties, currencyMapService, fxRateService, restTemplate);

        when(appProperties.getRateProvider()).thenReturn(mock(ApplicationProperties.RateProvider.class));
        when(appProperties.getRateProvider().getOneForge()).thenReturn(mock(ApplicationProperties.RateProvider.OneForge.class));

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void test_disable_scheduler_fxRate() throws Exception {
        when(appProperties.getRateProvider().getOneForge().isEnabled()).thenReturn(false);

        oneForgeFxRateProvider.fetchFxRates();

        verify(currencyMapService, never()).getByProvider(any());
    }

    @Test
    public void test_enable_scheduler_fxRate() throws Exception {
        when(appProperties.getRateProvider().getOneForge().isEnabled()).thenReturn(true);

        oneForgeFxRateProvider.fetchFxRates();

        verify(currencyMapService, times(1)).getByProvider(any());
    }

    @Test
    public void test_disable_scheduler_symbols() throws Exception {
        when(appProperties.getRateProvider().getOneForge().isEnabled()).thenReturn(false);

        oneForgeFxRateProvider.updateSupportedCurrencies();

        verify(currencyMapService, never()).getByProvider(any());
    }

    @Test
    public void test_enable_scheduler_symbols() throws Exception {
        when(appProperties.getRateProvider().getOneForge().isEnabled()).thenReturn(true);
        ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(mockResponse);

        oneForgeFxRateProvider.updateSupportedCurrencies();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());
    }
}
