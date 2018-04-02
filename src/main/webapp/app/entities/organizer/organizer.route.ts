import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { OrganizerComponent } from './organizer.component';
import { OrganizerDetailComponent } from './organizer-detail.component';
import { OrganizerPopupComponent } from './organizer-dialog.component';
import { OrganizerDeletePopupComponent } from './organizer-delete-dialog.component';

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
