import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { CurrencyMap } from './currency-map.model';
import { CurrencyMapService } from './currency-map.service';

@Component({
    selector: 'jhi-currency-map-detail',
    templateUrl: './currency-map-detail.component.html'
})
export class CurrencyMapDetailComponent implements OnInit, OnDestroy {

    currencyMap: CurrencyMap;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private currencyMapService: CurrencyMapService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCurrencyMaps();
    }

    load(id) {
        this.currencyMapService.find(id).subscribe((currencyMap) => {
            this.currencyMap = currencyMap;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCurrencyMaps() {
        this.eventSubscriber = this.eventManager.subscribe(
            'currencyMapListModification',
            (response) => this.load(this.currencyMap.id)
        );
    }
}
