import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { MarkedItem } from './marked-item.model';
import { MarkedItemPopupService } from './marked-item-popup.service';
import { MarkedItemService } from './marked-item.service';

@Component({
    selector: 'jhi-marked-item-dialog',
    templateUrl: './marked-item-dialog.component.html'
})
export class MarkedItemDialogComponent implements OnInit {

    markedItem: MarkedItem;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private markedItemService: MarkedItemService,
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
        if (this.markedItem.id !== undefined) {
            this.subscribeToSaveResponse(
                this.markedItemService.update(this.markedItem), false);
        } else {
            this.subscribeToSaveResponse(
                this.markedItemService.create(this.markedItem), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<MarkedItem>, isCreated: boolean) {
        result.subscribe((res: MarkedItem) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: MarkedItem, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Marked Item is created with identifier ${result.id}`
            : `A Marked Item is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'markedItemListModification', content: 'OK'});
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
    selector: 'jhi-marked-item-popup',
    template: ''
})
export class MarkedItemPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private markedItemPopupService: MarkedItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.markedItemPopupService
                    .open(MarkedItemDialogComponent, params['id']);
            } else {
                this.modalRef = this.markedItemPopupService
                    .open(MarkedItemDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
