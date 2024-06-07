import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contract-history.test-samples';

import { ContractHistoryFormService } from './contract-history-form.service';

describe('ContractHistory Form Service', () => {
  let service: ContractHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContractHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createContractHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContractHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contractCreatedDate: expect.any(Object),
            contractActiveDate: expect.any(Object),
            contractInActiveDate: expect.any(Object),
            contractLastModifiedDate: expect.any(Object),
            changeDescription: expect.any(Object),
            updated: expect.any(Object),
            approved: expect.any(Object),
          }),
        );
      });

      it('passing IContractHistory should create a new form with FormGroup', () => {
        const formGroup = service.createContractHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contractCreatedDate: expect.any(Object),
            contractActiveDate: expect.any(Object),
            contractInActiveDate: expect.any(Object),
            contractLastModifiedDate: expect.any(Object),
            changeDescription: expect.any(Object),
            updated: expect.any(Object),
            approved: expect.any(Object),
          }),
        );
      });
    });

    describe('getContractHistory', () => {
      it('should return NewContractHistory for default ContractHistory initial value', () => {
        const formGroup = service.createContractHistoryFormGroup(sampleWithNewData);

        const contractHistory = service.getContractHistory(formGroup) as any;

        expect(contractHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewContractHistory for empty ContractHistory initial value', () => {
        const formGroup = service.createContractHistoryFormGroup();

        const contractHistory = service.getContractHistory(formGroup) as any;

        expect(contractHistory).toMatchObject({});
      });

      it('should return IContractHistory', () => {
        const formGroup = service.createContractHistoryFormGroup(sampleWithRequiredData);

        const contractHistory = service.getContractHistory(formGroup) as any;

        expect(contractHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContractHistory should not enable id FormControl', () => {
        const formGroup = service.createContractHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContractHistory should disable id FormControl', () => {
        const formGroup = service.createContractHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
