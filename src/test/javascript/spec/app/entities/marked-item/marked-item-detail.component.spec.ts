import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { LopadminappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MarkedItemDetailComponent } from '../../../../../../main/webapp/app/entities/marked-item/marked-item-detail.component';
import { MarkedItemService } from '../../../../../../main/webapp/app/entities/marked-item/marked-item.service';
import { MarkedItem } from '../../../../../../main/webapp/app/entities/marked-item/marked-item.model';

describe('Component Tests', () => {

    describe('MarkedItem Management Detail Component', () => {
        let comp: MarkedItemDetailComponent;
        let fixture: ComponentFixture<MarkedItemDetailComponent>;
        let service: MarkedItemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LopadminappTestModule],
                declarations: [MarkedItemDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MarkedItemService,
                    EventManager
                ]
            }).overrideTemplate(MarkedItemDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MarkedItemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarkedItemService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MarkedItem(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.markedItem).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
