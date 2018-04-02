import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { MarkedComponent } from './marked.component';
import { MarkedDetailComponent } from './marked-detail.component';
import { MarkedPopupComponent } from './marked-dialog.component';
import { MarkedDeletePopupComponent } from './marked-delete-dialog.component';

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
