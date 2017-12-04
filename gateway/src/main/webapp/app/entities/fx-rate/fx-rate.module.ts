import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FxgatewaySharedModule } from '../../shared';
import {
    FxRateService,
    FxRatePopupService,
    FxRateComponent,
    FxRateDetailComponent,
    FxRateDialogComponent,
    FxRatePopupComponent,
    FxRateDeletePopupComponent,
    FxRateDeleteDialogComponent,
    fxRateRoute,
    fxRatePopupRoute,
    FxRateResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...fxRateRoute,
    ...fxRatePopupRoute,
];

@NgModule({
    imports: [
        FxgatewaySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        FxRateComponent,
        FxRateDetailComponent,
        FxRateDialogComponent,
        FxRateDeleteDialogComponent,
        FxRatePopupComponent,
        FxRateDeletePopupComponent,
    ],
    entryComponents: [
        FxRateComponent,
        FxRateDialogComponent,
        FxRatePopupComponent,
        FxRateDeleteDialogComponent,
        FxRateDeletePopupComponent,
    ],
    providers: [
        FxRateService,
        FxRatePopupService,
        FxRateResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FxgatewayFxRateModule {}
