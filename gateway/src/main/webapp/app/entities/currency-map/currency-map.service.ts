import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { CurrencyMap } from './currency-map.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CurrencyMapService {

    private resourceUrl = '/fxpriceservice/api/currency-maps';

    constructor(private http: Http) { }

    create(currencyMap: CurrencyMap): Observable<CurrencyMap> {
        const copy = this.convert(currencyMap);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(currencyMap: CurrencyMap): Observable<CurrencyMap> {
        const copy = this.convert(currencyMap);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<CurrencyMap> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
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

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to CurrencyMap.
     */
    private convertItemFromServer(json: any): CurrencyMap {
        const entity: CurrencyMap = Object.assign(new CurrencyMap(), json);
        return entity;
    }

    /**
     * Convert a CurrencyMap to a JSON which can be sent to the server.
     */
    private convert(currencyMap: CurrencyMap): CurrencyMap {
        const copy: CurrencyMap = Object.assign({}, currencyMap);
        return copy;
    }
}
