/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FglgatewayTestModule } from '../../../test.module';
import { GiftListDeleteDialogComponent } from 'app/entities/gift-list/gift-list-delete-dialog.component';
import { GiftListService } from 'app/entities/gift-list/gift-list.service';

describe('Component Tests', () => {
    describe('GiftList Management Delete Component', () => {
        let comp: GiftListDeleteDialogComponent;
        let fixture: ComponentFixture<GiftListDeleteDialogComponent>;
        let service: GiftListService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FglgatewayTestModule],
                declarations: [GiftListDeleteDialogComponent]
            })
                .overrideTemplate(GiftListDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GiftListDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GiftListService);
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
