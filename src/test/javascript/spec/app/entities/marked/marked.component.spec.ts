/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LoppemarkederAdminTestModule } from '../../../test.module';
import { MarkedComponent } from '../../../../../../main/webapp/app/entities/marked/marked.component';
import { MarkedService } from '../../../../../../main/webapp/app/entities/marked/marked.service';
import { Marked } from '../../../../../../main/webapp/app/entities/marked/marked.model';

describe('Component Tests', () => {

    describe('Marked Management Component', () => {
        let comp: MarkedComponent;
        let fixture: ComponentFixture<MarkedComponent>;
        let service: MarkedService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LoppemarkederAdminTestModule],
                declarations: [MarkedComponent],
                providers: [
                    MarkedService
                ]
            })
            .overrideTemplate(MarkedComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MarkedComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarkedService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Marked(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.markeds[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
