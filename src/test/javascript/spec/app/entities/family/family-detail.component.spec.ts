/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FglgatewayTestModule } from '../../../test.module';
import { FamilyDetailComponent } from 'app/entities/family/family-detail.component';
import { Family } from 'app/shared/model/family.model';

describe('Component Tests', () => {
    describe('Family Management Detail Component', () => {
        let comp: FamilyDetailComponent;
        let fixture: ComponentFixture<FamilyDetailComponent>;
        const route = ({ data: of({ family: new Family(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FglgatewayTestModule],
                declarations: [FamilyDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FamilyDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FamilyDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.family).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
