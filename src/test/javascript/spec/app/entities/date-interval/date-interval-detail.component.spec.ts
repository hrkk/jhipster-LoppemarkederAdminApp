import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { LopadminappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DateIntervalDetailComponent } from '../../../../../../main/webapp/app/entities/date-interval/date-interval-detail.component';
import { DateIntervalService } from '../../../../../../main/webapp/app/entities/date-interval/date-interval.service';
import { DateInterval } from '../../../../../../main/webapp/app/entities/date-interval/date-interval.model';

describe('Component Tests', () => {

    describe('DateInterval Management Detail Component', () => {
        let comp: DateIntervalDetailComponent;
        let fixture: ComponentFixture<DateIntervalDetailComponent>;
        let service: DateIntervalService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LopadminappTestModule],
                declarations: [DateIntervalDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DateIntervalService,
                    EventManager
                ]
            }).overrideTemplate(DateIntervalDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DateIntervalDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DateIntervalService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new DateInterval(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.dateInterval).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
