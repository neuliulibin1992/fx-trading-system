package com.enginemobi.fx.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;


/**
 * A FxRate.
 */
@Entity
@Table(name = "fx_rate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FxRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "extract_time")
    private Instant extractTime;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "currency_quote")
    private String currencyQuote;

    @Column(name = "currency_base_code")
    private String currencyBaseCode;

    @Column(name = "currency_non_base_code")
    private String currencyNonBaseCode;

    @Column(name = "bid_price", precision=10, scale=2)
    private BigDecimal bidPrice;

    @Column(name = "ask_price", precision=10, scale=2)
    private BigDecimal askPrice;

    @Column(name = "mid_price", precision=10, scale=2)
    private BigDecimal midPrice;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getExtractTime() {
        return extractTime;
    }

    public FxRate extractTime(Instant extractTime) {
        this.extractTime = extractTime;
        return this;
    }

    public void setExtractTime(Instant extractTime) {
        this.extractTime = extractTime;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public FxRate arrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
        return this;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getCurrencyQuote() {
        return currencyQuote;
    }

    public FxRate currencyQuote(String currencyQuote) {
        this.currencyQuote = currencyQuote;
        return this;
    }

    public void setCurrencyQuote(String currencyQuote) {
        this.currencyQuote = currencyQuote;
    }

    public String getCurrencyBaseCode() {
        return currencyBaseCode;
    }

    public FxRate currencyBaseCode(String currencyBaseCode) {
        this.currencyBaseCode = currencyBaseCode;
        return this;
    }

    public void setCurrencyBaseCode(String currencyBaseCode) {
        this.currencyBaseCode = currencyBaseCode;
    }

    public String getCurrencyNonBaseCode() {
        return currencyNonBaseCode;
    }

    public FxRate currencyNonBaseCode(String currencyNonBaseCode) {
        this.currencyNonBaseCode = currencyNonBaseCode;
        return this;
    }

    public void setCurrencyNonBaseCode(String currencyNonBaseCode) {
        this.currencyNonBaseCode = currencyNonBaseCode;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public FxRate bidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
        return this;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public FxRate askPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
        return this;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimal getMidPrice() {
        return midPrice;
    }

    public FxRate midPrice(BigDecimal midPrice) {
        this.midPrice = midPrice;
        return this;
    }

    public void setMidPrice(BigDecimal midPrice) {
        this.midPrice = midPrice;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FxRate fxRate = (FxRate) o;
        if (fxRate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fxRate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FxRate{" +
            "id=" + getId() +
            ", extractTime='" + getExtractTime() + "'" +
            ", arrivalDate='" + getArrivalDate() + "'" +
            ", currencyQuote='" + getCurrencyQuote() + "'" +
            ", currencyBaseCode='" + getCurrencyBaseCode() + "'" +
            ", currencyNonBaseCode='" + getCurrencyNonBaseCode() + "'" +
            ", bidPrice=" + getBidPrice() +
            ", askPrice=" + getAskPrice() +
            ", midPrice=" + getMidPrice() +
            "}";
    }
}
