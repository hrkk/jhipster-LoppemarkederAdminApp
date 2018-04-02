import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LoppemarkederAdminSharedModule } from '../../shared';
import {
    MarkedService,
    MarkedPopupService,
    MarkedComponent,
    MarkedDetailComponent,
    MarkedDialogComponent,
    MarkedPopupComponent,
    MarkedDeletePopupComponent,
    MarkedDeleteDialogComponent,
    markedRoute,
    markedPopupRoute,
} from './';

const ENTITY_STATES = [
    ...markedRoute,
    ...markedPopupRoute,
];

@NgModule({
    imports: [
        LoppemarkederAdminSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MarkedComponent,
        MarkedDetailComponent,
        MarkedDialogComponent,
        MarkedDeleteDialogComponent,
        MarkedPopupComponent,
        MarkedDeletePopupComponent,
    ],
    entryComponents: [
        MarkedComponent,
        MarkedDialogComponent,
        MarkedPopupComponent,
        MarkedDeleteDialogComponent,
        MarkedDeletePopupComponent,
    ],
    providers: [
        MarkedService,
        MarkedPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoppemarkederAdminMarkedModule {}
