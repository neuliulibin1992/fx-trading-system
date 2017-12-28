package com.enginemobi.fx.repository;

import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the CurrencyMap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyMapRepository extends JpaRepository<CurrencyMap, Long> {

    /**
     * delete currency map by rate provider
     * @param rateProvider
     * @return
     */
    Long deleteByRateProvider(CurrencyRateProvider rateProvider);

    List<CurrencyMap> getByRateProvider(CurrencyRateProvider rateProvider);
}
