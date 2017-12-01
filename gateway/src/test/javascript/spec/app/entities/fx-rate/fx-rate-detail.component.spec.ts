/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FxgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FxRateDetailComponent } from '../../../../../../main/webapp/app/entities/fx-rate/fx-rate-detail.component';
import { FxRateService } from '../../../../../../main/webapp/app/entities/fx-rate/fx-rate.service';
import { FxRate } from '../../../../../../main/webapp/app/entities/fx-rate/fx-rate.model';

describe('Component Tests', () => {

    describe('FxRate Management Detail Component', () => {
        let comp: FxRateDetailComponent;
        let fixture: ComponentFixture<FxRateDetailComponent>;
        let service: FxRateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FxgatewayTestModule],
                declarations: [FxRateDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FxRateService,
                    JhiEventManager
                ]
            }).overrideTemplate(FxRateDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FxRateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FxRateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new FxRate(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.fxRate).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
