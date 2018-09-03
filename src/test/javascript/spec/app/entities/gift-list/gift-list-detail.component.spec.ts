/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FglgatewayTestModule } from '../../../test.module';
import { GiftListDetailComponent } from 'app/entities/gift-list/gift-list-detail.component';
import { GiftList } from 'app/shared/model/gift-list.model';

describe('Component Tests', () => {
    describe('GiftList Management Detail Component', () => {
        let comp: GiftListDetailComponent;
        let fixture: ComponentFixture<GiftListDetailComponent>;
        const route = ({ data: of({ giftList: new GiftList(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FglgatewayTestModule],
                declarations: [GiftListDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(GiftListDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GiftListDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.giftList).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
