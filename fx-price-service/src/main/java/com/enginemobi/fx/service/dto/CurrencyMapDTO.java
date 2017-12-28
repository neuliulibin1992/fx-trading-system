package com.enginemobi.fx.service.dto;


import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CurrencyMap entity.
 */
public class CurrencyMapDTO implements Serializable {

    private Long id;

    private String currencyQuote;

    private String currencyBaseCode;

    private String currencyNonBaseCode;

    private CurrencyRateProvider rateProvider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyQuote() {
        return currencyQuote;
    }

    public void setCurrencyQuote(String currencyQuote) {
        this.currencyQuote = currencyQuote;
    }

    public String getCurrencyBaseCode() {
        return currencyBaseCode;
    }

    public void setCurrencyBaseCode(String currencyBaseCode) {
        this.currencyBaseCode = currencyBaseCode;
    }

    public String getCurrencyNonBaseCode() {
        return currencyNonBaseCode;
    }

    public void setCurrencyNonBaseCode(String currencyNonBaseCode) {
        this.currencyNonBaseCode = currencyNonBaseCode;
    }

    public CurrencyRateProvider getRateProvider() {
        return rateProvider;
    }

    public void setRateProvider(CurrencyRateProvider rateProvider) {
        this.rateProvider = rateProvider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrencyMapDTO currencyMapDTO = (CurrencyMapDTO) o;
        if(currencyMapDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyMapDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyMapDTO{" +
            "id=" + getId() +
            ", currencyQuote='" + getCurrencyQuote() + "'" +
            ", currencyBaseCode='" + getCurrencyBaseCode() + "'" +
            ", currencyNonBaseCode='" + getCurrencyNonBaseCode() + "'" +
            ", rateProvider='" + getRateProvider() + "'" +
            "}";
    }
}
