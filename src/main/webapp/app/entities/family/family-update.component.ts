import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFamily } from 'app/shared/model/family.model';
import { FamilyService } from './family.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-family-update',
    templateUrl: './family-update.component.html'
})
export class FamilyUpdateComponent implements OnInit {
    private _family: IFamily;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private familyService: FamilyService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ family }) => {
            this.family = family;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.family.id !== undefined) {
            this.subscribeToSaveResponse(this.familyService.update(this.family));
        } else {
            this.subscribeToSaveResponse(this.familyService.create(this.family));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFamily>>) {
        result.subscribe((res: HttpResponse<IFamily>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get family() {
        return this._family;
    }

    set family(family: IFamily) {
        this._family = family;
    }
}
