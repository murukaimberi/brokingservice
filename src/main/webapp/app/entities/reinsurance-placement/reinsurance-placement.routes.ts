import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ReinsurancePlacementComponent } from './list/reinsurance-placement.component';
import { ReinsurancePlacementDetailComponent } from './detail/reinsurance-placement-detail.component';
import { ReinsurancePlacementUpdateComponent } from './update/reinsurance-placement-update.component';
import ReinsurancePlacementResolve from './route/reinsurance-placement-routing-resolve.service';

const reinsurancePlacementRoute: Routes = [
  {
    path: '',
    component: ReinsurancePlacementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReinsurancePlacementDetailComponent,
    resolve: {
      reinsurancePlacement: ReinsurancePlacementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReinsurancePlacementUpdateComponent,
    resolve: {
      reinsurancePlacement: ReinsurancePlacementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReinsurancePlacementUpdateComponent,
    resolve: {
      reinsurancePlacement: ReinsurancePlacementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reinsurancePlacementRoute;
