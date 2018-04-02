import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Marked } from './marked.model';
import { MarkedService } from './marked.service';

@Injectable()
export class MarkedPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private markedService: MarkedService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.markedService.find(id)
                    .subscribe((markedResponse: HttpResponse<Marked>) => {
                        const marked: Marked = markedResponse.body;
                        if (marked.fromDate) {
                            marked.fromDate = {
                                year: marked.fromDate.getFullYear(),
                                month: marked.fromDate.getMonth() + 1,
                                day: marked.fromDate.getDate()
                            };
                        }
                        if (marked.toDate) {
                            marked.toDate = {
                                year: marked.toDate.getFullYear(),
                                month: marked.toDate.getMonth() + 1,
                                day: marked.toDate.getDate()
                            };
                        }
                        this.ngbModalRef = this.markedModalRef(component, marked);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.markedModalRef(component, new Marked());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    markedModalRef(component: Component, marked: Marked): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.marked = marked;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
