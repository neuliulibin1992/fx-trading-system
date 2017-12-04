import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { FxRate } from './fx-rate.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FxRateService {

    private resourceUrl = 'fxpriceservice/api/fx-rates';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(fxRate: FxRate): Observable<FxRate> {
        const copy = this.convert(fxRate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(fxRate: FxRate): Observable<FxRate> {
        const copy = this.convert(fxRate);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<FxRate> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.extractTime = this.dateUtils
            .convertDateTimeFromServer(entity.extractTime);
        entity.arrivalDate = this.dateUtils
            .convertLocalDateFromServer(entity.arrivalDate);
    }

    private convert(fxRate: FxRate): FxRate {
        const copy: FxRate = Object.assign({}, fxRate);

        copy.extractTime = this.dateUtils.toDate(fxRate.extractTime);
        copy.arrivalDate = this.dateUtils
            .convertLocalDateToServer(fxRate.arrivalDate);
        return copy;
    }
}
