import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../insurance-type.test-samples';

import { InsuranceTypeFormService } from './insurance-type-form.service';

describe('InsuranceType Form Service', () => {
  let service: InsuranceTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InsuranceTypeFormService);
  });

  describe('Service methods', () => {
    describe('createInsuranceTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInsuranceTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IInsuranceType should create a new form with FormGroup', () => {
        const formGroup = service.createInsuranceTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getInsuranceType', () => {
      it('should return NewInsuranceType for default InsuranceType initial value', () => {
        const formGroup = service.createInsuranceTypeFormGroup(sampleWithNewData);

        const insuranceType = service.getInsuranceType(formGroup) as any;

        expect(insuranceType).toMatchObject(sampleWithNewData);
      });

      it('should return NewInsuranceType for empty InsuranceType initial value', () => {
        const formGroup = service.createInsuranceTypeFormGroup();

        const insuranceType = service.getInsuranceType(formGroup) as any;

        expect(insuranceType).toMatchObject({});
      });

      it('should return IInsuranceType', () => {
        const formGroup = service.createInsuranceTypeFormGroup(sampleWithRequiredData);

        const insuranceType = service.getInsuranceType(formGroup) as any;

        expect(insuranceType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInsuranceType should not enable id FormControl', () => {
        const formGroup = service.createInsuranceTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInsuranceType should disable id FormControl', () => {
        const formGroup = service.createInsuranceTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
