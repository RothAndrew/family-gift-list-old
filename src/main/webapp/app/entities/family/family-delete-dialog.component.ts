import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFamily } from 'app/shared/model/family.model';
import { FamilyService } from './family.service';

@Component({
    selector: 'jhi-family-delete-dialog',
    templateUrl: './family-delete-dialog.component.html'
})
export class FamilyDeleteDialogComponent {
    family: IFamily;

    constructor(private familyService: FamilyService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.familyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'familyListModification',
                content: 'Deleted an family'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-family-delete-popup',
    template: ''
})
export class FamilyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ family }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FamilyDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.family = family;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
