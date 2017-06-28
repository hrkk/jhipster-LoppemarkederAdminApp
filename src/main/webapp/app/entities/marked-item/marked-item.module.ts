import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LopadminappSharedModule } from '../../shared';
import {
    MarkedItemService,
    MarkedItemPopupService,
    MarkedItemComponent,
    MarkedItemDetailComponent,
    MarkedItemDialogComponent,
    MarkedItemPopupComponent,
    MarkedItemDeletePopupComponent,
    MarkedItemDeleteDialogComponent,
    markedItemRoute,
    markedItemPopupRoute,
} from './';

const ENTITY_STATES = [
    ...markedItemRoute,
    ...markedItemPopupRoute,
];

@NgModule({
    imports: [
        LopadminappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MarkedItemComponent,
        MarkedItemDetailComponent,
        MarkedItemDialogComponent,
        MarkedItemDeleteDialogComponent,
        MarkedItemPopupComponent,
        MarkedItemDeletePopupComponent,
    ],
    entryComponents: [
        MarkedItemComponent,
        MarkedItemDialogComponent,
        MarkedItemPopupComponent,
        MarkedItemDeleteDialogComponent,
        MarkedItemDeletePopupComponent,
    ],
    providers: [
        MarkedItemService,
        MarkedItemPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LopadminappMarkedItemModule {}
