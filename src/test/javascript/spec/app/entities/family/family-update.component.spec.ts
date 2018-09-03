/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FglgatewayTestModule } from '../../../test.module';
import { FamilyUpdateComponent } from 'app/entities/family/family-update.component';
import { FamilyService } from 'app/entities/family/family.service';
import { Family } from 'app/shared/model/family.model';

describe('Component Tests', () => {
    describe('Family Management Update Component', () => {
        let comp: FamilyUpdateComponent;
        let fixture: ComponentFixture<FamilyUpdateComponent>;
        let service: FamilyService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FglgatewayTestModule],
                declarations: [FamilyUpdateComponent]
            })
                .overrideTemplate(FamilyUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FamilyUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FamilyService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Family(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.family = entity;
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
                    const entity = new Family();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.family = entity;
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
