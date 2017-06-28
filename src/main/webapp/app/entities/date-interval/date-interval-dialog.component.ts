import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { DateInterval } from './date-interval.model';
import { DateIntervalPopupService } from './date-interval-popup.service';
import { DateIntervalService } from './date-interval.service';
import { MarkedItem, MarkedItemService } from '../marked-item';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-date-interval-dialog',
    templateUrl: './date-interval-dialog.component.html'
})
export class DateIntervalDialogComponent implements OnInit {

    dateInterval: DateInterval;
    authorities: any[];
    isSaving: boolean;

    markeditems: MarkedItem[];
    fromDateDp: any;
    toDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private dateIntervalService: DateIntervalService,
        private markedItemService: MarkedItemService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.markedItemService.query()
            .subscribe((res: ResponseWrapper) => { this.markeditems = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.dateInterval.id !== undefined) {
            this.subscribeToSaveResponse(
                this.dateIntervalService.update(this.dateInterval), false);
        } else {
            this.subscribeToSaveResponse(
                this.dateIntervalService.create(this.dateInterval), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<DateInterval>, isCreated: boolean) {
        result.subscribe((res: DateInterval) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: DateInterval, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Date Interval is created with identifier ${result.id}`
            : `A Date Interval is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'dateIntervalListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackMarkedItemById(index: number, item: MarkedItem) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-date-interval-popup',
    template: ''
})
export class DateIntervalPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dateIntervalPopupService: DateIntervalPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.dateIntervalPopupService
                    .open(DateIntervalDialogComponent, params['id']);
            } else {
                this.modalRef = this.dateIntervalPopupService
                    .open(DateIntervalDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
