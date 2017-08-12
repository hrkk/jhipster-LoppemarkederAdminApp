import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { LopadminappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
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
                imports: [LopadminappTestModule],
                declarations: [MarkedDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MarkedService,
                    EventManager
                ]
            }).overrideTemplate(MarkedDetailComponent, '')
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

            spyOn(service, 'find').and.returnValue(Observable.of(new Marked(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.marked).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
