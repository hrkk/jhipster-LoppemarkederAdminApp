import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Marked } from './marked.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Marked>;

@Injectable()
export class MarkedService {

    private resourceUrl =  SERVER_API_URL + 'api/markeds';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/markeds';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(marked: Marked): Observable<EntityResponseType> {
        const copy = this.convert(marked);
        return this.http.post<Marked>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(marked: Marked): Observable<EntityResponseType> {
        const copy = this.convert(marked);
        return this.http.put<Marked>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Marked>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Marked[]>> {
        const options = createRequestOption(req);
        return this.http.get<Marked[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Marked[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Marked[]>> {
        const options = createRequestOption(req);
        return this.http.get<Marked[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Marked[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Marked = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Marked[]>): HttpResponse<Marked[]> {
        const jsonResponse: Marked[] = res.body;
        const body: Marked[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Marked.
     */
    private convertItemFromServer(marked: Marked): Marked {
        const copy: Marked = Object.assign({}, marked);
        copy.fromDate = this.dateUtils
            .convertLocalDateFromServer(marked.fromDate);
        copy.toDate = this.dateUtils
            .convertLocalDateFromServer(marked.toDate);
        return copy;
    }

    /**
     * Convert a Marked to a JSON which can be sent to the server.
     */
    private convert(marked: Marked): Marked {
        const copy: Marked = Object.assign({}, marked);
        copy.fromDate = this.dateUtils
            .convertLocalDateToServer(marked.fromDate);
        copy.toDate = this.dateUtils
            .convertLocalDateToServer(marked.toDate);
        return copy;
    }
}
