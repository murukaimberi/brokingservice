import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'brokingserviceApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'address',
    data: { pageTitle: 'brokingserviceApp.address.home.title' },
    loadChildren: () => import('./address/address.routes'),
  },
  {
    path: 'insurance-type',
    data: { pageTitle: 'brokingserviceApp.insuranceType.home.title' },
    loadChildren: () => import('./insurance-type/insurance-type.routes'),
  },
  {
    path: 'class-of-business',
    data: { pageTitle: 'brokingserviceApp.classOfBusiness.home.title' },
    loadChildren: () => import('./class-of-business/class-of-business.routes'),
  },
  {
    path: 'sub-class-of-business',
    data: { pageTitle: 'brokingserviceApp.subClassOfBusiness.home.title' },
    loadChildren: () => import('./sub-class-of-business/sub-class-of-business.routes'),
  },
  {
    path: 'contract',
    data: { pageTitle: 'brokingserviceApp.contract.home.title' },
    loadChildren: () => import('./contract/contract.routes'),
  },
  {
    path: 'contract-report',
    data: { pageTitle: 'brokingserviceApp.contractReport.home.title' },
    loadChildren: () => import('./contract-report/contract-report.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'brokingserviceApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'contract-history',
    data: { pageTitle: 'brokingserviceApp.contractHistory.home.title' },
    loadChildren: () => import('./contract-history/contract-history.routes'),
  },
  {
    path: 'business-partner',
    data: { pageTitle: 'brokingserviceApp.businessPartner.home.title' },
    loadChildren: () => import('./business-partner/business-partner.routes'),
  },
  {
    path: 'share',
    data: { pageTitle: 'brokingserviceApp.share.home.title' },
    loadChildren: () => import('./share/share.routes'),
  },
  {
    path: 'reinsurance-placement',
    data: { pageTitle: 'brokingserviceApp.reinsurancePlacement.home.title' },
    loadChildren: () => import('./reinsurance-placement/reinsurance-placement.routes'),
  },
  {
    path: 'country',
    data: { pageTitle: 'brokingserviceApp.country.home.title' },
    loadChildren: () => import('./country/country.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
