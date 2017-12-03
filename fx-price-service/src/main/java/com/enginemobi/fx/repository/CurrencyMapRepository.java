package com.enginemobi.fx.repository;

import com.enginemobi.fx.domain.CurrencyMap;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CurrencyMap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyMapRepository extends JpaRepository<CurrencyMap, Long> {

}
