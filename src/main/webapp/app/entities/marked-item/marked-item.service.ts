import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { MarkedItem } from './marked-item.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MarkedItemService {

    private resourceUrl = 'api/marked-items';
    private resourceSearchUrl = 'api/_search/marked-items';

    constructor(private http: Http) { }

    create(markedItem: MarkedItem): Observable<MarkedItem> {
        const copy = this.convert(markedItem);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(markedItem: MarkedItem): Observable<MarkedItem> {
        const copy = this.convert(markedItem);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<MarkedItem> {
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

    private convert(markedItem: MarkedItem): MarkedItem {
        const copy: MarkedItem = Object.assign({}, markedItem);
        return copy;
    }
}
