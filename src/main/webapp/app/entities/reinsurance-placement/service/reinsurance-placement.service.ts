import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReinsurancePlacement, NewReinsurancePlacement } from '../reinsurance-placement.model';

export type PartialUpdateReinsurancePlacement = Partial<IReinsurancePlacement> & Pick<IReinsurancePlacement, 'id'>;

export type EntityResponseType = HttpResponse<IReinsurancePlacement>;
export type EntityArrayResponseType = HttpResponse<IReinsurancePlacement[]>;

@Injectable({ providedIn: 'root' })
export class ReinsurancePlacementService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reinsurance-placements');

  create(reinsurancePlacement: NewReinsurancePlacement): Observable<EntityResponseType> {
    return this.http.post<IReinsurancePlacement>(this.resourceUrl, reinsurancePlacement, { observe: 'response' });
  }

  update(reinsurancePlacement: IReinsurancePlacement): Observable<EntityResponseType> {
    return this.http.put<IReinsurancePlacement>(
      `${this.resourceUrl}/${this.getReinsurancePlacementIdentifier(reinsurancePlacement)}`,
      reinsurancePlacement,
      { observe: 'response' },
    );
  }

  partialUpdate(reinsurancePlacement: PartialUpdateReinsurancePlacement): Observable<EntityResponseType> {
    return this.http.patch<IReinsurancePlacement>(
      `${this.resourceUrl}/${this.getReinsurancePlacementIdentifier(reinsurancePlacement)}`,
      reinsurancePlacement,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReinsurancePlacement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReinsurancePlacement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReinsurancePlacementIdentifier(reinsurancePlacement: Pick<IReinsurancePlacement, 'id'>): number {
    return reinsurancePlacement.id;
  }

  compareReinsurancePlacement(o1: Pick<IReinsurancePlacement, 'id'> | null, o2: Pick<IReinsurancePlacement, 'id'> | null): boolean {
    return o1 && o2 ? this.getReinsurancePlacementIdentifier(o1) === this.getReinsurancePlacementIdentifier(o2) : o1 === o2;
  }

  addReinsurancePlacementToCollectionIfMissing<Type extends Pick<IReinsurancePlacement, 'id'>>(
    reinsurancePlacementCollection: Type[],
    ...reinsurancePlacementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reinsurancePlacements: Type[] = reinsurancePlacementsToCheck.filter(isPresent);
    if (reinsurancePlacements.length > 0) {
      const reinsurancePlacementCollectionIdentifiers = reinsurancePlacementCollection.map(reinsurancePlacementItem =>
        this.getReinsurancePlacementIdentifier(reinsurancePlacementItem),
      );
      const reinsurancePlacementsToAdd = reinsurancePlacements.filter(reinsurancePlacementItem => {
        const reinsurancePlacementIdentifier = this.getReinsurancePlacementIdentifier(reinsurancePlacementItem);
        if (reinsurancePlacementCollectionIdentifiers.includes(reinsurancePlacementIdentifier)) {
          return false;
        }
        reinsurancePlacementCollectionIdentifiers.push(reinsurancePlacementIdentifier);
        return true;
      });
      return [...reinsurancePlacementsToAdd, ...reinsurancePlacementCollection];
    }
    return reinsurancePlacementCollection;
  }
}
