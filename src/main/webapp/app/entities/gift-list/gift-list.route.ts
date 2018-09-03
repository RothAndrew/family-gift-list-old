import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GiftList } from 'app/shared/model/gift-list.model';
import { GiftListService } from './gift-list.service';
import { GiftListComponent } from './gift-list.component';
import { GiftListDetailComponent } from './gift-list-detail.component';
import { GiftListUpdateComponent } from './gift-list-update.component';
import { GiftListDeletePopupComponent } from './gift-list-delete-dialog.component';
import { IGiftList } from 'app/shared/model/gift-list.model';

@Injectable({ providedIn: 'root' })
export class GiftListResolve implements Resolve<IGiftList> {
    constructor(private service: GiftListService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((giftList: HttpResponse<GiftList>) => giftList.body));
        }
        return of(new GiftList());
    }
}

export const giftListRoute: Routes = [
    {
        path: 'gift-list',
        component: GiftListComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.giftList.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gift-list/:id/view',
        component: GiftListDetailComponent,
        resolve: {
            giftList: GiftListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.giftList.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gift-list/new',
        component: GiftListUpdateComponent,
        resolve: {
            giftList: GiftListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.giftList.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gift-list/:id/edit',
        component: GiftListUpdateComponent,
        resolve: {
            giftList: GiftListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.giftList.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const giftListPopupRoute: Routes = [
    {
        path: 'gift-list/:id/delete',
        component: GiftListDeletePopupComponent,
        resolve: {
            giftList: GiftListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.giftList.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
