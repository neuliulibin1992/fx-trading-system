import { BaseEntity } from './../../shared';

const enum CurrencyRateProvider {
    'ONE_FORGE',
    'CURRENCY_LAYER'
}

export class CurrencyMap implements BaseEntity {
    constructor(
        public id?: number,
        public currencyQuote?: string,
        public currencyBaseCode?: string,
        public currencyNonBaseCode?: string,
        public rateProvider?: CurrencyRateProvider,
    ) {
    }
}
