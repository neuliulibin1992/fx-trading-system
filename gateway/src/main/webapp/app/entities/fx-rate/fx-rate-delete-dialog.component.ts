import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { FxRate } from './fx-rate.model';
import { FxRatePopupService } from './fx-rate-popup.service';
import { FxRateService } from './fx-rate.service';

@Component({
    selector: 'jhi-fx-rate-delete-dialog',
    templateUrl: './fx-rate-delete-dialog.component.html'
})
export class FxRateDeleteDialogComponent {

    fxRate: FxRate;

    constructor(
        private fxRateService: FxRateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.fxRateService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'fxRateListModification',
                content: 'Deleted an fxRate'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-fx-rate-delete-popup',
    template: ''
})
export class FxRateDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private fxRatePopupService: FxRatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.fxRatePopupService
                .open(FxRateDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
