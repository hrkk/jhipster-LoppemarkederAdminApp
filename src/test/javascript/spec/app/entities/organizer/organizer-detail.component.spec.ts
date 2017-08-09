import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { LopadminappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OrganizerDetailComponent } from '../../../../../../main/webapp/app/entities/organizer/organizer-detail.component';
import { OrganizerService } from '../../../../../../main/webapp/app/entities/organizer/organizer.service';
import { Organizer } from '../../../../../../main/webapp/app/entities/organizer/organizer.model';

describe('Component Tests', () => {

    describe('Organizer Management Detail Component', () => {
        let comp: OrganizerDetailComponent;
        let fixture: ComponentFixture<OrganizerDetailComponent>;
        let service: OrganizerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LopadminappTestModule],
                declarations: [OrganizerDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OrganizerService,
                    EventManager
                ]
            }).overrideTemplate(OrganizerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrganizerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrganizerService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Organizer(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.organizer).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
