import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LoppemarkederAdminMarkedModule } from './marked/marked.module';
import { LoppemarkederAdminOrganizerModule } from './organizer/organizer.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        LoppemarkederAdminMarkedModule,
        LoppemarkederAdminOrganizerModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoppemarkederAdminEntityModule {}
