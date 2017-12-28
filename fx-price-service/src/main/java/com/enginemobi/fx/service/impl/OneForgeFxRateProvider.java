package com.enginemobi.fx.service.impl;

import com.enginemobi.fx.config.ApplicationProperties;
import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import com.enginemobi.fx.service.CurrencyMapService;
import com.enginemobi.fx.service.FxRateProvider;
import com.enginemobi.fx.service.FxRateService;
import com.enginemobi.fx.service.dto.CurrencyMapDTO;
import com.enginemobi.fx.service.dto.FxRateDTO;
import com.enginemobi.fx.service.dto.oneforge.FxRateOneForge;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

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
        log.info("Getting fx rates from 1Forex");

        List<CurrencyMap> supportedCurrencies = currencyMapService.getByProvider(CurrencyRateProvider.ONE_FORGE);
        List<String> supportedSymbols = supportedCurrencies.stream().map(CurrencyMap::getCurrencyQuote).collect(Collectors.toList());

        if (supportedSymbols.isEmpty()) {
            log.warn("No supported symbols for OneForge, skip fetching fx rates");
            return;
        }

        List<List<String>> smallListOfSupportedSymbols = Lists.partition(supportedSymbols, 100);
        List<FxRateOneForge> allRates = newArrayList();

        for(List<String> listOfSupportedSymbols : smallListOfSupportedSymbols) {
            log.debug("Fetching fx rates for symbols {}. ", listOfSupportedSymbols);

            RestTemplate restTemplate = new RestTemplate();
            String url = constructUrlToGetFxRates(listOfSupportedSymbols);

            FxRateOneForge[] oneforgeFxRates = restTemplate.getForObject(url, FxRateOneForge[].class);
            allRates.addAll(Arrays.asList(oneforgeFxRates));
        }

        if (allRates.isEmpty()) {
            log.warn("No fx rates are returned from OneForge");
            return;
        }


        persistFxRates(allRates);
        log.info("Persisted fx rates from {}", CurrencyRateProvider.ONE_FORGE);


    }


    private String constructUrlToGetFxRates(List<String> symbols) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(appProperties.getRateProvider().getOneForge().getBaseUrl());
        urlBuilder.append("quotes?pairs=");
        urlBuilder.append(String.join(",", symbols));
        urlBuilder.append("&api_key=");
        urlBuilder.append(appProperties.getRateProvider().getOneForge().getApiKey());

        return urlBuilder.toString();
    }

    private void persistFxRates(List<FxRateOneForge> allRates) {
        for (FxRateOneForge oneForgefxRate: allRates) {

            FxRateDTO fxrateDto = new FxRateDTO();
            fxrateDto.setAskPrice(BigDecimal.valueOf(oneForgefxRate.getAsk()));
            fxrateDto.setBidPrice(BigDecimal.valueOf(oneForgefxRate.getBid()));
            fxrateDto.setMidPrice(BigDecimal.valueOf(oneForgefxRate.getPrice()));
            fxrateDto.setCurrencyQuote(oneForgefxRate.getSymbol());
            fxrateDto.setCurrencyBaseCode(oneForgefxRate.getSymbol().substring(0, 3));
            fxrateDto.setCurrencyNonBaseCode(oneForgefxRate.getSymbol().substring(3, 6));
            fxrateDto.setExtractTime(oneForgefxRate.getTimestamp());
            fxrateDto.setArrivalDate(LocalDate.now());

            fxRateService.save(fxrateDto);
        }
    }

    @Override
    @Scheduled(fixedDelay = 3600000)
    public void updateSupportedCurrencies() {
        log.info("Start updating supported symbols");

        RestTemplate restTemplate = new RestTemplate();
        String url = constructUrlToGetListOfSymbols();

        log.debug("Retrieving supported symbols from url {}", url);

        ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(url, String[].class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            log.error("Error occurred when getting supported symbols from 1forge");
            return;
        }


        List<String> supportedSymbols = Arrays.asList(responseEntity.getBody());

        if (supportedSymbols.isEmpty()) {
            log.warn("No currency symbols to be persisted");
            return;
        }

        // Remove existing symbols first
        currencyMapService.deleteByProvider(CurrencyRateProvider.ONE_FORGE);

        persistSupportedSymbols(supportedSymbols);
        log.info("Updated supported symbols from {}", CurrencyRateProvider.ONE_FORGE);


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
        for (String curSymbol: supportedSymbols) {
            CurrencyMapDTO currMapDto = new CurrencyMapDTO();
            currMapDto.setCurrencyQuote(curSymbol);
            currMapDto.setCurrencyBaseCode(curSymbol.substring(0, 3));
            currMapDto.setCurrencyNonBaseCode(curSymbol.substring(3, 6));
            currMapDto.setRateProvider(CurrencyRateProvider.ONE_FORGE);
            currencyMapService.save(currMapDto);
        }

    }


}
