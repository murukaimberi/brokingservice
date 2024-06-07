import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../share.test-samples';

import { ShareFormService } from './share-form.service';

describe('Share Form Service', () => {
  let service: ShareFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShareFormService);
  });

  describe('Service methods', () => {
    describe('createShareFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShareFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sharePercentage: expect.any(Object),
            totalSumInsured: expect.any(Object),
            limitOfLiability: expect.any(Object),
            grossPremium: expect.any(Object),
            riCommission: expect.any(Object),
            netPremium: expect.any(Object),
            brokerage: expect.any(Object),
            brokerageAmount: expect.any(Object),
            netPayable: expect.any(Object),
            reInsurer: expect.any(Object),
          }),
        );
      });

      it('passing IShare should create a new form with FormGroup', () => {
        const formGroup = service.createShareFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sharePercentage: expect.any(Object),
            totalSumInsured: expect.any(Object),
            limitOfLiability: expect.any(Object),
            grossPremium: expect.any(Object),
            riCommission: expect.any(Object),
            netPremium: expect.any(Object),
            brokerage: expect.any(Object),
            brokerageAmount: expect.any(Object),
            netPayable: expect.any(Object),
            reInsurer: expect.any(Object),
          }),
        );
      });
    });

    describe('getShare', () => {
      it('should return NewShare for default Share initial value', () => {
        const formGroup = service.createShareFormGroup(sampleWithNewData);

        const share = service.getShare(formGroup) as any;

        expect(share).toMatchObject(sampleWithNewData);
      });

      it('should return NewShare for empty Share initial value', () => {
        const formGroup = service.createShareFormGroup();

        const share = service.getShare(formGroup) as any;

        expect(share).toMatchObject({});
      });

      it('should return IShare', () => {
        const formGroup = service.createShareFormGroup(sampleWithRequiredData);

        const share = service.getShare(formGroup) as any;

        expect(share).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShare should not enable id FormControl', () => {
        const formGroup = service.createShareFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShare should disable id FormControl', () => {
        const formGroup = service.createShareFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
