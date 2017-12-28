package com.enginemobi.fx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to FX price service.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final RateProvider rateProvider = new RateProvider();

    public RateProvider getRateProvider() {
        return rateProvider;
    }

    public static class RateProvider {

        private final OneForge oneForge = new OneForge();

        public OneForge getOneForge() {
            return oneForge;
        }

        public static class OneForge {

            private boolean enabled = true;
            private String baseUrl;
            private String requestUrl;
            private String apiKey;
            private long fixedDelay = 3600;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public String getRequestUrl() {
                return requestUrl;
            }

            public void setRequestUrl(String requestUrl) {
                this.requestUrl = requestUrl;
            }

            public String getApiKey() {
                return apiKey;
            }

            public void setApiKey(String apiKey) {
                this.apiKey = apiKey;
            }

            public long getFixedDelay() {
                return fixedDelay;
            }

            public void setFixedDelay(long fixedDelay) {
                this.fixedDelay = fixedDelay;
            }

        }


        // Currency Layer
        private final CurrencyLayer currencyLayer = new CurrencyLayer();

        public CurrencyLayer getCurrencyLayer() {
            return currencyLayer;
        }

        public static class CurrencyLayer {

            private boolean enabled = true;
            private String baseUrl;
            private long fixedDelay = 3600;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public long getFixedDelay() {
                return fixedDelay;
            }

            public void setFixedDelay(long fixedDelay) {
                this.fixedDelay = fixedDelay;
            }

        }
    }

}
