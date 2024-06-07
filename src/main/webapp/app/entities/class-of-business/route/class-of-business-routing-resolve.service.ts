import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassOfBusiness } from '../class-of-business.model';
import { ClassOfBusinessService } from '../service/class-of-business.service';

const classOfBusinessResolve = (route: ActivatedRouteSnapshot): Observable<null | IClassOfBusiness> => {
  const id = route.params['id'];
  if (id) {
    return inject(ClassOfBusinessService)
      .find(id)
      .pipe(
        mergeMap((classOfBusiness: HttpResponse<IClassOfBusiness>) => {
          if (classOfBusiness.body) {
            return of(classOfBusiness.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default classOfBusinessResolve;
