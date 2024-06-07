import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../reinsurance-placement.test-samples';

import { ReinsurancePlacementFormService } from './reinsurance-placement-form.service';

describe('ReinsurancePlacement Form Service', () => {
  let service: ReinsurancePlacementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReinsurancePlacementFormService);
  });

  describe('Service methods', () => {
    describe('createReinsurancePlacementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReinsurancePlacementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            riPercentageCommission: expect.any(Object),
            riPercentageShare: expect.any(Object),
            riTotalSumInsured: expect.any(Object),
            riLimitOfLiability: expect.any(Object),
            grossPremiumHundred: expect.any(Object),
            riPremium: expect.any(Object),
            riCommission: expect.any(Object),
            netDueFromInsurer: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });

      it('passing IReinsurancePlacement should create a new form with FormGroup', () => {
        const formGroup = service.createReinsurancePlacementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            riPercentageCommission: expect.any(Object),
            riPercentageShare: expect.any(Object),
            riTotalSumInsured: expect.any(Object),
            riLimitOfLiability: expect.any(Object),
            grossPremiumHundred: expect.any(Object),
            riPremium: expect.any(Object),
            riCommission: expect.any(Object),
            netDueFromInsurer: expect.any(Object),
            contract: expect.any(Object),
          }),
        );
      });
    });

    describe('getReinsurancePlacement', () => {
      it('should return NewReinsurancePlacement for default ReinsurancePlacement initial value', () => {
        const formGroup = service.createReinsurancePlacementFormGroup(sampleWithNewData);

        const reinsurancePlacement = service.getReinsurancePlacement(formGroup) as any;

        expect(reinsurancePlacement).toMatchObject(sampleWithNewData);
      });

      it('should return NewReinsurancePlacement for empty ReinsurancePlacement initial value', () => {
        const formGroup = service.createReinsurancePlacementFormGroup();

        const reinsurancePlacement = service.getReinsurancePlacement(formGroup) as any;

        expect(reinsurancePlacement).toMatchObject({});
      });

      it('should return IReinsurancePlacement', () => {
        const formGroup = service.createReinsurancePlacementFormGroup(sampleWithRequiredData);

        const reinsurancePlacement = service.getReinsurancePlacement(formGroup) as any;

        expect(reinsurancePlacement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReinsurancePlacement should not enable id FormControl', () => {
        const formGroup = service.createReinsurancePlacementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReinsurancePlacement should disable id FormControl', () => {
        const formGroup = service.createReinsurancePlacementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
