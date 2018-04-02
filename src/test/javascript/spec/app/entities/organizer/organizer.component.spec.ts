/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LoppemarkederAdminTestModule } from '../../../test.module';
import { OrganizerComponent } from '../../../../../../main/webapp/app/entities/organizer/organizer.component';
import { OrganizerService } from '../../../../../../main/webapp/app/entities/organizer/organizer.service';
import { Organizer } from '../../../../../../main/webapp/app/entities/organizer/organizer.model';

describe('Component Tests', () => {

    describe('Organizer Management Component', () => {
        let comp: OrganizerComponent;
        let fixture: ComponentFixture<OrganizerComponent>;
        let service: OrganizerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LoppemarkederAdminTestModule],
                declarations: [OrganizerComponent],
                providers: [
                    OrganizerService
                ]
            })
            .overrideTemplate(OrganizerComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrganizerComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrganizerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Organizer(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.organizers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
