import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { FxRateComponent } from './fx-rate.component';
import { FxRateDetailComponent } from './fx-rate-detail.component';
import { FxRatePopupComponent } from './fx-rate-dialog.component';
import { FxRateDeletePopupComponent } from './fx-rate-delete-dialog.component';

@Injectable()
export class FxRateResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const fxRateRoute: Routes = [
    {
        path: 'fx-rate',
        component: FxRateComponent,
        resolve: {
            'pagingParams': FxRateResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.fxRate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'fx-rate/:id',
        component: FxRateDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.fxRate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fxRatePopupRoute: Routes = [
    {
        path: 'fx-rate-new',
        component: FxRatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.fxRate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'fx-rate/:id/edit',
        component: FxRatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.fxRate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'fx-rate/:id/delete',
        component: FxRateDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.fxRate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
