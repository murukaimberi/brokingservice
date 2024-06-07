import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusinessPartner } from '../business-partner.model';
import { BusinessPartnerService } from '../service/business-partner.service';

const businessPartnerResolve = (route: ActivatedRouteSnapshot): Observable<null | IBusinessPartner> => {
  const id = route.params['id'];
  if (id) {
    return inject(BusinessPartnerService)
      .find(id)
      .pipe(
        mergeMap((businessPartner: HttpResponse<IBusinessPartner>) => {
          if (businessPartner.body) {
            return of(businessPartner.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default businessPartnerResolve;
