import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShare } from '../share.model';
import { ShareService } from '../service/share.service';

const shareResolve = (route: ActivatedRouteSnapshot): Observable<null | IShare> => {
  const id = route.params['id'];
  if (id) {
    return inject(ShareService)
      .find(id)
      .pipe(
        mergeMap((share: HttpResponse<IShare>) => {
          if (share.body) {
            return of(share.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default shareResolve;
