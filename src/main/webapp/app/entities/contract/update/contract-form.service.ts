import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContract, NewContract } from '../contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContract for edit and NewContractFormGroupInput for create.
 */
type ContractFormGroupInput = IContract | PartialWithRequiredKeyOf<NewContract>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContract | NewContract> = Omit<T, 'inception' | 'expiry'> & {
  inception?: string | null;
  expiry?: string | null;
};

type ContractFormRawValue = FormValueOf<IContract>;

type NewContractFormRawValue = FormValueOf<NewContract>;

type ContractFormDefaults = Pick<NewContract, 'id' | 'inception' | 'expiry' | 'active' | 'reinsurers'>;

type ContractFormGroupContent = {
  id: FormControl<ContractFormRawValue['id'] | NewContract['id']>;
  type: FormControl<ContractFormRawValue['type']>;
  subType: FormControl<ContractFormRawValue['subType']>;
  inception: FormControl<ContractFormRawValue['inception']>;
  expiry: FormControl<ContractFormRawValue['expiry']>;
  currency: FormControl<ContractFormRawValue['currency']>;
  totalSumInsured: FormControl<ContractFormRawValue['totalSumInsured']>;
  limitOfLiability: FormControl<ContractFormRawValue['limitOfLiability']>;
  uuid: FormControl<ContractFormRawValue['uuid']>;
  status: FormControl<ContractFormRawValue['status']>;
  active: FormControl<ContractFormRawValue['active']>;
  insured: FormControl<ContractFormRawValue['insured']>;
  insurer: FormControl<ContractFormRawValue['insurer']>;
  broker: FormControl<ContractFormRawValue['broker']>;
  reinsurers: FormControl<ContractFormRawValue['reinsurers']>;
  classOfBusiness: FormControl<ContractFormRawValue['classOfBusiness']>;
  subClassOfBusiness: FormControl<ContractFormRawValue['subClassOfBusiness']>;
  country: FormControl<ContractFormRawValue['country']>;
};

export type ContractFormGroup = FormGroup<ContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractFormService {
  createContractFormGroup(contract: ContractFormGroupInput = { id: null }): ContractFormGroup {
    const contractRawValue = this.convertContractToContractRawValue({
      ...this.getFormDefaults(),
      ...contract,
    });
    return new FormGroup<ContractFormGroupContent>({
      id: new FormControl(
        { value: contractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(contractRawValue.type, {
        validators: [Validators.required],
      }),
      subType: new FormControl(contractRawValue.subType, {
        validators: [Validators.required],
      }),
      inception: new FormControl(contractRawValue.inception, {
        validators: [Validators.required],
      }),
      expiry: new FormControl(contractRawValue.expiry, {
        validators: [Validators.required],
      }),
      currency: new FormControl(contractRawValue.currency, {
        validators: [Validators.required],
      }),
      totalSumInsured: new FormControl(contractRawValue.totalSumInsured, {
        validators: [Validators.required],
      }),
      limitOfLiability: new FormControl(contractRawValue.limitOfLiability, {
        validators: [Validators.required],
      }),
      uuid: new FormControl(contractRawValue.uuid),
      status: new FormControl(contractRawValue.status, {
        validators: [Validators.required],
      }),
      active: new FormControl(contractRawValue.active),
      insured: new FormControl(contractRawValue.insured, {
        validators: [Validators.required],
      }),
      insurer: new FormControl(contractRawValue.insurer, {
        validators: [Validators.required],
      }),
      broker: new FormControl(contractRawValue.broker, {
        validators: [Validators.required],
      }),
      reinsurers: new FormControl(contractRawValue.reinsurers ?? []),
      classOfBusiness: new FormControl(contractRawValue.classOfBusiness, {
        validators: [Validators.required],
      }),
      subClassOfBusiness: new FormControl(contractRawValue.subClassOfBusiness, {
        validators: [Validators.required],
      }),
      country: new FormControl(contractRawValue.country, {
        validators: [Validators.required],
      }),
    });
  }

  getContract(form: ContractFormGroup): IContract | NewContract {
    return this.convertContractRawValueToContract(form.getRawValue() as ContractFormRawValue | NewContractFormRawValue);
  }

  resetForm(form: ContractFormGroup, contract: ContractFormGroupInput): void {
    const contractRawValue = this.convertContractToContractRawValue({ ...this.getFormDefaults(), ...contract });
    form.reset(
      {
        ...contractRawValue,
        id: { value: contractRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      inception: currentTime,
      expiry: currentTime,
      active: false,
      reinsurers: [],
    };
  }

  private convertContractRawValueToContract(rawContract: ContractFormRawValue | NewContractFormRawValue): IContract | NewContract {
    return {
      ...rawContract,
      inception: dayjs(rawContract.inception, DATE_TIME_FORMAT),
      expiry: dayjs(rawContract.expiry, DATE_TIME_FORMAT),
    };
  }

  private convertContractToContractRawValue(
    contract: IContract | (Partial<NewContract> & ContractFormDefaults),
  ): ContractFormRawValue | PartialWithRequiredKeyOf<NewContractFormRawValue> {
    return {
      ...contract,
      inception: contract.inception ? contract.inception.format(DATE_TIME_FORMAT) : undefined,
      expiry: contract.expiry ? contract.expiry.format(DATE_TIME_FORMAT) : undefined,
      reinsurers: contract.reinsurers ?? [],
    };
  }
}
