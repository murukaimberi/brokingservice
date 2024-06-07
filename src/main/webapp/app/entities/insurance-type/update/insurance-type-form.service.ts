import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInsuranceType, NewInsuranceType } from '../insurance-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInsuranceType for edit and NewInsuranceTypeFormGroupInput for create.
 */
type InsuranceTypeFormGroupInput = IInsuranceType | PartialWithRequiredKeyOf<NewInsuranceType>;

type InsuranceTypeFormDefaults = Pick<NewInsuranceType, 'id'>;

type InsuranceTypeFormGroupContent = {
  id: FormControl<IInsuranceType['id'] | NewInsuranceType['id']>;
  name: FormControl<IInsuranceType['name']>;
};

export type InsuranceTypeFormGroup = FormGroup<InsuranceTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InsuranceTypeFormService {
  createInsuranceTypeFormGroup(insuranceType: InsuranceTypeFormGroupInput = { id: null }): InsuranceTypeFormGroup {
    const insuranceTypeRawValue = {
      ...this.getFormDefaults(),
      ...insuranceType,
    };
    return new FormGroup<InsuranceTypeFormGroupContent>({
      id: new FormControl(
        { value: insuranceTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(insuranceTypeRawValue.name, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(150)],
      }),
    });
  }

  getInsuranceType(form: InsuranceTypeFormGroup): IInsuranceType | NewInsuranceType {
    return form.getRawValue() as IInsuranceType | NewInsuranceType;
  }

  resetForm(form: InsuranceTypeFormGroup, insuranceType: InsuranceTypeFormGroupInput): void {
    const insuranceTypeRawValue = { ...this.getFormDefaults(), ...insuranceType };
    form.reset(
      {
        ...insuranceTypeRawValue,
        id: { value: insuranceTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InsuranceTypeFormDefaults {
    return {
      id: null,
    };
  }
}
