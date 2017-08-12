import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Organizer } from './organizer.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class OrganizerService {

    private resourceUrl = 'api/organizers';
    private resourceSearchUrl = 'api/_search/organizers';

    constructor(private http: Http) { }

    create(organizer: Organizer): Observable<Organizer> {
        const copy = this.convert(organizer);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(organizer: Organizer): Observable<Organizer> {
        const copy = this.convert(organizer);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Organizer> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(organizer: Organizer): Organizer {
        const copy: Organizer = Object.assign({}, organizer);
        return copy;
    }
}
