package com.enginemobi.fx.service.impl;

import com.enginemobi.fx.config.ApplicationProperties;
import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import com.enginemobi.fx.service.CurrencyMapService;
import com.enginemobi.fx.service.FxRateProvider;
import com.enginemobi.fx.service.FxRateService;
import com.enginemobi.fx.service.dto.CurrencyMapDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for FxRate Provider.
 * Fx rates provided by 1forge
 * @see <a href="https://1forge.com">https://1forge.com</a>
 */
@Service
@Transactional
public class OneForgeFxRateProvider implements FxRateProvider {

    private final Logger log = LoggerFactory.getLogger(OneForgeFxRateProvider.class);

    private ApplicationProperties appProperties;
    private CurrencyMapService currencyMapService;
    private FxRateService fxRateService;

    public OneForgeFxRateProvider() {}

    public OneForgeFxRateProvider(ApplicationProperties applicationProperties,
                                  CurrencyMapService currencyMapService,
                                  FxRateService fxRateService) {
        appProperties = applicationProperties;
        this.currencyMapService = currencyMapService;
        this.fxRateService = fxRateService;
    }


    @Override
    @Scheduled(fixedDelayString = "${application.rate-provider.one-forge.fixed-delay}")
    public void fetchFxRates() {
        log.info("Start getting fx rates from 1Forex");


    }

    @Override
    @Scheduled(fixedDelay = 60000)
    public void updateSupportedCurrencies() {
        log.info("Start updating supported symbols");

        RestTemplate restTemplate = new RestTemplate();
        String url = constructUrlToGetListOfSymbols();

        log.debug("Retrieving supported symbols from url {}", url);

        ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(url, String[].class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String[] supportedSymbls = responseEntity.getBody();
            persistSupportedSymbols(Arrays.asList(supportedSymbls));
            log.info("Updated supported symbols from {}", CurrencyRateProvider.ONE_FORGE);
        } else {
            log.error("Error occurred when getting supported symbols from 1forge");
        }

    }

    private String constructUrlToGetListOfSymbols() {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(appProperties.getRateProvider().getOneForge().getBaseUrl());
        urlBuilder.append("symbols?");
        urlBuilder.append("api_key=");
        urlBuilder.append(appProperties.getRateProvider().getOneForge().getApiKey());

        return urlBuilder.toString();
    }

    private void persistSupportedSymbols(List<String> supportedSymbols) {
        if (supportedSymbols.isEmpty()) {
            log.warn("No symbol to be persisted, skipping");
            return;
        }

        for (String curSymbol: supportedSymbols) {
            CurrencyMapDTO currMapDto = new CurrencyMapDTO();
            currMapDto.setCurrencyQuote(curSymbol);
            currMapDto.setCurrencyBaseCode(curSymbol.substring(0, 2));
            currMapDto.setCurrencyBaseCode(curSymbol.substring(3, 5));
            currMapDto.setProvidedBy(CurrencyRateProvider.ONE_FORGE);
            currencyMapService.save(currMapDto);
        }

    }


}
