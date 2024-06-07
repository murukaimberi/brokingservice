import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContractReport, NewContractReport } from '../contract-report.model';

export type PartialUpdateContractReport = Partial<IContractReport> & Pick<IContractReport, 'id'>;

type RestOf<T extends IContractReport | NewContractReport> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestContractReport = RestOf<IContractReport>;

export type NewRestContractReport = RestOf<NewContractReport>;

export type PartialUpdateRestContractReport = RestOf<PartialUpdateContractReport>;

export type EntityResponseType = HttpResponse<IContractReport>;
export type EntityArrayResponseType = HttpResponse<IContractReport[]>;

@Injectable({ providedIn: 'root' })
export class ContractReportService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contract-reports');

  create(contractReport: NewContractReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractReport);
    return this.http
      .post<RestContractReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contractReport: IContractReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractReport);
    return this.http
      .put<RestContractReport>(`${this.resourceUrl}/${this.getContractReportIdentifier(contractReport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contractReport: PartialUpdateContractReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractReport);
    return this.http
      .patch<RestContractReport>(`${this.resourceUrl}/${this.getContractReportIdentifier(contractReport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContractReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContractReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContractReportIdentifier(contractReport: Pick<IContractReport, 'id'>): number {
    return contractReport.id;
  }

  compareContractReport(o1: Pick<IContractReport, 'id'> | null, o2: Pick<IContractReport, 'id'> | null): boolean {
    return o1 && o2 ? this.getContractReportIdentifier(o1) === this.getContractReportIdentifier(o2) : o1 === o2;
  }

  addContractReportToCollectionIfMissing<Type extends Pick<IContractReport, 'id'>>(
    contractReportCollection: Type[],
    ...contractReportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contractReports: Type[] = contractReportsToCheck.filter(isPresent);
    if (contractReports.length > 0) {
      const contractReportCollectionIdentifiers = contractReportCollection.map(contractReportItem =>
        this.getContractReportIdentifier(contractReportItem),
      );
      const contractReportsToAdd = contractReports.filter(contractReportItem => {
        const contractReportIdentifier = this.getContractReportIdentifier(contractReportItem);
        if (contractReportCollectionIdentifiers.includes(contractReportIdentifier)) {
          return false;
        }
        contractReportCollectionIdentifiers.push(contractReportIdentifier);
        return true;
      });
      return [...contractReportsToAdd, ...contractReportCollection];
    }
    return contractReportCollection;
  }

  protected convertDateFromClient<T extends IContractReport | NewContractReport | PartialUpdateContractReport>(
    contractReport: T,
  ): RestOf<T> {
    return {
      ...contractReport,
      createdDate: contractReport.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restContractReport: RestContractReport): IContractReport {
    return {
      ...restContractReport,
      createdDate: restContractReport.createdDate ? dayjs(restContractReport.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContractReport>): HttpResponse<IContractReport> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContractReport[]>): HttpResponse<IContractReport[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
