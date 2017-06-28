import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { MarkedItemComponent } from './marked-item.component';
import { MarkedItemDetailComponent } from './marked-item-detail.component';
import { MarkedItemPopupComponent } from './marked-item-dialog.component';
import { MarkedItemDeletePopupComponent } from './marked-item-delete-dialog.component';

import { Principal } from '../../shared';

export const markedItemRoute: Routes = [
    {
        path: 'marked-item',
        component: MarkedItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MarkedItems'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'marked-item/:id',
        component: MarkedItemDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MarkedItems'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const markedItemPopupRoute: Routes = [
    {
        path: 'marked-item-new',
        component: MarkedItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MarkedItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marked-item/:id/edit',
        component: MarkedItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MarkedItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marked-item/:id/delete',
        component: MarkedItemDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MarkedItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
