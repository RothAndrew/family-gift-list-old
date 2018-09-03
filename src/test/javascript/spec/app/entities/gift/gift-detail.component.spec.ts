/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FglgatewayTestModule } from '../../../test.module';
import { GiftDetailComponent } from 'app/entities/gift/gift-detail.component';
import { Gift } from 'app/shared/model/gift.model';

describe('Component Tests', () => {
    describe('Gift Management Detail Component', () => {
        let comp: GiftDetailComponent;
        let fixture: ComponentFixture<GiftDetailComponent>;
        const route = ({ data: of({ gift: new Gift(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FglgatewayTestModule],
                declarations: [GiftDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(GiftDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GiftDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.gift).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
