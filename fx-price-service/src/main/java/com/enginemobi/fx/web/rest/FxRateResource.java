package com.enginemobi.fx.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enginemobi.fx.service.FxRateService;
import com.enginemobi.fx.web.rest.util.HeaderUtil;
import com.enginemobi.fx.web.rest.util.PaginationUtil;
import com.enginemobi.fx.service.dto.FxRateDTO;
import com.enginemobi.fx.service.dto.FxRateCriteria;
import com.enginemobi.fx.service.FxRateQueryService;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FxRate.
 */
@RestController
@RequestMapping("/api")
public class FxRateResource {

    private final Logger log = LoggerFactory.getLogger(FxRateResource.class);

    private static final String ENTITY_NAME = "fxRate";

    private final FxRateService fxRateService;
    private final FxRateQueryService fxRateQueryService;

    public FxRateResource(FxRateService fxRateService, FxRateQueryService fxRateQueryService) {
        this.fxRateService = fxRateService;
        this.fxRateQueryService = fxRateQueryService;
    }

    /**
     * POST  /fx-rates : Create a new fxRate.
     *
     * @param fxRateDTO the fxRateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fxRateDTO, or with status 400 (Bad Request) if the fxRate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fx-rates")
    @Timed
    public ResponseEntity<FxRateDTO> createFxRate(@RequestBody FxRateDTO fxRateDTO) throws URISyntaxException {
        log.debug("REST request to save FxRate : {}", fxRateDTO);
        if (fxRateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fxRate cannot already have an ID")).body(null);
        }
        FxRateDTO result = fxRateService.save(fxRateDTO);
        return ResponseEntity.created(new URI("/api/fx-rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fx-rates : Updates an existing fxRate.
     *
     * @param fxRateDTO the fxRateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fxRateDTO,
     * or with status 400 (Bad Request) if the fxRateDTO is not valid,
     * or with status 500 (Internal Server Error) if the fxRateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fx-rates")
    @Timed
    public ResponseEntity<FxRateDTO> updateFxRate(@RequestBody FxRateDTO fxRateDTO) throws URISyntaxException {
        log.debug("REST request to update FxRate : {}", fxRateDTO);
        if (fxRateDTO.getId() == null) {
            return createFxRate(fxRateDTO);
        }
        FxRateDTO result = fxRateService.save(fxRateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fxRateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fx-rates : get all the fxRates.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of fxRates in body
     */
    @GetMapping("/fx-rates")
    @Timed
    public ResponseEntity<List<FxRateDTO>> getAllFxRates(FxRateCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get FxRates by criteria: {}", criteria);
        Page<FxRateDTO> page = fxRateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fx-rates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fx-rates/:id : get the "id" fxRate.
     *
     * @param id the id of the fxRateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fxRateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/fx-rates/{id}")
    @Timed
    public ResponseEntity<FxRateDTO> getFxRate(@PathVariable Long id) {
        log.debug("REST request to get FxRate : {}", id);
        FxRateDTO fxRateDTO = fxRateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fxRateDTO));
    }

    /**
     * DELETE  /fx-rates/:id : delete the "id" fxRate.
     *
     * @param id the id of the fxRateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fx-rates/{id}")
    @Timed
    public ResponseEntity<Void> deleteFxRate(@PathVariable Long id) {
        log.debug("REST request to delete FxRate : {}", id);
        fxRateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
