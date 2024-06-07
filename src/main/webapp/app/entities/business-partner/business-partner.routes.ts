import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BusinessPartnerComponent } from './list/business-partner.component';
import { BusinessPartnerDetailComponent } from './detail/business-partner-detail.component';
import { BusinessPartnerUpdateComponent } from './update/business-partner-update.component';
import BusinessPartnerResolve from './route/business-partner-routing-resolve.service';

const businessPartnerRoute: Routes = [
  {
    path: '',
    component: BusinessPartnerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessPartnerDetailComponent,
    resolve: {
      businessPartner: BusinessPartnerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessPartnerUpdateComponent,
    resolve: {
      businessPartner: BusinessPartnerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessPartnerUpdateComponent,
    resolve: {
      businessPartner: BusinessPartnerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default businessPartnerRoute;
