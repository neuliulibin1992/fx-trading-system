package com.enginemobi.fx.service.impl;

import com.enginemobi.fx.service.FxRateService;
import com.enginemobi.fx.domain.FxRate;
import com.enginemobi.fx.repository.FxRateRepository;
import com.enginemobi.fx.service.dto.FxRateDTO;
import com.enginemobi.fx.service.mapper.FxRateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing FxRate.
 */
@Service
@Transactional
public class FxRateServiceImpl implements FxRateService{

    private final Logger log = LoggerFactory.getLogger(FxRateServiceImpl.class);

    private final FxRateRepository fxRateRepository;

    private final FxRateMapper fxRateMapper;

    public FxRateServiceImpl(FxRateRepository fxRateRepository, FxRateMapper fxRateMapper) {
        this.fxRateRepository = fxRateRepository;
        this.fxRateMapper = fxRateMapper;
    }

    /**
     * Save a fxRate.
     *
     * @param fxRateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FxRateDTO save(FxRateDTO fxRateDTO) {
        log.debug("Request to save FxRate : {}", fxRateDTO);
        FxRate fxRate = fxRateMapper.toEntity(fxRateDTO);
        fxRate = fxRateRepository.save(fxRate);
        return fxRateMapper.toDto(fxRate);
    }

    /**
     * Get all the fxRates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FxRateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FxRates");
        return fxRateRepository.findAll(pageable)
            .map(fxRateMapper::toDto);
    }

    /**
     * Get one fxRate by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FxRateDTO findOne(Long id) {
        log.debug("Request to get FxRate : {}", id);
        FxRate fxRate = fxRateRepository.findOne(id);
        return fxRateMapper.toDto(fxRate);
    }

    /**
     * Delete the fxRate by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FxRate : {}", id);
        fxRateRepository.delete(id);
    }
}
