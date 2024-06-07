import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassOfBusiness, NewClassOfBusiness } from '../class-of-business.model';

export type PartialUpdateClassOfBusiness = Partial<IClassOfBusiness> & Pick<IClassOfBusiness, 'id'>;

export type EntityResponseType = HttpResponse<IClassOfBusiness>;
export type EntityArrayResponseType = HttpResponse<IClassOfBusiness[]>;

@Injectable({ providedIn: 'root' })
export class ClassOfBusinessService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/class-of-businesses');

  create(classOfBusiness: NewClassOfBusiness): Observable<EntityResponseType> {
    return this.http.post<IClassOfBusiness>(this.resourceUrl, classOfBusiness, { observe: 'response' });
  }

  update(classOfBusiness: IClassOfBusiness): Observable<EntityResponseType> {
    return this.http.put<IClassOfBusiness>(`${this.resourceUrl}/${this.getClassOfBusinessIdentifier(classOfBusiness)}`, classOfBusiness, {
      observe: 'response',
    });
  }

  partialUpdate(classOfBusiness: PartialUpdateClassOfBusiness): Observable<EntityResponseType> {
    return this.http.patch<IClassOfBusiness>(`${this.resourceUrl}/${this.getClassOfBusinessIdentifier(classOfBusiness)}`, classOfBusiness, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassOfBusiness>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassOfBusiness[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClassOfBusinessIdentifier(classOfBusiness: Pick<IClassOfBusiness, 'id'>): number {
    return classOfBusiness.id;
  }

  compareClassOfBusiness(o1: Pick<IClassOfBusiness, 'id'> | null, o2: Pick<IClassOfBusiness, 'id'> | null): boolean {
    return o1 && o2 ? this.getClassOfBusinessIdentifier(o1) === this.getClassOfBusinessIdentifier(o2) : o1 === o2;
  }

  addClassOfBusinessToCollectionIfMissing<Type extends Pick<IClassOfBusiness, 'id'>>(
    classOfBusinessCollection: Type[],
    ...classOfBusinessesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const classOfBusinesses: Type[] = classOfBusinessesToCheck.filter(isPresent);
    if (classOfBusinesses.length > 0) {
      const classOfBusinessCollectionIdentifiers = classOfBusinessCollection.map(classOfBusinessItem =>
        this.getClassOfBusinessIdentifier(classOfBusinessItem),
      );
      const classOfBusinessesToAdd = classOfBusinesses.filter(classOfBusinessItem => {
        const classOfBusinessIdentifier = this.getClassOfBusinessIdentifier(classOfBusinessItem);
        if (classOfBusinessCollectionIdentifiers.includes(classOfBusinessIdentifier)) {
          return false;
        }
        classOfBusinessCollectionIdentifiers.push(classOfBusinessIdentifier);
        return true;
      });
      return [...classOfBusinessesToAdd, ...classOfBusinessCollection];
    }
    return classOfBusinessCollection;
  }
}
