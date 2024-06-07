import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sub-class-of-business.test-samples';

import { SubClassOfBusinessFormService } from './sub-class-of-business-form.service';

describe('SubClassOfBusiness Form Service', () => {
  let service: SubClassOfBusinessFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubClassOfBusinessFormService);
  });

  describe('Service methods', () => {
    describe('createSubClassOfBusinessFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubClassOfBusinessFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            classOfBusiness: expect.any(Object),
          }),
        );
      });

      it('passing ISubClassOfBusiness should create a new form with FormGroup', () => {
        const formGroup = service.createSubClassOfBusinessFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            classOfBusiness: expect.any(Object),
          }),
        );
      });
    });

    describe('getSubClassOfBusiness', () => {
      it('should return NewSubClassOfBusiness for default SubClassOfBusiness initial value', () => {
        const formGroup = service.createSubClassOfBusinessFormGroup(sampleWithNewData);

        const subClassOfBusiness = service.getSubClassOfBusiness(formGroup) as any;

        expect(subClassOfBusiness).toMatchObject(sampleWithNewData);
      });

      it('should return NewSubClassOfBusiness for empty SubClassOfBusiness initial value', () => {
        const formGroup = service.createSubClassOfBusinessFormGroup();

        const subClassOfBusiness = service.getSubClassOfBusiness(formGroup) as any;

        expect(subClassOfBusiness).toMatchObject({});
      });

      it('should return ISubClassOfBusiness', () => {
        const formGroup = service.createSubClassOfBusinessFormGroup(sampleWithRequiredData);

        const subClassOfBusiness = service.getSubClassOfBusiness(formGroup) as any;

        expect(subClassOfBusiness).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISubClassOfBusiness should not enable id FormControl', () => {
        const formGroup = service.createSubClassOfBusinessFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSubClassOfBusiness should disable id FormControl', () => {
        const formGroup = service.createSubClassOfBusinessFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
