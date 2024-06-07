import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IClassOfBusiness, NewClassOfBusiness } from '../class-of-business.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClassOfBusiness for edit and NewClassOfBusinessFormGroupInput for create.
 */
type ClassOfBusinessFormGroupInput = IClassOfBusiness | PartialWithRequiredKeyOf<NewClassOfBusiness>;

type ClassOfBusinessFormDefaults = Pick<NewClassOfBusiness, 'id'>;

type ClassOfBusinessFormGroupContent = {
  id: FormControl<IClassOfBusiness['id'] | NewClassOfBusiness['id']>;
  name: FormControl<IClassOfBusiness['name']>;
  insuranceType: FormControl<IClassOfBusiness['insuranceType']>;
};

export type ClassOfBusinessFormGroup = FormGroup<ClassOfBusinessFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClassOfBusinessFormService {
  createClassOfBusinessFormGroup(classOfBusiness: ClassOfBusinessFormGroupInput = { id: null }): ClassOfBusinessFormGroup {
    const classOfBusinessRawValue = {
      ...this.getFormDefaults(),
      ...classOfBusiness,
    };
    return new FormGroup<ClassOfBusinessFormGroupContent>({
      id: new FormControl(
        { value: classOfBusinessRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(classOfBusinessRawValue.name, {
        validators: [Validators.minLength(3), Validators.maxLength(250)],
      }),
      insuranceType: new FormControl(classOfBusinessRawValue.insuranceType, {
        validators: [Validators.required],
      }),
    });
  }

  getClassOfBusiness(form: ClassOfBusinessFormGroup): IClassOfBusiness | NewClassOfBusiness {
    return form.getRawValue() as IClassOfBusiness | NewClassOfBusiness;
  }

  resetForm(form: ClassOfBusinessFormGroup, classOfBusiness: ClassOfBusinessFormGroupInput): void {
    const classOfBusinessRawValue = { ...this.getFormDefaults(), ...classOfBusiness };
    form.reset(
      {
        ...classOfBusinessRawValue,
        id: { value: classOfBusinessRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClassOfBusinessFormDefaults {
    return {
      id: null,
    };
  }
}
