import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CurrencyMap } from './currency-map.model';
import { CurrencyMapPopupService } from './currency-map-popup.service';
import { CurrencyMapService } from './currency-map.service';

@Component({
    selector: 'jhi-currency-map-dialog',
    templateUrl: './currency-map-dialog.component.html'
})
export class CurrencyMapDialogComponent implements OnInit {

    currencyMap: CurrencyMap;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private currencyMapService: CurrencyMapService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.currencyMap.id !== undefined) {
            this.subscribeToSaveResponse(
                this.currencyMapService.update(this.currencyMap));
        } else {
            this.subscribeToSaveResponse(
                this.currencyMapService.create(this.currencyMap));
        }
    }

    private subscribeToSaveResponse(result: Observable<CurrencyMap>) {
        result.subscribe((res: CurrencyMap) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CurrencyMap) {
        this.eventManager.broadcast({ name: 'currencyMapListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-currency-map-popup',
    template: ''
})
export class CurrencyMapPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private currencyMapPopupService: CurrencyMapPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.currencyMapPopupService
                    .open(CurrencyMapDialogComponent as Component, params['id']);
            } else {
                this.currencyMapPopupService
                    .open(CurrencyMapDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
