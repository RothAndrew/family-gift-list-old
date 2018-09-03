import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGiftList } from 'app/shared/model/gift-list.model';

type EntityResponseType = HttpResponse<IGiftList>;
type EntityArrayResponseType = HttpResponse<IGiftList[]>;

@Injectable({ providedIn: 'root' })
export class GiftListService {
    private resourceUrl = SERVER_API_URL + 'api/gift-lists';

    constructor(private http: HttpClient) {}

    create(giftList: IGiftList): Observable<EntityResponseType> {
        return this.http.post<IGiftList>(this.resourceUrl, giftList, { observe: 'response' });
    }

    update(giftList: IGiftList): Observable<EntityResponseType> {
        return this.http.put<IGiftList>(this.resourceUrl, giftList, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGiftList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGiftList[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
