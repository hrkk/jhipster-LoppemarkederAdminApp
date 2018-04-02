import { NgModule, LOCALE_ID } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { registerLocaleData } from '@angular/common';
import locale from '@angular/common/locales/en';

import {
    LoppemarkederAdminSharedLibsModule,
    JhiAlertComponent,
    JhiAlertErrorComponent
} from './';

@NgModule({
    imports: [
        LoppemarkederAdminSharedLibsModule
    ],
    declarations: [
        JhiAlertComponent,
        JhiAlertErrorComponent
    ],
    providers: [
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'en'
        },
    ],
    exports: [
        LoppemarkederAdminSharedLibsModule,
        JhiAlertComponent,
        JhiAlertErrorComponent
    ]
})
export class LoppemarkederAdminSharedCommonModule {
    constructor() {
        registerLocaleData(locale);
    }
}
