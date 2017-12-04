import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { FxRate } from './fx-rate.model';
import { FxRateService } from './fx-rate.service';

@Injectable()
export class FxRatePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private fxRateService: FxRateService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.fxRateService.find(id).subscribe((fxRate) => {
                    fxRate.extractTime = this.datePipe
                        .transform(fxRate.extractTime, 'yyyy-MM-ddTHH:mm:ss');
                    if (fxRate.arrivalDate) {
                        fxRate.arrivalDate = {
                            year: fxRate.arrivalDate.getFullYear(),
                            month: fxRate.arrivalDate.getMonth() + 1,
                            day: fxRate.arrivalDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.fxRateModalRef(component, fxRate);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.fxRateModalRef(component, new FxRate());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    fxRateModalRef(component: Component, fxRate: FxRate): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.fxRate = fxRate;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
