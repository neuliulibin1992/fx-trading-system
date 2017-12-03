import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CurrencyMap } from './currency-map.model';
import { CurrencyMapPopupService } from './currency-map-popup.service';
import { CurrencyMapService } from './currency-map.service';

@Component({
    selector: 'jhi-currency-map-delete-dialog',
    templateUrl: './currency-map-delete-dialog.component.html'
})
export class CurrencyMapDeleteDialogComponent {

    currencyMap: CurrencyMap;

    constructor(
        private currencyMapService: CurrencyMapService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.currencyMapService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'currencyMapListModification',
                content: 'Deleted an currencyMap'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-currency-map-delete-popup',
    template: ''
})
export class CurrencyMapDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private currencyMapPopupService: CurrencyMapPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.currencyMapPopupService
                .open(CurrencyMapDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
