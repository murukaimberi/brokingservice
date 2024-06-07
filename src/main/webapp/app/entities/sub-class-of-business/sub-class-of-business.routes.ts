import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SubClassOfBusinessComponent } from './list/sub-class-of-business.component';
import { SubClassOfBusinessDetailComponent } from './detail/sub-class-of-business-detail.component';
import { SubClassOfBusinessUpdateComponent } from './update/sub-class-of-business-update.component';
import SubClassOfBusinessResolve from './route/sub-class-of-business-routing-resolve.service';

const subClassOfBusinessRoute: Routes = [
  {
    path: '',
    component: SubClassOfBusinessComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubClassOfBusinessDetailComponent,
    resolve: {
      subClassOfBusiness: SubClassOfBusinessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubClassOfBusinessUpdateComponent,
    resolve: {
      subClassOfBusiness: SubClassOfBusinessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubClassOfBusinessUpdateComponent,
    resolve: {
      subClassOfBusiness: SubClassOfBusinessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default subClassOfBusinessRoute;
