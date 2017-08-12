import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Organizer } from './organizer.model';
import { OrganizerService } from './organizer.service';

@Component({
    selector: 'jhi-organizer-detail',
    templateUrl: './organizer-detail.component.html'
})
export class OrganizerDetailComponent implements OnInit, OnDestroy {

    organizer: Organizer;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private organizerService: OrganizerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOrganizers();
    }

    load(id) {
        this.organizerService.find(id).subscribe((organizer) => {
            this.organizer = organizer;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOrganizers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'organizerListModification',
            (response) => this.load(this.organizer.id)
        );
    }
}
