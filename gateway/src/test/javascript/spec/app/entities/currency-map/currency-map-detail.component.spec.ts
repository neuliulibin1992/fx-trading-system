/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FxgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CurrencyMapDetailComponent } from '../../../../../../main/webapp/app/entities/currency-map/currency-map-detail.component';
import { CurrencyMapService } from '../../../../../../main/webapp/app/entities/currency-map/currency-map.service';
import { CurrencyMap } from '../../../../../../main/webapp/app/entities/currency-map/currency-map.model';

describe('Component Tests', () => {

    describe('CurrencyMap Management Detail Component', () => {
        let comp: CurrencyMapDetailComponent;
        let fixture: ComponentFixture<CurrencyMapDetailComponent>;
        let service: CurrencyMapService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FxgatewayTestModule],
                declarations: [CurrencyMapDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CurrencyMapService,
                    JhiEventManager
                ]
            }).overrideTemplate(CurrencyMapDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CurrencyMapDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CurrencyMapService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CurrencyMap(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.currencyMap).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
