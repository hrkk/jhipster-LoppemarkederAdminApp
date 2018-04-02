import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Marked } from './marked.model';
import { MarkedService } from './marked.service';

@Component({
    selector: 'jhi-marked-detail',
    templateUrl: './marked-detail.component.html'
})
export class MarkedDetailComponent implements OnInit, OnDestroy {

    marked: Marked;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private markedService: MarkedService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMarkeds();
    }

    load(id) {
        this.markedService.find(id)
            .subscribe((markedResponse: HttpResponse<Marked>) => {
                this.marked = markedResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMarkeds() {
        this.eventSubscriber = this.eventManager.subscribe(
            'markedListModification',
            (response) => this.load(this.marked.id)
        );
    }
}
