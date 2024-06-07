import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContractHistory } from '../contract-history.model';
import { ContractHistoryService } from '../service/contract-history.service';

const contractHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IContractHistory> => {
  const id = route.params['id'];
  if (id) {
    return inject(ContractHistoryService)
      .find(id)
      .pipe(
        mergeMap((contractHistory: HttpResponse<IContractHistory>) => {
          if (contractHistory.body) {
            return of(contractHistory.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default contractHistoryResolve;
