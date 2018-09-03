import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Family } from 'app/shared/model/family.model';
import { FamilyService } from './family.service';
import { FamilyComponent } from './family.component';
import { FamilyDetailComponent } from './family-detail.component';
import { FamilyUpdateComponent } from './family-update.component';
import { FamilyDeletePopupComponent } from './family-delete-dialog.component';
import { IFamily } from 'app/shared/model/family.model';

@Injectable({ providedIn: 'root' })
export class FamilyResolve implements Resolve<IFamily> {
    constructor(private service: FamilyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((family: HttpResponse<Family>) => family.body));
        }
        return of(new Family());
    }
}

export const familyRoute: Routes = [
    {
        path: 'family',
        component: FamilyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.family.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'family/:id/view',
        component: FamilyDetailComponent,
        resolve: {
            family: FamilyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.family.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'family/new',
        component: FamilyUpdateComponent,
        resolve: {
            family: FamilyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.family.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'family/:id/edit',
        component: FamilyUpdateComponent,
        resolve: {
            family: FamilyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.family.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const familyPopupRoute: Routes = [
    {
        path: 'family/:id/delete',
        component: FamilyDeletePopupComponent,
        resolve: {
            family: FamilyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fglgatewayApp.family.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
