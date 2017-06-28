import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { MarkedItem } from './marked-item.model';
import { MarkedItemService } from './marked-item.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-marked-item',
    templateUrl: './marked-item.component.html'
})
export class MarkedItemComponent implements OnInit, OnDestroy {
markedItems: MarkedItem[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private markedItemService: MarkedItemService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.markedItemService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.markedItems = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.markedItemService.query().subscribe(
            (res: ResponseWrapper) => {
                this.markedItems = res.json;
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
        this.registerChangeInMarkedItems();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: MarkedItem) {
        return item.id;
    }
    registerChangeInMarkedItems() {
        this.eventSubscriber = this.eventManager.subscribe('markedItemListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
