package com.enginemobi.fx.service;

import com.enginemobi.fx.service.dto.FxRateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing FxRate.
 */
public interface FxRateService {

    /**
     * Save a fxRate.
     *
     * @param fxRateDTO the entity to save
     * @return the persisted entity
     */
    FxRateDTO save(FxRateDTO fxRateDTO);

    /**
     *  Get all the fxRates.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FxRateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" fxRate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FxRateDTO findOne(Long id);

    /**
     *  Delete the "id" fxRate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
