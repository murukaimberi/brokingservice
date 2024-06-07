import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { InsuranceTypeService } from '../service/insurance-type.service';
import { IInsuranceType } from '../insurance-type.model';
import { InsuranceTypeFormService } from './insurance-type-form.service';

import { InsuranceTypeUpdateComponent } from './insurance-type-update.component';

describe('InsuranceType Management Update Component', () => {
  let comp: InsuranceTypeUpdateComponent;
  let fixture: ComponentFixture<InsuranceTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let insuranceTypeFormService: InsuranceTypeFormService;
  let insuranceTypeService: InsuranceTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, InsuranceTypeUpdateComponent],
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
      .overrideTemplate(InsuranceTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InsuranceTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    insuranceTypeFormService = TestBed.inject(InsuranceTypeFormService);
    insuranceTypeService = TestBed.inject(InsuranceTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const insuranceType: IInsuranceType = { id: 456 };

      activatedRoute.data = of({ insuranceType });
      comp.ngOnInit();

      expect(comp.insuranceType).toEqual(insuranceType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceType>>();
      const insuranceType = { id: 123 };
      jest.spyOn(insuranceTypeFormService, 'getInsuranceType').mockReturnValue(insuranceType);
      jest.spyOn(insuranceTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceType }));
      saveSubject.complete();

      // THEN
      expect(insuranceTypeFormService.getInsuranceType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(insuranceTypeService.update).toHaveBeenCalledWith(expect.objectContaining(insuranceType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceType>>();
      const insuranceType = { id: 123 };
      jest.spyOn(insuranceTypeFormService, 'getInsuranceType').mockReturnValue({ id: null });
      jest.spyOn(insuranceTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceType }));
      saveSubject.complete();

      // THEN
      expect(insuranceTypeFormService.getInsuranceType).toHaveBeenCalled();
      expect(insuranceTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceType>>();
      const insuranceType = { id: 123 };
      jest.spyOn(insuranceTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(insuranceTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
