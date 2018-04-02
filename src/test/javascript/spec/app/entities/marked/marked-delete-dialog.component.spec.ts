/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { LoppemarkederAdminTestModule } from '../../../test.module';
import { MarkedDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/marked/marked-delete-dialog.component';
import { MarkedService } from '../../../../../../main/webapp/app/entities/marked/marked.service';

describe('Component Tests', () => {

    describe('Marked Management Delete Component', () => {
        let comp: MarkedDeleteDialogComponent;
        let fixture: ComponentFixture<MarkedDeleteDialogComponent>;
        let service: MarkedService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LoppemarkederAdminTestModule],
                declarations: [MarkedDeleteDialogComponent],
                providers: [
                    MarkedService
                ]
            })
            .overrideTemplate(MarkedDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MarkedDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarkedService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
