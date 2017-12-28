package com.enginemobi.fx.service.dto;

import io.github.jhipster.service.filter.*;

import java.io.Serializable;



/**
 * Criteria class for the FxRate entity. This class is used in FxRateResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /fx-rates?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FxRateCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private InstantFilter extractTime;

    private LocalDateFilter arrivalDate;

    private StringFilter currencyQuote;

    private StringFilter currencyBaseCode;

    private StringFilter currencyNonBaseCode;

    private BigDecimalFilter bidPrice;

    private BigDecimalFilter askPrice;

    private BigDecimalFilter midPrice;

    public FxRateCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getExtractTime() {
        return extractTime;
    }

    public void setExtractTime(InstantFilter extractTime) {
        this.extractTime = extractTime;
    }

    public LocalDateFilter getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateFilter arrivalDate) {
        this.arrivalDate = arrivalDate;
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

    public BigDecimalFilter getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimalFilter bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimalFilter getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimalFilter askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimalFilter getMidPrice() {
        return midPrice;
    }

    public void setMidPrice(BigDecimalFilter midPrice) {
        this.midPrice = midPrice;
    }

    @Override
    public String toString() {
        return "FxRateCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (extractTime != null ? "extractTime=" + extractTime + ", " : "") +
                (arrivalDate != null ? "arrivalDate=" + arrivalDate + ", " : "") +
                (currencyQuote != null ? "currencyQuote=" + currencyQuote + ", " : "") +
                (currencyBaseCode != null ? "currencyBaseCode=" + currencyBaseCode + ", " : "") +
                (currencyNonBaseCode != null ? "currencyNonBaseCode=" + currencyNonBaseCode + ", " : "") +
                (bidPrice != null ? "bidPrice=" + bidPrice + ", " : "") +
                (askPrice != null ? "askPrice=" + askPrice + ", " : "") +
                (midPrice != null ? "midPrice=" + midPrice + ", " : "") +
            "}";
    }

}
