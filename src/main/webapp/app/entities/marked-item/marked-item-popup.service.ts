import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MarkedItem } from './marked-item.model';
import { MarkedItemService } from './marked-item.service';

@Injectable()
export class MarkedItemPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private markedItemService: MarkedItemService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.markedItemService.find(id).subscribe((markedItem) => {
                this.markedItemModalRef(component, markedItem);
            });
        } else {
            return this.markedItemModalRef(component, new MarkedItem());
        }
    }

    markedItemModalRef(component: Component, markedItem: MarkedItem): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.markedItem = markedItem;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
