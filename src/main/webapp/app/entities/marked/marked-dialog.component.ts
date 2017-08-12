import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Marked } from './marked.model';
import { MarkedPopupService } from './marked-popup.service';
import { MarkedService } from './marked.service';

@Component({
    selector: 'jhi-marked-dialog',
    templateUrl: './marked-dialog.component.html'
})
export class MarkedDialogComponent implements OnInit {

    marked: Marked;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private markedService: MarkedService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.marked.id !== undefined) {
            this.subscribeToSaveResponse(
                this.markedService.update(this.marked), false);
        } else {
            this.subscribeToSaveResponse(
                this.markedService.create(this.marked), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Marked>, isCreated: boolean) {
        result.subscribe((res: Marked) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Marked, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Marked is created with identifier ${result.id}`
            : `A Marked is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'markedListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-marked-popup',
    template: ''
})
export class MarkedPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private markedPopupService: MarkedPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.markedPopupService
                    .open(MarkedDialogComponent, params['id']);
            } else {
                this.modalRef = this.markedPopupService
                    .open(MarkedDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
