package com.enginemobi.fx.repository;

import com.enginemobi.fx.domain.FxRate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FxRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FxRateRepository extends JpaRepository<FxRate, Long>, JpaSpecificationExecutor<FxRate> {

}
