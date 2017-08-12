import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Organizer } from './organizer.model';
import { OrganizerPopupService } from './organizer-popup.service';
import { OrganizerService } from './organizer.service';

@Component({
    selector: 'jhi-organizer-dialog',
    templateUrl: './organizer-dialog.component.html'
})
export class OrganizerDialogComponent implements OnInit {

    organizer: Organizer;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private organizerService: OrganizerService,
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
        if (this.organizer.id !== undefined) {
            this.subscribeToSaveResponse(
                this.organizerService.update(this.organizer), false);
        } else {
            this.subscribeToSaveResponse(
                this.organizerService.create(this.organizer), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Organizer>, isCreated: boolean) {
        result.subscribe((res: Organizer) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Organizer, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Organizer is created with identifier ${result.id}`
            : `A Organizer is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'organizerListModification', content: 'OK'});
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
    selector: 'jhi-organizer-popup',
    template: ''
})
export class OrganizerPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private organizerPopupService: OrganizerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.organizerPopupService
                    .open(OrganizerDialogComponent, params['id']);
            } else {
                this.modalRef = this.organizerPopupService
                    .open(OrganizerDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
