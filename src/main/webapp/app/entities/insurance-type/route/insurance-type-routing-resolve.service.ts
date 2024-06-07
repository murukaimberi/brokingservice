import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInsuranceType } from '../insurance-type.model';
import { InsuranceTypeService } from '../service/insurance-type.service';

const insuranceTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IInsuranceType> => {
  const id = route.params['id'];
  if (id) {
    return inject(InsuranceTypeService)
      .find(id)
      .pipe(
        mergeMap((insuranceType: HttpResponse<IInsuranceType>) => {
          if (insuranceType.body) {
            return of(insuranceType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default insuranceTypeResolve;
