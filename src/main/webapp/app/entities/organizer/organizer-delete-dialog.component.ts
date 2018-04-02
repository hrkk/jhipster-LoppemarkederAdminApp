import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Organizer } from './organizer.model';
import { OrganizerPopupService } from './organizer-popup.service';
import { OrganizerService } from './organizer.service';

@Component({
    selector: 'jhi-organizer-delete-dialog',
    templateUrl: './organizer-delete-dialog.component.html'
})
export class OrganizerDeleteDialogComponent {

    organizer: Organizer;

    constructor(
        private organizerService: OrganizerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.organizerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'organizerListModification',
                content: 'Deleted an organizer'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-organizer-delete-popup',
    template: ''
})
export class OrganizerDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private organizerPopupService: OrganizerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.organizerPopupService
                .open(OrganizerDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
