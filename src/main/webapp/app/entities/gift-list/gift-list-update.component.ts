import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IGiftList } from 'app/shared/model/gift-list.model';
import { GiftListService } from './gift-list.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-gift-list-update',
    templateUrl: './gift-list-update.component.html'
})
export class GiftListUpdateComponent implements OnInit {
    private _giftList: IGiftList;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private giftListService: GiftListService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ giftList }) => {
            this.giftList = giftList;
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
        if (this.giftList.id !== undefined) {
            this.subscribeToSaveResponse(this.giftListService.update(this.giftList));
        } else {
            this.subscribeToSaveResponse(this.giftListService.create(this.giftList));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGiftList>>) {
        result.subscribe((res: HttpResponse<IGiftList>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get giftList() {
        return this._giftList;
    }

    set giftList(giftList: IGiftList) {
        this._giftList = giftList;
    }
}
