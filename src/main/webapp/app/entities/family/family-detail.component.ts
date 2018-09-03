import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFamily } from 'app/shared/model/family.model';

@Component({
    selector: 'jhi-family-detail',
    templateUrl: './family-detail.component.html'
})
export class FamilyDetailComponent implements OnInit {
    family: IFamily;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ family }) => {
            this.family = family;
        });
    }

    previousState() {
        window.history.back();
    }
}
