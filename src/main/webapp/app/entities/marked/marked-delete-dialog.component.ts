import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Marked } from './marked.model';
import { MarkedPopupService } from './marked-popup.service';
import { MarkedService } from './marked.service';

@Component({
    selector: 'jhi-marked-delete-dialog',
    templateUrl: './marked-delete-dialog.component.html'
})
export class MarkedDeleteDialogComponent {

    marked: Marked;

    constructor(
        private markedService: MarkedService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.markedService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'markedListModification',
                content: 'Deleted an marked'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Marked is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-marked-delete-popup',
    template: ''
})
export class MarkedDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private markedPopupService: MarkedPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.markedPopupService
                .open(MarkedDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
