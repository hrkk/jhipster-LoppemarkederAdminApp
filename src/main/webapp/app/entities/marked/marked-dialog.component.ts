import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Marked } from './marked.model';
import { MarkedPopupService } from './marked-popup.service';
import { MarkedService } from './marked.service';
import { Organizer, OrganizerService } from '../organizer';

@Component({
    selector: 'jhi-marked-dialog',
    templateUrl: './marked-dialog.component.html'
})
export class MarkedDialogComponent implements OnInit {

    marked: Marked;
    isSaving: boolean;

    organizers: Organizer[];
    fromDateDp: any;
    toDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private markedService: MarkedService,
        private organizerService: OrganizerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.organizerService.query()
            .subscribe((res: HttpResponse<Organizer[]>) => { this.organizers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.marked.id !== undefined) {
            this.subscribeToSaveResponse(
                this.markedService.update(this.marked));
        } else {
            this.subscribeToSaveResponse(
                this.markedService.create(this.marked));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Marked>>) {
        result.subscribe((res: HttpResponse<Marked>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Marked) {
        this.eventManager.broadcast({ name: 'markedListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackOrganizerById(index: number, item: Organizer) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-marked-popup',
    template: ''
})
export class MarkedPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private markedPopupService: MarkedPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.markedPopupService
                    .open(MarkedDialogComponent as Component, params['id']);
            } else {
                this.markedPopupService
                    .open(MarkedDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
