import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FxgatewaySharedModule } from '../../shared';
import {
    CurrencyMapService,
    CurrencyMapPopupService,
    CurrencyMapComponent,
    CurrencyMapDetailComponent,
    CurrencyMapDialogComponent,
    CurrencyMapPopupComponent,
    CurrencyMapDeletePopupComponent,
    CurrencyMapDeleteDialogComponent,
    currencyMapRoute,
    currencyMapPopupRoute,
    CurrencyMapResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...currencyMapRoute,
    ...currencyMapPopupRoute,
];

@NgModule({
    imports: [
        FxgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CurrencyMapComponent,
        CurrencyMapDetailComponent,
        CurrencyMapDialogComponent,
        CurrencyMapDeleteDialogComponent,
        CurrencyMapPopupComponent,
        CurrencyMapDeletePopupComponent,
    ],
    entryComponents: [
        CurrencyMapComponent,
        CurrencyMapDialogComponent,
        CurrencyMapPopupComponent,
        CurrencyMapDeleteDialogComponent,
        CurrencyMapDeletePopupComponent,
    ],
    providers: [
        CurrencyMapService,
        CurrencyMapPopupService,
        CurrencyMapResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FxgatewayCurrencyMapModule {}
