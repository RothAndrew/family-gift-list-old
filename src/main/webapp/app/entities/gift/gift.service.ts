import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGift } from 'app/shared/model/gift.model';

type EntityResponseType = HttpResponse<IGift>;
type EntityArrayResponseType = HttpResponse<IGift[]>;

@Injectable({ providedIn: 'root' })
export class GiftService {
    private resourceUrl = SERVER_API_URL + 'api/gifts';

    constructor(private http: HttpClient) {}

    create(gift: IGift): Observable<EntityResponseType> {
        return this.http.post<IGift>(this.resourceUrl, gift, { observe: 'response' });
    }

    update(gift: IGift): Observable<EntityResponseType> {
        return this.http.put<IGift>(this.resourceUrl, gift, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGift>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGift[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
