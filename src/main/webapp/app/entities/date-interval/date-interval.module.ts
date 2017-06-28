import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LopadminappSharedModule } from '../../shared';
import {
    DateIntervalService,
    DateIntervalPopupService,
    DateIntervalComponent,
    DateIntervalDetailComponent,
    DateIntervalDialogComponent,
    DateIntervalPopupComponent,
    DateIntervalDeletePopupComponent,
    DateIntervalDeleteDialogComponent,
    dateIntervalRoute,
    dateIntervalPopupRoute,
} from './';

const ENTITY_STATES = [
    ...dateIntervalRoute,
    ...dateIntervalPopupRoute,
];

@NgModule({
    imports: [
        LopadminappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        DateIntervalComponent,
        DateIntervalDetailComponent,
        DateIntervalDialogComponent,
        DateIntervalDeleteDialogComponent,
        DateIntervalPopupComponent,
        DateIntervalDeletePopupComponent,
    ],
    entryComponents: [
        DateIntervalComponent,
        DateIntervalDialogComponent,
        DateIntervalPopupComponent,
        DateIntervalDeleteDialogComponent,
        DateIntervalDeletePopupComponent,
    ],
    providers: [
        DateIntervalService,
        DateIntervalPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LopadminappDateIntervalModule {}
