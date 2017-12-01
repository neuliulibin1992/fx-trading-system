package com.enginemobi.fx.service.mapper;

import com.enginemobi.fx.domain.*;
import com.enginemobi.fx.service.dto.FxRateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FxRate and its DTO FxRateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FxRateMapper extends EntityMapper<FxRateDTO, FxRate> {

    

    

    default FxRate fromId(Long id) {
        if (id == null) {
            return null;
        }
        FxRate fxRate = new FxRate();
        fxRate.setId(id);
        return fxRate;
    }
}
