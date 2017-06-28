import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { MarkedItem } from './marked-item.model';
import { MarkedItemPopupService } from './marked-item-popup.service';
import { MarkedItemService } from './marked-item.service';

@Component({
    selector: 'jhi-marked-item-delete-dialog',
    templateUrl: './marked-item-delete-dialog.component.html'
})
export class MarkedItemDeleteDialogComponent {

    markedItem: MarkedItem;

    constructor(
        private markedItemService: MarkedItemService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.markedItemService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'markedItemListModification',
                content: 'Deleted an markedItem'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Marked Item is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-marked-item-delete-popup',
    template: ''
})
export class MarkedItemDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private markedItemPopupService: MarkedItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.markedItemPopupService
                .open(MarkedItemDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
