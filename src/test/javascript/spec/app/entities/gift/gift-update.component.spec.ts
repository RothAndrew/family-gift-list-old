/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FglgatewayTestModule } from '../../../test.module';
import { GiftUpdateComponent } from 'app/entities/gift/gift-update.component';
import { GiftService } from 'app/entities/gift/gift.service';
import { Gift } from 'app/shared/model/gift.model';

describe('Component Tests', () => {
    describe('Gift Management Update Component', () => {
        let comp: GiftUpdateComponent;
        let fixture: ComponentFixture<GiftUpdateComponent>;
        let service: GiftService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FglgatewayTestModule],
                declarations: [GiftUpdateComponent]
            })
                .overrideTemplate(GiftUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(GiftUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GiftService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Gift(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.gift = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Gift();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.gift = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
