import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CurrencyMapComponent } from './currency-map.component';
import { CurrencyMapDetailComponent } from './currency-map-detail.component';
import { CurrencyMapPopupComponent } from './currency-map-dialog.component';
import { CurrencyMapDeletePopupComponent } from './currency-map-delete-dialog.component';

@Injectable()
export class CurrencyMapResolvePagingParams implements Resolve<any> {

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

export const currencyMapRoute: Routes = [
    {
        path: 'currency-map',
        component: CurrencyMapComponent,
        resolve: {
            'pagingParams': CurrencyMapResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.currencyMap.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'currency-map/:id',
        component: CurrencyMapDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.currencyMap.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const currencyMapPopupRoute: Routes = [
    {
        path: 'currency-map-new',
        component: CurrencyMapPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.currencyMap.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'currency-map/:id/edit',
        component: CurrencyMapPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.currencyMap.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'currency-map/:id/delete',
        component: CurrencyMapDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fxgatewayApp.currencyMap.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
