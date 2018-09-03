/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FglgatewayTestModule } from '../../../test.module';
import { GiftListUpdateComponent } from 'app/entities/gift-list/gift-list-update.component';
import { GiftListService } from 'app/entities/gift-list/gift-list.service';
import { GiftList } from 'app/shared/model/gift-list.model';

describe('Component Tests', () => {
    describe('GiftList Management Update Component', () => {
        let comp: GiftListUpdateComponent;
        let fixture: ComponentFixture<GiftListUpdateComponent>;
        let service: GiftListService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FglgatewayTestModule],
                declarations: [GiftListUpdateComponent]
            })
                .overrideTemplate(GiftListUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(GiftListUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GiftListService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new GiftList(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.giftList = entity;
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
                    const entity = new GiftList();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.giftList = entity;
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
