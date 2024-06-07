import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ShareComponent } from './list/share.component';
import { ShareDetailComponent } from './detail/share-detail.component';
import { ShareUpdateComponent } from './update/share-update.component';
import ShareResolve from './route/share-routing-resolve.service';

const shareRoute: Routes = [
  {
    path: '',
    component: ShareComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShareDetailComponent,
    resolve: {
      share: ShareResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShareUpdateComponent,
    resolve: {
      share: ShareResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShareUpdateComponent,
    resolve: {
      share: ShareResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default shareRoute;
