import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FxRate } from './fx-rate.model';
import { FxRatePopupService } from './fx-rate-popup.service';
import { FxRateService } from './fx-rate.service';

@Component({
    selector: 'jhi-fx-rate-dialog',
    templateUrl: './fx-rate-dialog.component.html'
})
export class FxRateDialogComponent implements OnInit {

    fxRate: FxRate;
    isSaving: boolean;
    arrivalDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private fxRateService: FxRateService,
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
        if (this.fxRate.id !== undefined) {
            this.subscribeToSaveResponse(
                this.fxRateService.update(this.fxRate));
        } else {
            this.subscribeToSaveResponse(
                this.fxRateService.create(this.fxRate));
        }
    }

    private subscribeToSaveResponse(result: Observable<FxRate>) {
        result.subscribe((res: FxRate) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: FxRate) {
        this.eventManager.broadcast({ name: 'fxRateListModification', content: 'OK'});
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
    selector: 'jhi-fx-rate-popup',
    template: ''
})
export class FxRatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private fxRatePopupService: FxRatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.fxRatePopupService
                    .open(FxRateDialogComponent as Component, params['id']);
            } else {
                this.fxRatePopupService
                    .open(FxRateDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
