package com.enginemobi.fx.service.dto;


import java.time.Instant;
import java.time.LocalDate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the FxRate entity.
 */
public class FxRateDTO implements Serializable {

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

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getExtractTime() {
        return extractTime;
    }

    public void setExtractTime(Instant extractTime) {
        this.extractTime = extractTime;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
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

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimal getMidPrice() {
        return midPrice;
    }

    public void setMidPrice(BigDecimal midPrice) {
        this.midPrice = midPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FxRateDTO fxRateDTO = (FxRateDTO) o;
        if(fxRateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fxRateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FxRateDTO{" +
            "id=" + getId() +
            ", extractTime='" + getExtractTime() + "'" +
            ", arrivalDate='" + getArrivalDate() + "'" +
            ", currencyQuote='" + getCurrencyQuote() + "'" +
            ", currencyBaseCode='" + getCurrencyBaseCode() + "'" +
            ", currencyNonBaseCode='" + getCurrencyNonBaseCode() + "'" +
            ", bidPrice='" + getBidPrice() + "'" +
            ", askPrice='" + getAskPrice() + "'" +
            ", midPrice='" + getMidPrice() + "'" +
            "}";
    }
}
