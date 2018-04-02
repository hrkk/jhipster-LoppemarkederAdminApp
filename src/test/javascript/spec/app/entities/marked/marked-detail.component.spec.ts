/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { LoppemarkederAdminTestModule } from '../../../test.module';
import { MarkedDetailComponent } from '../../../../../../main/webapp/app/entities/marked/marked-detail.component';
import { MarkedService } from '../../../../../../main/webapp/app/entities/marked/marked.service';
import { Marked } from '../../../../../../main/webapp/app/entities/marked/marked.model';

describe('Component Tests', () => {

    describe('Marked Management Detail Component', () => {
        let comp: MarkedDetailComponent;
        let fixture: ComponentFixture<MarkedDetailComponent>;
        let service: MarkedService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LoppemarkederAdminTestModule],
                declarations: [MarkedDetailComponent],
                providers: [
                    MarkedService
                ]
            })
            .overrideTemplate(MarkedDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MarkedDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarkedService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Marked(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.marked).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
