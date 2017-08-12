import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LopadminappSharedModule } from '../../shared';
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
        LopadminappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
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
export class LopadminappOrganizerModule {}
