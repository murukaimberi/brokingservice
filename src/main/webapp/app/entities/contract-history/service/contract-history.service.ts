import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContractHistory, NewContractHistory } from '../contract-history.model';

export type PartialUpdateContractHistory = Partial<IContractHistory> & Pick<IContractHistory, 'id'>;

type RestOf<T extends IContractHistory | NewContractHistory> = Omit<
  T,
  'contractCreatedDate' | 'contractActiveDate' | 'contractInActiveDate' | 'contractLastModifiedDate'
> & {
  contractCreatedDate?: string | null;
  contractActiveDate?: string | null;
  contractInActiveDate?: string | null;
  contractLastModifiedDate?: string | null;
};

export type RestContractHistory = RestOf<IContractHistory>;

export type NewRestContractHistory = RestOf<NewContractHistory>;

export type PartialUpdateRestContractHistory = RestOf<PartialUpdateContractHistory>;

export type EntityResponseType = HttpResponse<IContractHistory>;
export type EntityArrayResponseType = HttpResponse<IContractHistory[]>;

@Injectable({ providedIn: 'root' })
export class ContractHistoryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contract-histories');

  create(contractHistory: NewContractHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractHistory);
    return this.http
      .post<RestContractHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contractHistory: IContractHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractHistory);
    return this.http
      .put<RestContractHistory>(`${this.resourceUrl}/${this.getContractHistoryIdentifier(contractHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contractHistory: PartialUpdateContractHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractHistory);
    return this.http
      .patch<RestContractHistory>(`${this.resourceUrl}/${this.getContractHistoryIdentifier(contractHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContractHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContractHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContractHistoryIdentifier(contractHistory: Pick<IContractHistory, 'id'>): number {
    return contractHistory.id;
  }

  compareContractHistory(o1: Pick<IContractHistory, 'id'> | null, o2: Pick<IContractHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getContractHistoryIdentifier(o1) === this.getContractHistoryIdentifier(o2) : o1 === o2;
  }

  addContractHistoryToCollectionIfMissing<Type extends Pick<IContractHistory, 'id'>>(
    contractHistoryCollection: Type[],
    ...contractHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contractHistories: Type[] = contractHistoriesToCheck.filter(isPresent);
    if (contractHistories.length > 0) {
      const contractHistoryCollectionIdentifiers = contractHistoryCollection.map(contractHistoryItem =>
        this.getContractHistoryIdentifier(contractHistoryItem),
      );
      const contractHistoriesToAdd = contractHistories.filter(contractHistoryItem => {
        const contractHistoryIdentifier = this.getContractHistoryIdentifier(contractHistoryItem);
        if (contractHistoryCollectionIdentifiers.includes(contractHistoryIdentifier)) {
          return false;
        }
        contractHistoryCollectionIdentifiers.push(contractHistoryIdentifier);
        return true;
      });
      return [...contractHistoriesToAdd, ...contractHistoryCollection];
    }
    return contractHistoryCollection;
  }

  protected convertDateFromClient<T extends IContractHistory | NewContractHistory | PartialUpdateContractHistory>(
    contractHistory: T,
  ): RestOf<T> {
    return {
      ...contractHistory,
      contractCreatedDate: contractHistory.contractCreatedDate?.toJSON() ?? null,
      contractActiveDate: contractHistory.contractActiveDate?.toJSON() ?? null,
      contractInActiveDate: contractHistory.contractInActiveDate?.toJSON() ?? null,
      contractLastModifiedDate: contractHistory.contractLastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restContractHistory: RestContractHistory): IContractHistory {
    return {
      ...restContractHistory,
      contractCreatedDate: restContractHistory.contractCreatedDate ? dayjs(restContractHistory.contractCreatedDate) : undefined,
      contractActiveDate: restContractHistory.contractActiveDate ? dayjs(restContractHistory.contractActiveDate) : undefined,
      contractInActiveDate: restContractHistory.contractInActiveDate ? dayjs(restContractHistory.contractInActiveDate) : undefined,
      contractLastModifiedDate: restContractHistory.contractLastModifiedDate
        ? dayjs(restContractHistory.contractLastModifiedDate)
        : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContractHistory>): HttpResponse<IContractHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContractHistory[]>): HttpResponse<IContractHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
