import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { DateInterval } from './date-interval.model';
import { DateIntervalService } from './date-interval.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-date-interval',
    templateUrl: './date-interval.component.html'
})
export class DateIntervalComponent implements OnInit, OnDestroy {
dateIntervals: DateInterval[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private dateIntervalService: DateIntervalService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.dateIntervalService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.dateIntervals = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.dateIntervalService.query().subscribe(
            (res: ResponseWrapper) => {
                this.dateIntervals = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInDateIntervals();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: DateInterval) {
        return item.id;
    }
    registerChangeInDateIntervals() {
        this.eventSubscriber = this.eventManager.subscribe('dateIntervalListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
