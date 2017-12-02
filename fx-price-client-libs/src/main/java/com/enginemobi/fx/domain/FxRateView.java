package com.enginemobi.fx.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


/**
 * A FxRateView.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FxRateView implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Instant extractTime;

    private LocalDate arrivalDate;

    private String currencyQuote;

    private String currencyBaseCode;

    private String currencyNonBaseCode;

    private BigDecimal bidPrice;

    private BigDecimal askPrice;

    private BigDecimal midPrice;


    public Long getId() {
        return id;
    }

    public Instant getExtractTime() {
        return extractTime;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public String getCurrencyQuote() {
        return currencyQuote;
    }

    public String getCurrencyBaseCode() {
        return currencyBaseCode;
    }

    public String getCurrencyNonBaseCode() {
        return currencyNonBaseCode;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public BigDecimal getMidPrice() {
        return midPrice;
    }

}
