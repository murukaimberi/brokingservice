import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IBusinessPartner } from 'app/entities/business-partner/business-partner.model';
import { BusinessPartnerService } from 'app/entities/business-partner/service/business-partner.service';
import { AddressService } from '../service/address.service';
import { IAddress } from '../address.model';
import { AddressFormService } from './address-form.service';

import { AddressUpdateComponent } from './address-update.component';

describe('Address Management Update Component', () => {
  let comp: AddressUpdateComponent;
  let fixture: ComponentFixture<AddressUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let addressFormService: AddressFormService;
  let addressService: AddressService;
  let businessPartnerService: BusinessPartnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AddressUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AddressUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AddressUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    addressFormService = TestBed.inject(AddressFormService);
    addressService = TestBed.inject(AddressService);
    businessPartnerService = TestBed.inject(BusinessPartnerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BusinessPartner query and add missing value', () => {
      const address: IAddress = { id: 456 };
      const cedent: IBusinessPartner = { id: 8593 };
      address.cedent = cedent;

      const businessPartnerCollection: IBusinessPartner[] = [{ id: 6143 }];
      jest.spyOn(businessPartnerService, 'query').mockReturnValue(of(new HttpResponse({ body: businessPartnerCollection })));
      const additionalBusinessPartners = [cedent];
      const expectedCollection: IBusinessPartner[] = [...additionalBusinessPartners, ...businessPartnerCollection];
      jest.spyOn(businessPartnerService, 'addBusinessPartnerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ address });
      comp.ngOnInit();

      expect(businessPartnerService.query).toHaveBeenCalled();
      expect(businessPartnerService.addBusinessPartnerToCollectionIfMissing).toHaveBeenCalledWith(
        businessPartnerCollection,
        ...additionalBusinessPartners.map(expect.objectContaining),
      );
      expect(comp.businessPartnersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const address: IAddress = { id: 456 };
      const cedent: IBusinessPartner = { id: 10040 };
      address.cedent = cedent;

      activatedRoute.data = of({ address });
      comp.ngOnInit();

      expect(comp.businessPartnersSharedCollection).toContain(cedent);
      expect(comp.address).toEqual(address);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddress>>();
      const address = { id: 123 };
      jest.spyOn(addressFormService, 'getAddress').mockReturnValue(address);
      jest.spyOn(addressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ address });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: address }));
      saveSubject.complete();

      // THEN
      expect(addressFormService.getAddress).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(addressService.update).toHaveBeenCalledWith(expect.objectContaining(address));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddress>>();
      const address = { id: 123 };
      jest.spyOn(addressFormService, 'getAddress').mockReturnValue({ id: null });
      jest.spyOn(addressService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ address: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: address }));
      saveSubject.complete();

      // THEN
      expect(addressFormService.getAddress).toHaveBeenCalled();
      expect(addressService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddress>>();
      const address = { id: 123 };
      jest.spyOn(addressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ address });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(addressService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBusinessPartner', () => {
      it('Should forward to businessPartnerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(businessPartnerService, 'compareBusinessPartner');
        comp.compareBusinessPartner(entity, entity2);
        expect(businessPartnerService.compareBusinessPartner).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
