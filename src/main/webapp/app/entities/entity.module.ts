import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LopadminappMarkedModule } from './marked/marked.module';
import { LopadminappDateIntervalModule } from './date-interval/date-interval.module';
import { LopadminappOrganizerModule } from './organizer/organizer.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        LopadminappMarkedModule,
        LopadminappDateIntervalModule,
        LopadminappOrganizerModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LopadminappEntityModule {}
