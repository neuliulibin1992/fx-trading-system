package com.enginemobi.fx.service;


import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.domain.CurrencyMap_;
import com.enginemobi.fx.repository.CurrencyMapRepository;
import com.enginemobi.fx.service.dto.CurrencyMapCriteria;
import com.enginemobi.fx.service.dto.CurrencyMapDTO;
import com.enginemobi.fx.service.mapper.CurrencyMapMapper;
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
 * Service for executing complex queries for CurrencyMap entities in the database.
 * The main input is a {@link CurrencyMapCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link CurrencyMapDTO} or a {@link Page} of {%link CurrencyMapDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class CurrencyMapQueryService extends QueryService<CurrencyMap> {

    private final Logger log = LoggerFactory.getLogger(CurrencyMapQueryService.class);


    private final CurrencyMapRepository currencyMapRepository;

    private final CurrencyMapMapper currencyMapMapper;
    public CurrencyMapQueryService(CurrencyMapRepository currencyMapRepository, CurrencyMapMapper currencyMapMapper) {
        this.currencyMapRepository = currencyMapRepository;
        this.currencyMapMapper = currencyMapMapper;
    }

    /**
     * Return a {@link List} of {%link CurrencyMapDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CurrencyMapDTO> findByCriteria(CurrencyMapCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<CurrencyMap> specification = createSpecification(criteria);
        return currencyMapMapper.toDto(currencyMapRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link CurrencyMapDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CurrencyMapDTO> findByCriteria(CurrencyMapCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<CurrencyMap> specification = createSpecification(criteria);
        final Page<CurrencyMap> result = currencyMapRepository.findAll(specification, page);
        return result.map(currencyMapMapper::toDto);
    }

    /**
     * Function to convert CurrencyMapCriteria to a {@link Specifications}
     */
    private Specifications<CurrencyMap> createSpecification(CurrencyMapCriteria criteria) {
        Specifications<CurrencyMap> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CurrencyMap_.id));
            }
            if (criteria.getCurrencyQuote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyQuote(), CurrencyMap_.currencyQuote));
            }
            if (criteria.getCurrencyBaseCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyBaseCode(), CurrencyMap_.currencyBaseCode));
            }
            if (criteria.getCurrencyNonBaseCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyNonBaseCode(), CurrencyMap_.currencyNonBaseCode));
            }
            if (criteria.getRateProvider() != null) {
                specification = specification.and(buildSpecification(criteria.getRateProvider(), CurrencyMap_.rateProvider));
            }
        }
        return specification;
    }

}
