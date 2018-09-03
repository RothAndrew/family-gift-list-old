import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FglgatewayFamilyModule } from './family/family.module';
import { FglgatewayGiftListModule } from './gift-list/gift-list.module';
import { FglgatewayGiftModule } from './gift/gift.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        FglgatewayFamilyModule,
        FglgatewayGiftListModule,
        FglgatewayGiftModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FglgatewayEntityModule {}
