import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISubClassOfBusiness, NewSubClassOfBusiness } from '../sub-class-of-business.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubClassOfBusiness for edit and NewSubClassOfBusinessFormGroupInput for create.
 */
type SubClassOfBusinessFormGroupInput = ISubClassOfBusiness | PartialWithRequiredKeyOf<NewSubClassOfBusiness>;

type SubClassOfBusinessFormDefaults = Pick<NewSubClassOfBusiness, 'id'>;

type SubClassOfBusinessFormGroupContent = {
  id: FormControl<ISubClassOfBusiness['id'] | NewSubClassOfBusiness['id']>;
  name: FormControl<ISubClassOfBusiness['name']>;
  classOfBusiness: FormControl<ISubClassOfBusiness['classOfBusiness']>;
};

export type SubClassOfBusinessFormGroup = FormGroup<SubClassOfBusinessFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubClassOfBusinessFormService {
  createSubClassOfBusinessFormGroup(subClassOfBusiness: SubClassOfBusinessFormGroupInput = { id: null }): SubClassOfBusinessFormGroup {
    const subClassOfBusinessRawValue = {
      ...this.getFormDefaults(),
      ...subClassOfBusiness,
    };
    return new FormGroup<SubClassOfBusinessFormGroupContent>({
      id: new FormControl(
        { value: subClassOfBusinessRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(subClassOfBusinessRawValue.name, {
        validators: [Validators.minLength(3), Validators.maxLength(250)],
      }),
      classOfBusiness: new FormControl(subClassOfBusinessRawValue.classOfBusiness, {
        validators: [Validators.required],
      }),
    });
  }

  getSubClassOfBusiness(form: SubClassOfBusinessFormGroup): ISubClassOfBusiness | NewSubClassOfBusiness {
    return form.getRawValue() as ISubClassOfBusiness | NewSubClassOfBusiness;
  }

  resetForm(form: SubClassOfBusinessFormGroup, subClassOfBusiness: SubClassOfBusinessFormGroupInput): void {
    const subClassOfBusinessRawValue = { ...this.getFormDefaults(), ...subClassOfBusiness };
    form.reset(
      {
        ...subClassOfBusinessRawValue,
        id: { value: subClassOfBusinessRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SubClassOfBusinessFormDefaults {
    return {
      id: null,
    };
  }
}
