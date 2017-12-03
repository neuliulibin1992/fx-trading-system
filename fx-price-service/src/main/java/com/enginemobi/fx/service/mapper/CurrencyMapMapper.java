package com.enginemobi.fx.service.mapper;

import com.enginemobi.fx.domain.*;
import com.enginemobi.fx.service.dto.CurrencyMapDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CurrencyMap and its DTO CurrencyMapDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurrencyMapMapper extends EntityMapper<CurrencyMapDTO, CurrencyMap> {

    

    

    default CurrencyMap fromId(Long id) {
        if (id == null) {
            return null;
        }
        CurrencyMap currencyMap = new CurrencyMap();
        currencyMap.setId(id);
        return currencyMap;
    }
}
