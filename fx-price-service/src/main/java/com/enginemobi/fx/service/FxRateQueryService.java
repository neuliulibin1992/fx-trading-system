package com.enginemobi.fx.service;

import com.enginemobi.fx.domain.FxRate;
import com.enginemobi.fx.domain.FxRate_;
import com.enginemobi.fx.repository.FxRateRepository;
import com.enginemobi.fx.service.dto.FxRateCriteria;
import com.enginemobi.fx.service.dto.FxRateDTO;
import com.enginemobi.fx.service.mapper.FxRateMapper;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for FxRate entities in the database.
 * The main input is a {@link FxRateCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link FxRateDTO} or a {@link Page} of {%link FxRateDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class FxRateQueryService extends QueryService<FxRate> {

    private final Logger log = LoggerFactory.getLogger(FxRateQueryService.class);


    private final FxRateRepository fxRateRepository;

    private final FxRateMapper fxRateMapper;
    public FxRateQueryService(FxRateRepository fxRateRepository, FxRateMapper fxRateMapper) {
        this.fxRateRepository = fxRateRepository;
        this.fxRateMapper = fxRateMapper;
    }

    /**
     * Return a {@link List} of {%link FxRateDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FxRateDTO> findByCriteria(FxRateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<FxRate> specification = createSpecification(criteria);
        return fxRateMapper.toDto(fxRateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link FxRateDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FxRateDTO> findByCriteria(FxRateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<FxRate> specification = createSpecification(criteria);
        final Page<FxRate> result = fxRateRepository.findAll(specification, page);
        return result.map(fxRateMapper::toDto);
    }

    /**
     * Function to convert FxRateCriteria to a {@link Specifications}
     */
    private Specifications<FxRate> createSpecification(FxRateCriteria criteria) {
        Specifications<FxRate> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FxRate_.id));
            }
            if (criteria.getExtractTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExtractTime(), FxRate_.extractTime));
            }
            if (criteria.getArrivalDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getArrivalDate(), FxRate_.arrivalDate));
            }
            if (criteria.getCurrencyQuote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyQuote(), FxRate_.currencyQuote));
            }
            if (criteria.getCurrencyBaseCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyBaseCode(), FxRate_.currencyBaseCode));
            }
            if (criteria.getCurrencyNonBaseCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyNonBaseCode(), FxRate_.currencyNonBaseCode));
            }
            if (criteria.getBidPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBidPrice(), FxRate_.bidPrice));
            }
            if (criteria.getAskPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAskPrice(), FxRate_.askPrice));
            }
            if (criteria.getMidPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMidPrice(), FxRate_.midPrice));
            }
        }
        return specification;
    }

}
