import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { DateIntervalComponent } from './date-interval.component';
import { DateIntervalDetailComponent } from './date-interval-detail.component';
import { DateIntervalPopupComponent } from './date-interval-dialog.component';
import { DateIntervalDeletePopupComponent } from './date-interval-delete-dialog.component';

import { Principal } from '../../shared';

export const dateIntervalRoute: Routes = [
    {
        path: 'date-interval',
        component: DateIntervalComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DateIntervals'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'date-interval/:id',
        component: DateIntervalDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DateIntervals'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dateIntervalPopupRoute: Routes = [
    {
        path: 'date-interval-new',
        component: DateIntervalPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DateIntervals'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'date-interval/:id/edit',
        component: DateIntervalPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DateIntervals'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'date-interval/:id/delete',
        component: DateIntervalDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DateIntervals'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
