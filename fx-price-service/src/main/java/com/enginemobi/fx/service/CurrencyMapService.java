package com.enginemobi.fx.service;

import com.enginemobi.fx.service.dto.CurrencyMapDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CurrencyMap.
 */
public interface CurrencyMapService {

    /**
     * Save a currencyMap.
     *
     * @param currencyMapDTO the entity to save
     * @return the persisted entity
     */
    CurrencyMapDTO save(CurrencyMapDTO currencyMapDTO);

    /**
     *  Get all the currencyMaps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CurrencyMapDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" currencyMap.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CurrencyMapDTO findOne(Long id);

    /**
     *  Delete the "id" currencyMap.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
