import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAddress, NewAddress } from '../address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAddress for edit and NewAddressFormGroupInput for create.
 */
type AddressFormGroupInput = IAddress | PartialWithRequiredKeyOf<NewAddress>;

type AddressFormDefaults = Pick<NewAddress, 'id'>;

type AddressFormGroupContent = {
  id: FormControl<IAddress['id'] | NewAddress['id']>;
  streetAddress: FormControl<IAddress['streetAddress']>;
  city: FormControl<IAddress['city']>;
  province: FormControl<IAddress['province']>;
  state: FormControl<IAddress['state']>;
  zipCode: FormControl<IAddress['zipCode']>;
  country: FormControl<IAddress['country']>;
  cedent: FormControl<IAddress['cedent']>;
};

export type AddressFormGroup = FormGroup<AddressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AddressFormService {
  createAddressFormGroup(address: AddressFormGroupInput = { id: null }): AddressFormGroup {
    const addressRawValue = {
      ...this.getFormDefaults(),
      ...address,
    };
    return new FormGroup<AddressFormGroupContent>({
      id: new FormControl(
        { value: addressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      streetAddress: new FormControl(addressRawValue.streetAddress, {
        validators: [Validators.required],
      }),
      city: new FormControl(addressRawValue.city, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(150)],
      }),
      province: new FormControl(addressRawValue.province, {
        validators: [Validators.required],
      }),
      state: new FormControl(addressRawValue.state),
      zipCode: new FormControl(addressRawValue.zipCode, {
        validators: [Validators.required],
      }),
      country: new FormControl(addressRawValue.country, {
        validators: [Validators.required],
      }),
      cedent: new FormControl(addressRawValue.cedent),
    });
  }

  getAddress(form: AddressFormGroup): IAddress | NewAddress {
    return form.getRawValue() as IAddress | NewAddress;
  }

  resetForm(form: AddressFormGroup, address: AddressFormGroupInput): void {
    const addressRawValue = { ...this.getFormDefaults(), ...address };
    form.reset(
      {
        ...addressRawValue,
        id: { value: addressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AddressFormDefaults {
    return {
      id: null,
    };
  }
}
