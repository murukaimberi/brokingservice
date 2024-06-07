import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInsuranceType, NewInsuranceType } from '../insurance-type.model';

export type PartialUpdateInsuranceType = Partial<IInsuranceType> & Pick<IInsuranceType, 'id'>;

export type EntityResponseType = HttpResponse<IInsuranceType>;
export type EntityArrayResponseType = HttpResponse<IInsuranceType[]>;

@Injectable({ providedIn: 'root' })
export class InsuranceTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/insurance-types');

  create(insuranceType: NewInsuranceType): Observable<EntityResponseType> {
    return this.http.post<IInsuranceType>(this.resourceUrl, insuranceType, { observe: 'response' });
  }

  update(insuranceType: IInsuranceType): Observable<EntityResponseType> {
    return this.http.put<IInsuranceType>(`${this.resourceUrl}/${this.getInsuranceTypeIdentifier(insuranceType)}`, insuranceType, {
      observe: 'response',
    });
  }

  partialUpdate(insuranceType: PartialUpdateInsuranceType): Observable<EntityResponseType> {
    return this.http.patch<IInsuranceType>(`${this.resourceUrl}/${this.getInsuranceTypeIdentifier(insuranceType)}`, insuranceType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInsuranceType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInsuranceType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInsuranceTypeIdentifier(insuranceType: Pick<IInsuranceType, 'id'>): number {
    return insuranceType.id;
  }

  compareInsuranceType(o1: Pick<IInsuranceType, 'id'> | null, o2: Pick<IInsuranceType, 'id'> | null): boolean {
    return o1 && o2 ? this.getInsuranceTypeIdentifier(o1) === this.getInsuranceTypeIdentifier(o2) : o1 === o2;
  }

  addInsuranceTypeToCollectionIfMissing<Type extends Pick<IInsuranceType, 'id'>>(
    insuranceTypeCollection: Type[],
    ...insuranceTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const insuranceTypes: Type[] = insuranceTypesToCheck.filter(isPresent);
    if (insuranceTypes.length > 0) {
      const insuranceTypeCollectionIdentifiers = insuranceTypeCollection.map(insuranceTypeItem =>
        this.getInsuranceTypeIdentifier(insuranceTypeItem),
      );
      const insuranceTypesToAdd = insuranceTypes.filter(insuranceTypeItem => {
        const insuranceTypeIdentifier = this.getInsuranceTypeIdentifier(insuranceTypeItem);
        if (insuranceTypeCollectionIdentifiers.includes(insuranceTypeIdentifier)) {
          return false;
        }
        insuranceTypeCollectionIdentifiers.push(insuranceTypeIdentifier);
        return true;
      });
      return [...insuranceTypesToAdd, ...insuranceTypeCollection];
    }
    return insuranceTypeCollection;
  }
}
