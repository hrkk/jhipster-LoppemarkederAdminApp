import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { MarkedComponent } from './marked.component';
import { MarkedDetailComponent } from './marked-detail.component';
import { MarkedPopupComponent } from './marked-dialog.component';
import { MarkedDeletePopupComponent } from './marked-delete-dialog.component';

import { Principal } from '../../shared';

export const markedRoute: Routes = [
    {
        path: 'marked',
        component: MarkedComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Markeds'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'marked/:id',
        component: MarkedDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Markeds'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const markedPopupRoute: Routes = [
    {
        path: 'marked-new',
        component: MarkedPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Markeds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marked/:id/edit',
        component: MarkedPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Markeds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marked/:id/delete',
        component: MarkedDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Markeds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
