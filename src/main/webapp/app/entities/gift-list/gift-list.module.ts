import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FglgatewaySharedModule } from 'app/shared';
import { FglgatewayAdminModule } from 'app/admin/admin.module';
import {
    GiftListComponent,
    GiftListDetailComponent,
    GiftListUpdateComponent,
    GiftListDeletePopupComponent,
    GiftListDeleteDialogComponent,
    giftListRoute,
    giftListPopupRoute
} from './';

const ENTITY_STATES = [...giftListRoute, ...giftListPopupRoute];

@NgModule({
    imports: [FglgatewaySharedModule, FglgatewayAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GiftListComponent,
        GiftListDetailComponent,
        GiftListUpdateComponent,
        GiftListDeleteDialogComponent,
        GiftListDeletePopupComponent
    ],
    entryComponents: [GiftListComponent, GiftListUpdateComponent, GiftListDeleteDialogComponent, GiftListDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FglgatewayGiftListModule {}
