import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { DateInterval } from './date-interval.model';
import { DateIntervalService } from './date-interval.service';

@Component({
    selector: 'jhi-date-interval-detail',
    templateUrl: './date-interval-detail.component.html'
})
export class DateIntervalDetailComponent implements OnInit, OnDestroy {

    dateInterval: DateInterval;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private dateIntervalService: DateIntervalService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDateIntervals();
    }

    load(id) {
        this.dateIntervalService.find(id).subscribe((dateInterval) => {
            this.dateInterval = dateInterval;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDateIntervals() {
        this.eventSubscriber = this.eventManager.subscribe(
            'dateIntervalListModification',
            (response) => this.load(this.dateInterval.id)
        );
    }
}
