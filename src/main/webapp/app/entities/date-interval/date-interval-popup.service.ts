import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DateInterval } from './date-interval.model';
import { DateIntervalService } from './date-interval.service';

@Injectable()
export class DateIntervalPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private dateIntervalService: DateIntervalService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.dateIntervalService.find(id).subscribe((dateInterval) => {
                if (dateInterval.fromDate) {
                    dateInterval.fromDate = {
                        year: dateInterval.fromDate.getFullYear(),
                        month: dateInterval.fromDate.getMonth() + 1,
                        day: dateInterval.fromDate.getDate()
                    };
                }
                if (dateInterval.toDate) {
                    dateInterval.toDate = {
                        year: dateInterval.toDate.getFullYear(),
                        month: dateInterval.toDate.getMonth() + 1,
                        day: dateInterval.toDate.getDate()
                    };
                }
                this.dateIntervalModalRef(component, dateInterval);
            });
        } else {
            return this.dateIntervalModalRef(component, new DateInterval());
        }
    }

    dateIntervalModalRef(component: Component, dateInterval: DateInterval): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.dateInterval = dateInterval;
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
