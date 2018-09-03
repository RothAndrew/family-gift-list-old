import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FglgatewaySharedModule } from 'app/shared';
import {
    GiftComponent,
    GiftDetailComponent,
    GiftUpdateComponent,
    GiftDeletePopupComponent,
    GiftDeleteDialogComponent,
    giftRoute,
    giftPopupRoute
} from './';

const ENTITY_STATES = [...giftRoute, ...giftPopupRoute];

@NgModule({
    imports: [FglgatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [GiftComponent, GiftDetailComponent, GiftUpdateComponent, GiftDeleteDialogComponent, GiftDeletePopupComponent],
    entryComponents: [GiftComponent, GiftUpdateComponent, GiftDeleteDialogComponent, GiftDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FglgatewayGiftModule {}
