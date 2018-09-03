import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IGift } from 'app/shared/model/gift.model';
import { GiftService } from './gift.service';
import { IGiftList } from 'app/shared/model/gift-list.model';
import { GiftListService } from 'app/entities/gift-list';

@Component({
    selector: 'jhi-gift-update',
    templateUrl: './gift-update.component.html'
})
export class GiftUpdateComponent implements OnInit {
    private _gift: IGift;
    isSaving: boolean;

    giftlists: IGiftList[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private giftService: GiftService,
        private giftListService: GiftListService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ gift }) => {
            this.gift = gift;
        });
        this.giftListService.query().subscribe(
            (res: HttpResponse<IGiftList[]>) => {
                this.giftlists = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.gift.id !== undefined) {
            this.subscribeToSaveResponse(this.giftService.update(this.gift));
        } else {
            this.subscribeToSaveResponse(this.giftService.create(this.gift));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGift>>) {
        result.subscribe((res: HttpResponse<IGift>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackGiftListById(index: number, item: IGiftList) {
        return item.id;
    }
    get gift() {
        return this._gift;
    }

    set gift(gift: IGift) {
        this._gift = gift;
    }
}
