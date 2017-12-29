package com.enginemobi.fx.repository;

import com.enginemobi.fx.FxpriceserviceApp;
import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FxpriceserviceApp.class)
public class CurrencyMapRepositoryIntTest {

    @Autowired
    private CurrencyMapRepository currencyMapRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        currencyMapRepository.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        currencyMapRepository.deleteAll();
    }

    @Test
    @Transactional
    public void test_deleteByRateProvider() throws Exception {
        CurrencyMap currencyMap = new CurrencyMap();
        currencyMap.setRateProvider(CurrencyRateProvider.ONE_FORGE);
        currencyMap.setCurrencyBaseCode("USD");
        currencyMap.setCurrencyNonBaseCode("AUD");
        currencyMapRepository.save(currencyMap);

        currencyMap = new CurrencyMap();
        currencyMap.setRateProvider(CurrencyRateProvider.CURRENCY_LAYER);
        currencyMap.setCurrencyBaseCode("USD");
        currencyMap.setCurrencyNonBaseCode("AUD");
        currencyMapRepository.save(currencyMap);

        List<CurrencyMap> currMaps = currencyMapRepository.findAll();

        assertThat(currMaps.size()).isEqualTo(2);

        // remove one cur
        currencyMapRepository.deleteByRateProvider(CurrencyRateProvider.ONE_FORGE);

        currMaps = currencyMapRepository.getByRateProvider(CurrencyRateProvider.CURRENCY_LAYER);

        assertThat(currMaps.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void test_getByRateProvider() throws Exception {
        CurrencyMap currencyMap = new CurrencyMap();
        currencyMap.setRateProvider(CurrencyRateProvider.ONE_FORGE);
        currencyMap.setCurrencyBaseCode("USD");
        currencyMap.setCurrencyNonBaseCode("AUD");

        currencyMapRepository.save(currencyMap);

        List<CurrencyMap> currMaps = currencyMapRepository.getByRateProvider(CurrencyRateProvider.ONE_FORGE);

        assertThat(currMaps.size()).isEqualTo(1);
    }

}
