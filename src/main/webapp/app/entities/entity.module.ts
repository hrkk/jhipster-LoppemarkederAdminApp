import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LopadminappMarkedItemModule } from './marked-item/marked-item.module';
import { LopadminappDateIntervalModule } from './date-interval/date-interval.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        LopadminappMarkedItemModule,
        LopadminappDateIntervalModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LopadminappEntityModule {}
