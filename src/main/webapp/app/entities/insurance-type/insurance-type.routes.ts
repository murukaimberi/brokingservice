import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InsuranceTypeComponent } from './list/insurance-type.component';
import { InsuranceTypeDetailComponent } from './detail/insurance-type-detail.component';
import { InsuranceTypeUpdateComponent } from './update/insurance-type-update.component';
import InsuranceTypeResolve from './route/insurance-type-routing-resolve.service';

const insuranceTypeRoute: Routes = [
  {
    path: '',
    component: InsuranceTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InsuranceTypeDetailComponent,
    resolve: {
      insuranceType: InsuranceTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InsuranceTypeUpdateComponent,
    resolve: {
      insuranceType: InsuranceTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InsuranceTypeUpdateComponent,
    resolve: {
      insuranceType: InsuranceTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default insuranceTypeRoute;
