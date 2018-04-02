import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LoppemarkederAdminSharedModule } from '../../shared';
import {
    OrganizerService,
    OrganizerPopupService,
    OrganizerComponent,
    OrganizerDetailComponent,
    OrganizerDialogComponent,
    OrganizerPopupComponent,
    OrganizerDeletePopupComponent,
    OrganizerDeleteDialogComponent,
    organizerRoute,
    organizerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...organizerRoute,
    ...organizerPopupRoute,
];

@NgModule({
    imports: [
        LoppemarkederAdminSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        OrganizerComponent,
        OrganizerDetailComponent,
        OrganizerDialogComponent,
        OrganizerDeleteDialogComponent,
        OrganizerPopupComponent,
        OrganizerDeletePopupComponent,
    ],
    entryComponents: [
        OrganizerComponent,
        OrganizerDialogComponent,
        OrganizerPopupComponent,
        OrganizerDeleteDialogComponent,
        OrganizerDeletePopupComponent,
    ],
    providers: [
        OrganizerService,
        OrganizerPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoppemarkederAdminOrganizerModule {}
