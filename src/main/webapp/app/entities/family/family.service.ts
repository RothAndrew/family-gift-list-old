import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFamily } from 'app/shared/model/family.model';

type EntityResponseType = HttpResponse<IFamily>;
type EntityArrayResponseType = HttpResponse<IFamily[]>;

@Injectable({ providedIn: 'root' })
export class FamilyService {
    private resourceUrl = SERVER_API_URL + 'api/families';

    constructor(private http: HttpClient) {}

    create(family: IFamily): Observable<EntityResponseType> {
        return this.http.post<IFamily>(this.resourceUrl, family, { observe: 'response' });
    }

    update(family: IFamily): Observable<EntityResponseType> {
        return this.http.put<IFamily>(this.resourceUrl, family, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFamily>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFamily[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
