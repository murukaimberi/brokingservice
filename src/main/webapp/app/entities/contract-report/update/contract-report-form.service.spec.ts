import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contract-report.test-samples';

import { ContractReportFormService } from './contract-report-form.service';

describe('ContractReport Form Service', () => {
  let service: ContractReportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContractReportFormService);
  });

  describe('Service methods', () => {
    describe('createContractReportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContractReportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contractDocument: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IContractReport should create a new form with FormGroup', () => {
        const formGroup = service.createContractReportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contractDocument: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getContractReport', () => {
      it('should return NewContractReport for default ContractReport initial value', () => {
        const formGroup = service.createContractReportFormGroup(sampleWithNewData);

        const contractReport = service.getContractReport(formGroup) as any;

        expect(contractReport).toMatchObject(sampleWithNewData);
      });

      it('should return NewContractReport for empty ContractReport initial value', () => {
        const formGroup = service.createContractReportFormGroup();

        const contractReport = service.getContractReport(formGroup) as any;

        expect(contractReport).toMatchObject({});
      });

      it('should return IContractReport', () => {
        const formGroup = service.createContractReportFormGroup(sampleWithRequiredData);

        const contractReport = service.getContractReport(formGroup) as any;

        expect(contractReport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContractReport should not enable id FormControl', () => {
        const formGroup = service.createContractReportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContractReport should disable id FormControl', () => {
        const formGroup = service.createContractReportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
