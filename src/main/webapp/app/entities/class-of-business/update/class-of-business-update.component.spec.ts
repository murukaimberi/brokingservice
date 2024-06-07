import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IInsuranceType } from 'app/entities/insurance-type/insurance-type.model';
import { InsuranceTypeService } from 'app/entities/insurance-type/service/insurance-type.service';
import { ClassOfBusinessService } from '../service/class-of-business.service';
import { IClassOfBusiness } from '../class-of-business.model';
import { ClassOfBusinessFormService } from './class-of-business-form.service';

import { ClassOfBusinessUpdateComponent } from './class-of-business-update.component';

describe('ClassOfBusiness Management Update Component', () => {
  let comp: ClassOfBusinessUpdateComponent;
  let fixture: ComponentFixture<ClassOfBusinessUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classOfBusinessFormService: ClassOfBusinessFormService;
  let classOfBusinessService: ClassOfBusinessService;
  let insuranceTypeService: InsuranceTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ClassOfBusinessUpdateComponent],
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
      .overrideTemplate(ClassOfBusinessUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassOfBusinessUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classOfBusinessFormService = TestBed.inject(ClassOfBusinessFormService);
    classOfBusinessService = TestBed.inject(ClassOfBusinessService);
    insuranceTypeService = TestBed.inject(InsuranceTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InsuranceType query and add missing value', () => {
      const classOfBusiness: IClassOfBusiness = { id: 456 };
      const insuranceType: IInsuranceType = { id: 9163 };
      classOfBusiness.insuranceType = insuranceType;

      const insuranceTypeCollection: IInsuranceType[] = [{ id: 18004 }];
      jest.spyOn(insuranceTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: insuranceTypeCollection })));
      const additionalInsuranceTypes = [insuranceType];
      const expectedCollection: IInsuranceType[] = [...additionalInsuranceTypes, ...insuranceTypeCollection];
      jest.spyOn(insuranceTypeService, 'addInsuranceTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classOfBusiness });
      comp.ngOnInit();

      expect(insuranceTypeService.query).toHaveBeenCalled();
      expect(insuranceTypeService.addInsuranceTypeToCollectionIfMissing).toHaveBeenCalledWith(
        insuranceTypeCollection,
        ...additionalInsuranceTypes.map(expect.objectContaining),
      );
      expect(comp.insuranceTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classOfBusiness: IClassOfBusiness = { id: 456 };
      const insuranceType: IInsuranceType = { id: 21416 };
      classOfBusiness.insuranceType = insuranceType;

      activatedRoute.data = of({ classOfBusiness });
      comp.ngOnInit();

      expect(comp.insuranceTypesSharedCollection).toContain(insuranceType);
      expect(comp.classOfBusiness).toEqual(classOfBusiness);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassOfBusiness>>();
      const classOfBusiness = { id: 123 };
      jest.spyOn(classOfBusinessFormService, 'getClassOfBusiness').mockReturnValue(classOfBusiness);
      jest.spyOn(classOfBusinessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classOfBusiness });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classOfBusiness }));
      saveSubject.complete();

      // THEN
      expect(classOfBusinessFormService.getClassOfBusiness).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(classOfBusinessService.update).toHaveBeenCalledWith(expect.objectContaining(classOfBusiness));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassOfBusiness>>();
      const classOfBusiness = { id: 123 };
      jest.spyOn(classOfBusinessFormService, 'getClassOfBusiness').mockReturnValue({ id: null });
      jest.spyOn(classOfBusinessService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classOfBusiness: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classOfBusiness }));
      saveSubject.complete();

      // THEN
      expect(classOfBusinessFormService.getClassOfBusiness).toHaveBeenCalled();
      expect(classOfBusinessService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassOfBusiness>>();
      const classOfBusiness = { id: 123 };
      jest.spyOn(classOfBusinessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classOfBusiness });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classOfBusinessService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInsuranceType', () => {
      it('Should forward to insuranceTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(insuranceTypeService, 'compareInsuranceType');
        comp.compareInsuranceType(entity, entity2);
        expect(insuranceTypeService.compareInsuranceType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
