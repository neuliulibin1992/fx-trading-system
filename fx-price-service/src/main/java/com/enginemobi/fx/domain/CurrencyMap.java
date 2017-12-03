package com.enginemobi.fx.domain;

import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A CurrencyMap.
 */
@Entity
@Table(name = "currency_map")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CurrencyMap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "currency_quote")
    private String currencyQuote;

    @Column(name = "currency_base_code")
    private String currencyBaseCode;

    @Column(name = "currency_non_base_code")
    private String currencyNonBaseCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "provided_by")
    private CurrencyRateProvider providedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyQuote() {
        return currencyQuote;
    }

    public CurrencyMap currencyQuote(String currencyQuote) {
        this.currencyQuote = currencyQuote;
        return this;
    }

    public void setCurrencyQuote(String currencyQuote) {
        this.currencyQuote = currencyQuote;
    }

    public String getCurrencyBaseCode() {
        return currencyBaseCode;
    }

    public CurrencyMap currencyBaseCode(String currencyBaseCode) {
        this.currencyBaseCode = currencyBaseCode;
        return this;
    }

    public void setCurrencyBaseCode(String currencyBaseCode) {
        this.currencyBaseCode = currencyBaseCode;
    }

    public String getCurrencyNonBaseCode() {
        return currencyNonBaseCode;
    }

    public CurrencyMap currencyNonBaseCode(String currencyNonBaseCode) {
        this.currencyNonBaseCode = currencyNonBaseCode;
        return this;
    }

    public void setCurrencyNonBaseCode(String currencyNonBaseCode) {
        this.currencyNonBaseCode = currencyNonBaseCode;
    }

    public CurrencyRateProvider getProvidedBy() {
        return providedBy;
    }

    public CurrencyMap providedBy(CurrencyRateProvider providedBy) {
        this.providedBy = providedBy;
        return this;
    }

    public void setProvidedBy(CurrencyRateProvider providedBy) {
        this.providedBy = providedBy;
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
        CurrencyMap currencyMap = (CurrencyMap) o;
        if (currencyMap.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyMap.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyMap{" +
            "id=" + getId() +
            ", currencyQuote='" + getCurrencyQuote() + "'" +
            ", currencyBaseCode='" + getCurrencyBaseCode() + "'" +
            ", currencyNonBaseCode='" + getCurrencyNonBaseCode() + "'" +
            ", providedBy='" + getProvidedBy() + "'" +
            "}";
    }
}
