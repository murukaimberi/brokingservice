import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShare, NewShare } from '../share.model';

export type PartialUpdateShare = Partial<IShare> & Pick<IShare, 'id'>;

export type EntityResponseType = HttpResponse<IShare>;
export type EntityArrayResponseType = HttpResponse<IShare[]>;

@Injectable({ providedIn: 'root' })
export class ShareService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shares');

  create(share: NewShare): Observable<EntityResponseType> {
    return this.http.post<IShare>(this.resourceUrl, share, { observe: 'response' });
  }

  update(share: IShare): Observable<EntityResponseType> {
    return this.http.put<IShare>(`${this.resourceUrl}/${this.getShareIdentifier(share)}`, share, { observe: 'response' });
  }

  partialUpdate(share: PartialUpdateShare): Observable<EntityResponseType> {
    return this.http.patch<IShare>(`${this.resourceUrl}/${this.getShareIdentifier(share)}`, share, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShare>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShare[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getShareIdentifier(share: Pick<IShare, 'id'>): number {
    return share.id;
  }

  compareShare(o1: Pick<IShare, 'id'> | null, o2: Pick<IShare, 'id'> | null): boolean {
    return o1 && o2 ? this.getShareIdentifier(o1) === this.getShareIdentifier(o2) : o1 === o2;
  }

  addShareToCollectionIfMissing<Type extends Pick<IShare, 'id'>>(
    shareCollection: Type[],
    ...sharesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shares: Type[] = sharesToCheck.filter(isPresent);
    if (shares.length > 0) {
      const shareCollectionIdentifiers = shareCollection.map(shareItem => this.getShareIdentifier(shareItem));
      const sharesToAdd = shares.filter(shareItem => {
        const shareIdentifier = this.getShareIdentifier(shareItem);
        if (shareCollectionIdentifiers.includes(shareIdentifier)) {
          return false;
        }
        shareCollectionIdentifiers.push(shareIdentifier);
        return true;
      });
      return [...sharesToAdd, ...shareCollection];
    }
    return shareCollection;
  }
}
