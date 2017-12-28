package com.enginemobi.fx.service.dto;

import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;






/**
 * Criteria class for the CurrencyMap entity. This class is used in CurrencyMapResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /currency-maps?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CurrencyMapCriteria implements Serializable {
    /**
     * Class for filtering CurrencyRateProvider
     */
    public static class CurrencyRateProviderFilter extends Filter<CurrencyRateProvider> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter currencyQuote;

    private StringFilter currencyBaseCode;

    private StringFilter currencyNonBaseCode;

    private CurrencyRateProviderFilter rateProvider;

    public CurrencyMapCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCurrencyQuote() {
        return currencyQuote;
    }

    public void setCurrencyQuote(StringFilter currencyQuote) {
        this.currencyQuote = currencyQuote;
    }

    public StringFilter getCurrencyBaseCode() {
        return currencyBaseCode;
    }

    public void setCurrencyBaseCode(StringFilter currencyBaseCode) {
        this.currencyBaseCode = currencyBaseCode;
    }

    public StringFilter getCurrencyNonBaseCode() {
        return currencyNonBaseCode;
    }

    public void setCurrencyNonBaseCode(StringFilter currencyNonBaseCode) {
        this.currencyNonBaseCode = currencyNonBaseCode;
    }

    public CurrencyRateProviderFilter getRateProvider() {
        return rateProvider;
    }

    public void setRateProvider(CurrencyRateProviderFilter rateProvider) {
        this.rateProvider = rateProvider;
    }

    @Override
    public String toString() {
        return "CurrencyMapCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (currencyQuote != null ? "currencyQuote=" + currencyQuote + ", " : "") +
                (currencyBaseCode != null ? "currencyBaseCode=" + currencyBaseCode + ", " : "") +
                (currencyNonBaseCode != null ? "currencyNonBaseCode=" + currencyNonBaseCode + ", " : "") +
                (rateProvider != null ? "rateProvider=" + rateProvider + ", " : "") +
            "}";
    }

}
