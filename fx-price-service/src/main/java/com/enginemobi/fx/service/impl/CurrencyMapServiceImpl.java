package com.enginemobi.fx.service.impl;

import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import com.enginemobi.fx.service.CurrencyMapService;
import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.repository.CurrencyMapRepository;
import com.enginemobi.fx.service.dto.CurrencyMapDTO;
import com.enginemobi.fx.service.mapper.CurrencyMapMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing CurrencyMap.
 */
@Service
@Transactional
public class CurrencyMapServiceImpl implements CurrencyMapService{

    private final Logger log = LoggerFactory.getLogger(CurrencyMapServiceImpl.class);

    private final CurrencyMapRepository currencyMapRepository;

    private final CurrencyMapMapper currencyMapMapper;
    public CurrencyMapServiceImpl(CurrencyMapRepository currencyMapRepository, CurrencyMapMapper currencyMapMapper) {
        this.currencyMapRepository = currencyMapRepository;
        this.currencyMapMapper = currencyMapMapper;
    }

    /**
     * Save a currencyMap.
     *
     * @param currencyMapDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CurrencyMapDTO save(CurrencyMapDTO currencyMapDTO) {
        log.debug("Request to save CurrencyMap : {}", currencyMapDTO);
        CurrencyMap currencyMap = currencyMapMapper.toEntity(currencyMapDTO);
        currencyMap = currencyMapRepository.save(currencyMap);
        return currencyMapMapper.toDto(currencyMap);
    }

    /**
     *  Get all the currencyMaps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyMapDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CurrencyMaps");
        return currencyMapRepository.findAll(pageable)
            .map(currencyMapMapper::toDto);
    }

    /**
     *  Get one currencyMap by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CurrencyMapDTO findOne(Long id) {
        log.debug("Request to get CurrencyMap : {}", id);
        CurrencyMap currencyMap = currencyMapRepository.findOne(id);
        return currencyMapMapper.toDto(currencyMap);
    }

    /**
     *  Delete the  currencyMap by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CurrencyMap : {}", id);
        currencyMapRepository.delete(id);
    }

    @Override
    public Long deleteByProvider(CurrencyRateProvider rateProvider) {
        return currencyMapRepository.deleteByRateProvider(rateProvider);
    }

    @Override
    public List<CurrencyMap> getByProvider(CurrencyRateProvider rateProvider) {
        return currencyMapRepository.getByRateProvider(rateProvider);
    }
}
