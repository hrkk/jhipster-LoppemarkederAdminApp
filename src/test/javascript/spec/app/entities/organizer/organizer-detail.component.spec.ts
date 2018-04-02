/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { LoppemarkederAdminTestModule } from '../../../test.module';
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
                imports: [LoppemarkederAdminTestModule],
                declarations: [OrganizerDetailComponent],
                providers: [
                    OrganizerService
                ]
            })
            .overrideTemplate(OrganizerDetailComponent, '')
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

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Organizer(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.organizer).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
