import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ContractReportComponent } from './list/contract-report.component';
import { ContractReportDetailComponent } from './detail/contract-report-detail.component';
import { ContractReportUpdateComponent } from './update/contract-report-update.component';
import ContractReportResolve from './route/contract-report-routing-resolve.service';

const contractReportRoute: Routes = [
  {
    path: '',
    component: ContractReportComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContractReportDetailComponent,
    resolve: {
      contractReport: ContractReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContractReportUpdateComponent,
    resolve: {
      contractReport: ContractReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContractReportUpdateComponent,
    resolve: {
      contractReport: ContractReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contractReportRoute;
