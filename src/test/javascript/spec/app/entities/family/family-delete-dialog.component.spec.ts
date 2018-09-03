/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FglgatewayTestModule } from '../../../test.module';
import { FamilyDeleteDialogComponent } from 'app/entities/family/family-delete-dialog.component';
import { FamilyService } from 'app/entities/family/family.service';

describe('Component Tests', () => {
    describe('Family Management Delete Component', () => {
        let comp: FamilyDeleteDialogComponent;
        let fixture: ComponentFixture<FamilyDeleteDialogComponent>;
        let service: FamilyService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FglgatewayTestModule],
                declarations: [FamilyDeleteDialogComponent]
            })
                .overrideTemplate(FamilyDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FamilyDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FamilyService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

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
