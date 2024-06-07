import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBusinessPartner, NewBusinessPartner } from '../business-partner.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBusinessPartner for edit and NewBusinessPartnerFormGroupInput for create.
 */
type BusinessPartnerFormGroupInput = IBusinessPartner | PartialWithRequiredKeyOf<NewBusinessPartner>;

type BusinessPartnerFormDefaults = Pick<NewBusinessPartner, 'id' | 'reInsurerContracts'>;

type BusinessPartnerFormGroupContent = {
  id: FormControl<IBusinessPartner['id'] | NewBusinessPartner['id']>;
  name: FormControl<IBusinessPartner['name']>;
  description: FormControl<IBusinessPartner['description']>;
  representativeName: FormControl<IBusinessPartner['representativeName']>;
  email: FormControl<IBusinessPartner['email']>;
  phoneNumber: FormControl<IBusinessPartner['phoneNumber']>;
  agentType: FormControl<IBusinessPartner['agentType']>;
  reInsurerContracts: FormControl<IBusinessPartner['reInsurerContracts']>;
};

export type BusinessPartnerFormGroup = FormGroup<BusinessPartnerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BusinessPartnerFormService {
  createBusinessPartnerFormGroup(businessPartner: BusinessPartnerFormGroupInput = { id: null }): BusinessPartnerFormGroup {
    const businessPartnerRawValue = {
      ...this.getFormDefaults(),
      ...businessPartner,
    };
    return new FormGroup<BusinessPartnerFormGroupContent>({
      id: new FormControl(
        { value: businessPartnerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(businessPartnerRawValue.name, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(150)],
      }),
      description: new FormControl(businessPartnerRawValue.description, {
        validators: [Validators.required],
      }),
      representativeName: new FormControl(businessPartnerRawValue.representativeName, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(150)],
      }),
      email: new FormControl(businessPartnerRawValue.email, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      }),
      phoneNumber: new FormControl(businessPartnerRawValue.phoneNumber, {
        validators: [Validators.required],
      }),
      agentType: new FormControl(businessPartnerRawValue.agentType, {
        validators: [Validators.required],
      }),
      reInsurerContracts: new FormControl(businessPartnerRawValue.reInsurerContracts ?? []),
    });
  }

  getBusinessPartner(form: BusinessPartnerFormGroup): IBusinessPartner | NewBusinessPartner {
    return form.getRawValue() as IBusinessPartner | NewBusinessPartner;
  }

  resetForm(form: BusinessPartnerFormGroup, businessPartner: BusinessPartnerFormGroupInput): void {
    const businessPartnerRawValue = { ...this.getFormDefaults(), ...businessPartner };
    form.reset(
      {
        ...businessPartnerRawValue,
        id: { value: businessPartnerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BusinessPartnerFormDefaults {
    return {
      id: null,
      reInsurerContracts: [],
    };
  }
}
