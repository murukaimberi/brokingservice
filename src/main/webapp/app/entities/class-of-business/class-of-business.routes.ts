import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ClassOfBusinessComponent } from './list/class-of-business.component';
import { ClassOfBusinessDetailComponent } from './detail/class-of-business-detail.component';
import { ClassOfBusinessUpdateComponent } from './update/class-of-business-update.component';
import ClassOfBusinessResolve from './route/class-of-business-routing-resolve.service';

const classOfBusinessRoute: Routes = [
  {
    path: '',
    component: ClassOfBusinessComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassOfBusinessDetailComponent,
    resolve: {
      classOfBusiness: ClassOfBusinessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassOfBusinessUpdateComponent,
    resolve: {
      classOfBusiness: ClassOfBusinessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassOfBusinessUpdateComponent,
    resolve: {
      classOfBusiness: ClassOfBusinessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default classOfBusinessRoute;
