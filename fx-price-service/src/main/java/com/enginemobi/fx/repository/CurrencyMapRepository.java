package com.enginemobi.fx.repository;

import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


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
    Long deleteByProvidedBy(CurrencyRateProvider rateProvider);
}
