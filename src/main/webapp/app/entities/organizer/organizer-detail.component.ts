import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

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
        private eventManager: JhiEventManager,
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
        this.organizerService.find(id)
            .subscribe((organizerResponse: HttpResponse<Organizer>) => {
                this.organizer = organizerResponse.body;
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
