import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { FxRate } from './fx-rate.model';
import { FxRateService } from './fx-rate.service';

@Component({
    selector: 'jhi-fx-rate-detail',
    templateUrl: './fx-rate-detail.component.html'
})
export class FxRateDetailComponent implements OnInit, OnDestroy {

    fxRate: FxRate;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private fxRateService: FxRateService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFxRates();
    }

    load(id) {
        this.fxRateService.find(id).subscribe((fxRate) => {
            this.fxRate = fxRate;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFxRates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'fxRateListModification',
            (response) => this.load(this.fxRate.id)
        );
    }
}
