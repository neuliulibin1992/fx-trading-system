import { BaseEntity } from './../../shared';

export class FxRate implements BaseEntity {
    constructor(
        public id?: number,
        public extractTime?: any,
        public arrivalDate?: any,
        public currencyQuote?: string,
        public currencyBaseCode?: string,
        public currencyNonBaseCode?: string,
        public bidPrice?: number,
        public askPrice?: number,
        public midPrice?: number,
    ) {
    }
}
