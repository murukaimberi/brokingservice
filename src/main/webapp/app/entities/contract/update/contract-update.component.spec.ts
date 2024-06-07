import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IBusinessPartner } from 'app/entities/business-partner/business-partner.model';
import { BusinessPartnerService } from 'app/entities/business-partner/service/business-partner.service';
import { IClassOfBusiness } from 'app/entities/class-of-business/class-of-business.model';
import { ClassOfBusinessService } from 'app/entities/class-of-business/service/class-of-business.service';
import { ISubClassOfBusiness } from 'app/entities/sub-class-of-business/sub-class-of-business.model';
import { SubClassOfBusinessService } from 'app/entities/sub-class-of-business/service/sub-class-of-business.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';
import { ContractFormService } from './contract-form.service';

import { ContractUpdateComponent } from './contract-update.component';

describe('Contract Management Update Component', () => {
  let comp: ContractUpdateComponent;
  let fixture: ComponentFixture<ContractUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractFormService: ContractFormService;
  let contractService: ContractService;
  let businessPartnerService: BusinessPartnerService;
  let classOfBusinessService: ClassOfBusinessService;
  let subClassOfBusinessService: SubClassOfBusinessService;
  let countryService: CountryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ContractUpdateComponent],
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
      .overrideTemplate(ContractUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractFormService = TestBed.inject(ContractFormService);
    contractService = TestBed.inject(ContractService);
    businessPartnerService = TestBed.inject(BusinessPartnerService);
    classOfBusinessService = TestBed.inject(ClassOfBusinessService);
    subClassOfBusinessService = TestBed.inject(SubClassOfBusinessService);
    countryService = TestBed.inject(CountryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BusinessPartner query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const insured: IBusinessPartner = { id: 3711 };
      contract.insured = insured;
      const insurer: IBusinessPartner = { id: 3607 };
      contract.insurer = insurer;
      const broker: IBusinessPartner = { id: 13874 };
      contract.broker = broker;
      const reinsurers: IBusinessPartner[] = [{ id: 16866 }];
      contract.reinsurers = reinsurers;

      const businessPartnerCollection: IBusinessPartner[] = [{ id: 23066 }];
      jest.spyOn(businessPartnerService, 'query').mockReturnValue(of(new HttpResponse({ body: businessPartnerCollection })));
      const additionalBusinessPartners = [insured, insurer, broker, ...reinsurers];
      const expectedCollection: IBusinessPartner[] = [...additionalBusinessPartners, ...businessPartnerCollection];
      jest.spyOn(businessPartnerService, 'addBusinessPartnerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(businessPartnerService.query).toHaveBeenCalled();
      expect(businessPartnerService.addBusinessPartnerToCollectionIfMissing).toHaveBeenCalledWith(
        businessPartnerCollection,
        ...additionalBusinessPartners.map(expect.objectContaining),
      );
      expect(comp.businessPartnersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ClassOfBusiness query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const classOfBusiness: IClassOfBusiness = { id: 2215 };
      contract.classOfBusiness = classOfBusiness;

      const classOfBusinessCollection: IClassOfBusiness[] = [{ id: 22817 }];
      jest.spyOn(classOfBusinessService, 'query').mockReturnValue(of(new HttpResponse({ body: classOfBusinessCollection })));
      const additionalClassOfBusinesses = [classOfBusiness];
      const expectedCollection: IClassOfBusiness[] = [...additionalClassOfBusinesses, ...classOfBusinessCollection];
      jest.spyOn(classOfBusinessService, 'addClassOfBusinessToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(classOfBusinessService.query).toHaveBeenCalled();
      expect(classOfBusinessService.addClassOfBusinessToCollectionIfMissing).toHaveBeenCalledWith(
        classOfBusinessCollection,
        ...additionalClassOfBusinesses.map(expect.objectContaining),
      );
      expect(comp.classOfBusinessesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SubClassOfBusiness query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const subClassOfBusiness: ISubClassOfBusiness = { id: 2294 };
      contract.subClassOfBusiness = subClassOfBusiness;

      const subClassOfBusinessCollection: ISubClassOfBusiness[] = [{ id: 30786 }];
      jest.spyOn(subClassOfBusinessService, 'query').mockReturnValue(of(new HttpResponse({ body: subClassOfBusinessCollection })));
      const additionalSubClassOfBusinesses = [subClassOfBusiness];
      const expectedCollection: ISubClassOfBusiness[] = [...additionalSubClassOfBusinesses, ...subClassOfBusinessCollection];
      jest.spyOn(subClassOfBusinessService, 'addSubClassOfBusinessToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(subClassOfBusinessService.query).toHaveBeenCalled();
      expect(subClassOfBusinessService.addSubClassOfBusinessToCollectionIfMissing).toHaveBeenCalledWith(
        subClassOfBusinessCollection,
        ...additionalSubClassOfBusinesses.map(expect.objectContaining),
      );
      expect(comp.subClassOfBusinessesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Country query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const country: ICountry = { id: 23541 };
      contract.country = country;

      const countryCollection: ICountry[] = [{ id: 5189 }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contract: IContract = { id: 456 };
      const insured: IBusinessPartner = { id: 17083 };
      contract.insured = insured;
      const insurer: IBusinessPartner = { id: 20876 };
      contract.insurer = insurer;
      const broker: IBusinessPartner = { id: 14453 };
      contract.broker = broker;
      const reinsurers: IBusinessPartner = { id: 1654 };
      contract.reinsurers = [reinsurers];
      const classOfBusiness: IClassOfBusiness = { id: 24660 };
      contract.classOfBusiness = classOfBusiness;
      const subClassOfBusiness: ISubClassOfBusiness = { id: 29172 };
      contract.subClassOfBusiness = subClassOfBusiness;
      const country: ICountry = { id: 32089 };
      contract.country = country;

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(comp.businessPartnersSharedCollection).toContain(insured);
      expect(comp.businessPartnersSharedCollection).toContain(insurer);
      expect(comp.businessPartnersSharedCollection).toContain(broker);
      expect(comp.businessPartnersSharedCollection).toContain(reinsurers);
      expect(comp.classOfBusinessesSharedCollection).toContain(classOfBusiness);
      expect(comp.subClassOfBusinessesSharedCollection).toContain(subClassOfBusiness);
      expect(comp.countriesSharedCollection).toContain(country);
      expect(comp.contract).toEqual(contract);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue(contract);
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractService.update).toHaveBeenCalledWith(expect.objectContaining(contract));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue({ id: null });
      jest.spyOn(contractService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(contractService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractService.update).toHaveBeenCalled();
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

    describe('compareClassOfBusiness', () => {
      it('Should forward to classOfBusinessService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(classOfBusinessService, 'compareClassOfBusiness');
        comp.compareClassOfBusiness(entity, entity2);
        expect(classOfBusinessService.compareClassOfBusiness).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSubClassOfBusiness', () => {
      it('Should forward to subClassOfBusinessService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(subClassOfBusinessService, 'compareSubClassOfBusiness');
        comp.compareSubClassOfBusiness(entity, entity2);
        expect(subClassOfBusinessService.compareSubClassOfBusiness).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCountry', () => {
      it('Should forward to countryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(countryService, 'compareCountry');
        comp.compareCountry(entity, entity2);
        expect(countryService.compareCountry).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
