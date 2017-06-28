import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { MarkedItem } from './marked-item.model';
import { MarkedItemService } from './marked-item.service';

@Component({
    selector: 'jhi-marked-item-detail',
    templateUrl: './marked-item-detail.component.html'
})
export class MarkedItemDetailComponent implements OnInit, OnDestroy {

    markedItem: MarkedItem;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private markedItemService: MarkedItemService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMarkedItems();
    }

    load(id) {
        this.markedItemService.find(id).subscribe((markedItem) => {
            this.markedItem = markedItem;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMarkedItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'markedItemListModification',
            (response) => this.load(this.markedItem.id)
        );
    }
}
