import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContractReport } from '../contract-report.model';
import { ContractReportService } from '../service/contract-report.service';

const contractReportResolve = (route: ActivatedRouteSnapshot): Observable<null | IContractReport> => {
  const id = route.params['id'];
  if (id) {
    return inject(ContractReportService)
      .find(id)
      .pipe(
        mergeMap((contractReport: HttpResponse<IContractReport>) => {
          if (contractReport.body) {
            return of(contractReport.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default contractReportResolve;
