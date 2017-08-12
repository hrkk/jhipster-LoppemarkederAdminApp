import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Marked } from './marked.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MarkedService {

    private resourceUrl = 'api/markeds';
    private resourceSearchUrl = 'api/_search/markeds';

    constructor(private http: Http) { }

    create(marked: Marked): Observable<Marked> {
        const copy = this.convert(marked);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(marked: Marked): Observable<Marked> {
        const copy = this.convert(marked);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Marked> {
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

    private convert(marked: Marked): Marked {
        const copy: Marked = Object.assign({}, marked);
        return copy;
    }
}
