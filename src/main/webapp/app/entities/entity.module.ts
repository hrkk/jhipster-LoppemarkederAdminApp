import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LopadminappMarkedModule } from './marked/marked.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        LopadminappMarkedModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LopadminappEntityModule {}
