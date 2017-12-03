package com.enginemobi.fx.service;

/**
 * Service Interface for refreshing fx rates
 */
public interface FxRateProvider {

    /**
     * Fetch new fx rates
     */
    void fetchFxRates();

    /**
     * Update supported currencies by the provider
     */
    void updateSupportedCurrencies();
}
