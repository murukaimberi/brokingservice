import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReinsurancePlacement } from '../reinsurance-placement.model';
import { ReinsurancePlacementService } from '../service/reinsurance-placement.service';

const reinsurancePlacementResolve = (route: ActivatedRouteSnapshot): Observable<null | IReinsurancePlacement> => {
  const id = route.params['id'];
  if (id) {
    return inject(ReinsurancePlacementService)
      .find(id)
      .pipe(
        mergeMap((reinsurancePlacement: HttpResponse<IReinsurancePlacement>) => {
          if (reinsurancePlacement.body) {
            return of(reinsurancePlacement.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default reinsurancePlacementResolve;
