import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGiftList } from 'app/shared/model/gift-list.model';

@Component({
    selector: 'jhi-gift-list-detail',
    templateUrl: './gift-list-detail.component.html'
})
export class GiftListDetailComponent implements OnInit {
    giftList: IGiftList;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ giftList }) => {
            this.giftList = giftList;
        });
    }

    previousState() {
        window.history.back();
    }
}
