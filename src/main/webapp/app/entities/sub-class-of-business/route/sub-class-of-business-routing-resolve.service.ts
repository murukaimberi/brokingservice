import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubClassOfBusiness } from '../sub-class-of-business.model';
import { SubClassOfBusinessService } from '../service/sub-class-of-business.service';

const subClassOfBusinessResolve = (route: ActivatedRouteSnapshot): Observable<null | ISubClassOfBusiness> => {
  const id = route.params['id'];
  if (id) {
    return inject(SubClassOfBusinessService)
      .find(id)
      .pipe(
        mergeMap((subClassOfBusiness: HttpResponse<ISubClassOfBusiness>) => {
          if (subClassOfBusiness.body) {
            return of(subClassOfBusiness.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default subClassOfBusinessResolve;
