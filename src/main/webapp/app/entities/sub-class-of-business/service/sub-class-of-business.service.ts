import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubClassOfBusiness, NewSubClassOfBusiness } from '../sub-class-of-business.model';

export type PartialUpdateSubClassOfBusiness = Partial<ISubClassOfBusiness> & Pick<ISubClassOfBusiness, 'id'>;

export type EntityResponseType = HttpResponse<ISubClassOfBusiness>;
export type EntityArrayResponseType = HttpResponse<ISubClassOfBusiness[]>;

@Injectable({ providedIn: 'root' })
export class SubClassOfBusinessService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sub-class-of-businesses');

  create(subClassOfBusiness: NewSubClassOfBusiness): Observable<EntityResponseType> {
    return this.http.post<ISubClassOfBusiness>(this.resourceUrl, subClassOfBusiness, { observe: 'response' });
  }

  update(subClassOfBusiness: ISubClassOfBusiness): Observable<EntityResponseType> {
    return this.http.put<ISubClassOfBusiness>(
      `${this.resourceUrl}/${this.getSubClassOfBusinessIdentifier(subClassOfBusiness)}`,
      subClassOfBusiness,
      { observe: 'response' },
    );
  }

  partialUpdate(subClassOfBusiness: PartialUpdateSubClassOfBusiness): Observable<EntityResponseType> {
    return this.http.patch<ISubClassOfBusiness>(
      `${this.resourceUrl}/${this.getSubClassOfBusinessIdentifier(subClassOfBusiness)}`,
      subClassOfBusiness,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubClassOfBusiness>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubClassOfBusiness[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSubClassOfBusinessIdentifier(subClassOfBusiness: Pick<ISubClassOfBusiness, 'id'>): number {
    return subClassOfBusiness.id;
  }

  compareSubClassOfBusiness(o1: Pick<ISubClassOfBusiness, 'id'> | null, o2: Pick<ISubClassOfBusiness, 'id'> | null): boolean {
    return o1 && o2 ? this.getSubClassOfBusinessIdentifier(o1) === this.getSubClassOfBusinessIdentifier(o2) : o1 === o2;
  }

  addSubClassOfBusinessToCollectionIfMissing<Type extends Pick<ISubClassOfBusiness, 'id'>>(
    subClassOfBusinessCollection: Type[],
    ...subClassOfBusinessesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const subClassOfBusinesses: Type[] = subClassOfBusinessesToCheck.filter(isPresent);
    if (subClassOfBusinesses.length > 0) {
      const subClassOfBusinessCollectionIdentifiers = subClassOfBusinessCollection.map(subClassOfBusinessItem =>
        this.getSubClassOfBusinessIdentifier(subClassOfBusinessItem),
      );
      const subClassOfBusinessesToAdd = subClassOfBusinesses.filter(subClassOfBusinessItem => {
        const subClassOfBusinessIdentifier = this.getSubClassOfBusinessIdentifier(subClassOfBusinessItem);
        if (subClassOfBusinessCollectionIdentifiers.includes(subClassOfBusinessIdentifier)) {
          return false;
        }
        subClassOfBusinessCollectionIdentifiers.push(subClassOfBusinessIdentifier);
        return true;
      });
      return [...subClassOfBusinessesToAdd, ...subClassOfBusinessCollection];
    }
    return subClassOfBusinessCollection;
  }
}
