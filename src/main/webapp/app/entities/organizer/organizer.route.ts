import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { OrganizerComponent } from './organizer.component';
import { OrganizerDetailComponent } from './organizer-detail.component';
import { OrganizerPopupComponent } from './organizer-dialog.component';
import { OrganizerDeletePopupComponent } from './organizer-delete-dialog.component';

import { Principal } from '../../shared';

export const organizerRoute: Routes = [
    {
        path: 'organizer',
        component: OrganizerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Organizers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'organizer/:id',
        component: OrganizerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Organizers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const organizerPopupRoute: Routes = [
    {
        path: 'organizer-new',
        component: OrganizerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Organizers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'organizer/:id/edit',
        component: OrganizerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Organizers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'organizer/:id/delete',
        component: OrganizerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Organizers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
