import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FglgatewaySharedModule } from 'app/shared';
import { FglgatewayAdminModule } from 'app/admin/admin.module';
import {
    FamilyComponent,
    FamilyDetailComponent,
    FamilyUpdateComponent,
    FamilyDeletePopupComponent,
    FamilyDeleteDialogComponent,
    familyRoute,
    familyPopupRoute
} from './';

const ENTITY_STATES = [...familyRoute, ...familyPopupRoute];

@NgModule({
    imports: [FglgatewaySharedModule, FglgatewayAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [FamilyComponent, FamilyDetailComponent, FamilyUpdateComponent, FamilyDeleteDialogComponent, FamilyDeletePopupComponent],
    entryComponents: [FamilyComponent, FamilyUpdateComponent, FamilyDeleteDialogComponent, FamilyDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FglgatewayFamilyModule {}
