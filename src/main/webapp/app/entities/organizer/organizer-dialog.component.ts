import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Organizer } from './organizer.model';
import { OrganizerPopupService } from './organizer-popup.service';
import { OrganizerService } from './organizer.service';

@Component({
    selector: 'jhi-organizer-dialog',
    templateUrl: './organizer-dialog.component.html'
})
export class OrganizerDialogComponent implements OnInit {

    organizer: Organizer;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private organizerService: OrganizerService,
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
        if (this.organizer.id !== undefined) {
            this.subscribeToSaveResponse(
                this.organizerService.update(this.organizer));
        } else {
            this.subscribeToSaveResponse(
                this.organizerService.create(this.organizer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Organizer>>) {
        result.subscribe((res: HttpResponse<Organizer>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Organizer) {
        this.eventManager.broadcast({ name: 'organizerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-organizer-popup',
    template: ''
})
export class OrganizerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private organizerPopupService: OrganizerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.organizerPopupService
                    .open(OrganizerDialogComponent as Component, params['id']);
            } else {
                this.organizerPopupService
                    .open(OrganizerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
