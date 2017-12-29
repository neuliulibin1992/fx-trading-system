package com.enginemobi.fx.prototype;

import com.enginemobi.fx.domain.CurrencyMap;
import com.enginemobi.fx.domain.enumeration.CurrencyRateProvider;
import com.neovisionaries.i18n.CurrencyCode;

import static com.neovisionaries.i18n.CurrencyCode.AUD;
import static com.neovisionaries.i18n.CurrencyCode.EUR;
import static com.neovisionaries.i18n.CurrencyCode.USD;

public class CurrencyMapData {
    public static CurrencyMap createAUDUSD() {
        return new CurrencyMap()
            .currencyQuote(AUD.name().concat(USD.name()))
            .currencyBaseCode(AUD.name())
            .currencyNonBaseCode(USD.name())
            .rateProvider(CurrencyRateProvider.ONE_FORGE);
    }

    public static CurrencyMap createEURUSD() {
        return new CurrencyMap()
            .currencyQuote(EUR.name().concat(USD.name()))
            .currencyBaseCode(EUR.name())
            .currencyNonBaseCode(USD.name())
            .rateProvider(CurrencyRateProvider.ONE_FORGE);
    }

    public static CurrencyMap createCurrencyMap(CurrencyCode baseCode, CurrencyCode nonbaseCode) {
        return new CurrencyMap()
            .currencyQuote(baseCode.name().concat(nonbaseCode.name()))
            .currencyBaseCode(baseCode.name())
            .currencyNonBaseCode(nonbaseCode.name())
            .rateProvider(CurrencyRateProvider.ONE_FORGE);
    }
}
