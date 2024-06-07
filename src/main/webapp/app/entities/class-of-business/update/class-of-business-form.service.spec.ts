import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../class-of-business.test-samples';

import { ClassOfBusinessFormService } from './class-of-business-form.service';

describe('ClassOfBusiness Form Service', () => {
  let service: ClassOfBusinessFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClassOfBusinessFormService);
  });

  describe('Service methods', () => {
    describe('createClassOfBusinessFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClassOfBusinessFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            insuranceType: expect.any(Object),
          }),
        );
      });

      it('passing IClassOfBusiness should create a new form with FormGroup', () => {
        const formGroup = service.createClassOfBusinessFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            insuranceType: expect.any(Object),
          }),
        );
      });
    });

    describe('getClassOfBusiness', () => {
      it('should return NewClassOfBusiness for default ClassOfBusiness initial value', () => {
        const formGroup = service.createClassOfBusinessFormGroup(sampleWithNewData);

        const classOfBusiness = service.getClassOfBusiness(formGroup) as any;

        expect(classOfBusiness).toMatchObject(sampleWithNewData);
      });

      it('should return NewClassOfBusiness for empty ClassOfBusiness initial value', () => {
        const formGroup = service.createClassOfBusinessFormGroup();

        const classOfBusiness = service.getClassOfBusiness(formGroup) as any;

        expect(classOfBusiness).toMatchObject({});
      });

      it('should return IClassOfBusiness', () => {
        const formGroup = service.createClassOfBusinessFormGroup(sampleWithRequiredData);

        const classOfBusiness = service.getClassOfBusiness(formGroup) as any;

        expect(classOfBusiness).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IClassOfBusiness should not enable id FormControl', () => {
        const formGroup = service.createClassOfBusinessFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewClassOfBusiness should disable id FormControl', () => {
        const formGroup = service.createClassOfBusinessFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
