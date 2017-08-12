import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { DateInterval } from './date-interval.model';
import { DateIntervalPopupService } from './date-interval-popup.service';
import { DateIntervalService } from './date-interval.service';

@Component({
    selector: 'jhi-date-interval-delete-dialog',
    templateUrl: './date-interval-delete-dialog.component.html'
})
export class DateIntervalDeleteDialogComponent {

    dateInterval: DateInterval;

    constructor(
        private dateIntervalService: DateIntervalService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dateIntervalService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'dateIntervalListModification',
                content: 'Deleted an dateInterval'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Date Interval is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-date-interval-delete-popup',
    template: ''
})
export class DateIntervalDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dateIntervalPopupService: DateIntervalPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.dateIntervalPopupService
                .open(DateIntervalDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
