import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { JhiDateUtils } from 'ng-jhipster';

import { FxRate } from './fx-rate.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FxRateService {

    private resourceUrl = '/fxpriceservice/api/fx-rates';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(fxRate: FxRate): Observable<FxRate> {
        const copy = this.convert(fxRate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(fxRate: FxRate): Observable<FxRate> {
        const copy = this.convert(fxRate);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<FxRate> {
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
     * Convert a returned JSON object to FxRate.
     */
    private convertItemFromServer(json: any): FxRate {
        const entity: FxRate = Object.assign(new FxRate(), json);
        entity.extractTime = this.dateUtils
            .convertDateTimeFromServer(json.extractTime);
        entity.arrivalDate = this.dateUtils
            .convertLocalDateFromServer(json.arrivalDate);
        return entity;
    }

    /**
     * Convert a FxRate to a JSON which can be sent to the server.
     */
    private convert(fxRate: FxRate): FxRate {
        const copy: FxRate = Object.assign({}, fxRate);

        copy.extractTime = this.dateUtils.toDate(fxRate.extractTime);
        copy.arrivalDate = this.dateUtils
            .convertLocalDateToServer(fxRate.arrivalDate);
        return copy;
    }
}
