import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ContractHistoryComponent } from './list/contract-history.component';
import { ContractHistoryDetailComponent } from './detail/contract-history-detail.component';
import { ContractHistoryUpdateComponent } from './update/contract-history-update.component';
import ContractHistoryResolve from './route/contract-history-routing-resolve.service';

const contractHistoryRoute: Routes = [
  {
    path: '',
    component: ContractHistoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContractHistoryDetailComponent,
    resolve: {
      contractHistory: ContractHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContractHistoryUpdateComponent,
    resolve: {
      contractHistory: ContractHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContractHistoryUpdateComponent,
    resolve: {
      contractHistory: ContractHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contractHistoryRoute;
