package com.enginemobi.fx.service.impl;

import com.enginemobi.fx.FxpriceserviceApp;
import com.enginemobi.fx.config.ApplicationProperties;
import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.domain.FxRate;
import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import com.enginemobi.fx.repository.CurrencyMapRepository;
import com.enginemobi.fx.repository.FxRateRepository;
import com.enginemobi.fx.service.CurrencyMapService;
import com.enginemobi.fx.service.FxRateService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.enginemobi.fx.prototype.CurrencyMapData.createAUDUSD;
import static com.enginemobi.fx.prototype.CurrencyMapData.createEURUSD;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = FxpriceserviceApp.class)
@Transactional
public class OneForgeFxRateProviderIntTest {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private CurrencyMapService currencyMapService;

    @Autowired
    private FxRateService fxRateService;

    @Autowired
    private RestTemplate restTemplate;

    private OneForgeFxRateProvider oneForgeRateProvider;

    @Autowired
    private CurrencyMapRepository currencyMapRepo;

    @Autowired
    private FxRateRepository fxRateRepo;

    private List<CurrencyMap> curMaps;

    @Before
    public void setUp() throws Exception {
        applicationProperties.getRateProvider().getOneForge().setEnabled(true);
        oneForgeRateProvider = new OneForgeFxRateProvider(applicationProperties, currencyMapService, fxRateService, restTemplate);
        curMaps = createEntities();
    }

    @After
    public void tearDown() throws Exception {
    }

    private List<CurrencyMap> createEntities() {
        return Arrays.asList(createAUDUSD(), createEURUSD());
    }

    @Test
    public void fetchFxRates() throws Exception {
        currencyMapRepo.deleteAll();
        fxRateRepo.deleteAll();
        List<FxRate> actualFxRates = fxRateRepo.findAll();
        assertThat(actualFxRates).isEmpty();

        currencyMapRepo.save(curMaps);

        oneForgeRateProvider.fetchFxRates();

        actualFxRates = fxRateRepo.findAll();
        assertThat(actualFxRates.size()).isEqualTo(2);
    }

    @Test
    public void updateSupportedCurrencies() throws Exception {
        currencyMapRepo.deleteAll();
        fxRateRepo.deleteAll();

        List<CurrencyMap> actualCurrMaps = currencyMapRepo.findAll();
        assertThat(actualCurrMaps).isEmpty();

        oneForgeRateProvider.updateSupportedCurrencies();

        actualCurrMaps = currencyMapRepo.findAll();
        assertThat(actualCurrMaps).isNotEmpty();

        for(CurrencyMap curMap : actualCurrMaps) {
            assertThat(curMap.getRateProvider()).isEqualTo(CurrencyRateProvider.ONE_FORGE);
        }
    }

}
