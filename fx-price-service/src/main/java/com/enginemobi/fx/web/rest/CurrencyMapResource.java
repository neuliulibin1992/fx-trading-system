package com.enginemobi.fx.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enginemobi.fx.service.CurrencyMapService;
import com.enginemobi.fx.web.rest.errors.BadRequestAlertException;
import com.enginemobi.fx.web.rest.util.HeaderUtil;
import com.enginemobi.fx.web.rest.util.PaginationUtil;
import com.enginemobi.fx.service.dto.CurrencyMapDTO;
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
 * REST controller for managing CurrencyMap.
 */
@RestController
@RequestMapping("/api")
public class CurrencyMapResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyMapResource.class);

    private static final String ENTITY_NAME = "currencyMap";

    private final CurrencyMapService currencyMapService;

    public CurrencyMapResource(CurrencyMapService currencyMapService) {
        this.currencyMapService = currencyMapService;
    }

    /**
     * POST  /currency-maps : Create a new currencyMap.
     *
     * @param currencyMapDTO the currencyMapDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new currencyMapDTO, or with status 400 (Bad Request) if the currencyMap has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/currency-maps")
    @Timed
    public ResponseEntity<CurrencyMapDTO> createCurrencyMap(@RequestBody CurrencyMapDTO currencyMapDTO) throws URISyntaxException {
        log.debug("REST request to save CurrencyMap : {}", currencyMapDTO);
        if (currencyMapDTO.getId() != null) {
            throw new BadRequestAlertException("A new currencyMap cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyMapDTO result = currencyMapService.save(currencyMapDTO);
        return ResponseEntity.created(new URI("/api/currency-maps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /currency-maps : Updates an existing currencyMap.
     *
     * @param currencyMapDTO the currencyMapDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated currencyMapDTO,
     * or with status 400 (Bad Request) if the currencyMapDTO is not valid,
     * or with status 500 (Internal Server Error) if the currencyMapDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/currency-maps")
    @Timed
    public ResponseEntity<CurrencyMapDTO> updateCurrencyMap(@RequestBody CurrencyMapDTO currencyMapDTO) throws URISyntaxException {
        log.debug("REST request to update CurrencyMap : {}", currencyMapDTO);
        if (currencyMapDTO.getId() == null) {
            return createCurrencyMap(currencyMapDTO);
        }
        CurrencyMapDTO result = currencyMapService.save(currencyMapDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, currencyMapDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /currency-maps : get all the currencyMaps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of currencyMaps in body
     */
    @GetMapping("/currency-maps")
    @Timed
    public ResponseEntity<List<CurrencyMapDTO>> getAllCurrencyMaps(Pageable pageable) {
        log.debug("REST request to get a page of CurrencyMaps");
        Page<CurrencyMapDTO> page = currencyMapService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currency-maps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /currency-maps/:id : get the "id" currencyMap.
     *
     * @param id the id of the currencyMapDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the currencyMapDTO, or with status 404 (Not Found)
     */
    @GetMapping("/currency-maps/{id}")
    @Timed
    public ResponseEntity<CurrencyMapDTO> getCurrencyMap(@PathVariable Long id) {
        log.debug("REST request to get CurrencyMap : {}", id);
        CurrencyMapDTO currencyMapDTO = currencyMapService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(currencyMapDTO));
    }

    /**
     * DELETE  /currency-maps/:id : delete the "id" currencyMap.
     *
     * @param id the id of the currencyMapDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/currency-maps/{id}")
    @Timed
    public ResponseEntity<Void> deleteCurrencyMap(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyMap : {}", id);
        currencyMapService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
