import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Organizer } from './organizer.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Organizer>;

@Injectable()
export class OrganizerService {

    private resourceUrl =  SERVER_API_URL + 'api/organizers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/organizers';

    constructor(private http: HttpClient) { }

    create(organizer: Organizer): Observable<EntityResponseType> {
        const copy = this.convert(organizer);
        return this.http.post<Organizer>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(organizer: Organizer): Observable<EntityResponseType> {
        const copy = this.convert(organizer);
        return this.http.put<Organizer>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Organizer>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Organizer[]>> {
        const options = createRequestOption(req);
        return this.http.get<Organizer[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Organizer[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Organizer[]>> {
        const options = createRequestOption(req);
        return this.http.get<Organizer[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Organizer[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Organizer = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Organizer[]>): HttpResponse<Organizer[]> {
        const jsonResponse: Organizer[] = res.body;
        const body: Organizer[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Organizer.
     */
    private convertItemFromServer(organizer: Organizer): Organizer {
        const copy: Organizer = Object.assign({}, organizer);
        return copy;
    }

    /**
     * Convert a Organizer to a JSON which can be sent to the server.
     */
    private convert(organizer: Organizer): Organizer {
        const copy: Organizer = Object.assign({}, organizer);
        return copy;
    }
}
