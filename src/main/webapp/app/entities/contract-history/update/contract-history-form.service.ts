import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContractHistory, NewContractHistory } from '../contract-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContractHistory for edit and NewContractHistoryFormGroupInput for create.
 */
type ContractHistoryFormGroupInput = IContractHistory | PartialWithRequiredKeyOf<NewContractHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContractHistory | NewContractHistory> = Omit<
  T,
  'contractCreatedDate' | 'contractActiveDate' | 'contractInActiveDate' | 'contractLastModifiedDate'
> & {
  contractCreatedDate?: string | null;
  contractActiveDate?: string | null;
  contractInActiveDate?: string | null;
  contractLastModifiedDate?: string | null;
};

type ContractHistoryFormRawValue = FormValueOf<IContractHistory>;

type NewContractHistoryFormRawValue = FormValueOf<NewContractHistory>;

type ContractHistoryFormDefaults = Pick<
  NewContractHistory,
  'id' | 'contractCreatedDate' | 'contractActiveDate' | 'contractInActiveDate' | 'contractLastModifiedDate'
>;

type ContractHistoryFormGroupContent = {
  id: FormControl<ContractHistoryFormRawValue['id'] | NewContractHistory['id']>;
  contractCreatedDate: FormControl<ContractHistoryFormRawValue['contractCreatedDate']>;
  contractActiveDate: FormControl<ContractHistoryFormRawValue['contractActiveDate']>;
  contractInActiveDate: FormControl<ContractHistoryFormRawValue['contractInActiveDate']>;
  contractLastModifiedDate: FormControl<ContractHistoryFormRawValue['contractLastModifiedDate']>;
  changeDescription: FormControl<ContractHistoryFormRawValue['changeDescription']>;
  updated: FormControl<ContractHistoryFormRawValue['updated']>;
  approved: FormControl<ContractHistoryFormRawValue['approved']>;
};

export type ContractHistoryFormGroup = FormGroup<ContractHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractHistoryFormService {
  createContractHistoryFormGroup(contractHistory: ContractHistoryFormGroupInput = { id: null }): ContractHistoryFormGroup {
    const contractHistoryRawValue = this.convertContractHistoryToContractHistoryRawValue({
      ...this.getFormDefaults(),
      ...contractHistory,
    });
    return new FormGroup<ContractHistoryFormGroupContent>({
      id: new FormControl(
        { value: contractHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      contractCreatedDate: new FormControl(contractHistoryRawValue.contractCreatedDate, {
        validators: [Validators.required],
      }),
      contractActiveDate: new FormControl(contractHistoryRawValue.contractActiveDate, {
        validators: [Validators.required],
      }),
      contractInActiveDate: new FormControl(contractHistoryRawValue.contractInActiveDate),
      contractLastModifiedDate: new FormControl(contractHistoryRawValue.contractLastModifiedDate),
      changeDescription: new FormControl(contractHistoryRawValue.changeDescription, {
        validators: [Validators.required],
      }),
      updated: new FormControl(contractHistoryRawValue.updated, {
        validators: [Validators.required],
      }),
      approved: new FormControl(contractHistoryRawValue.approved),
    });
  }

  getContractHistory(form: ContractHistoryFormGroup): IContractHistory | NewContractHistory {
    return this.convertContractHistoryRawValueToContractHistory(
      form.getRawValue() as ContractHistoryFormRawValue | NewContractHistoryFormRawValue,
    );
  }

  resetForm(form: ContractHistoryFormGroup, contractHistory: ContractHistoryFormGroupInput): void {
    const contractHistoryRawValue = this.convertContractHistoryToContractHistoryRawValue({ ...this.getFormDefaults(), ...contractHistory });
    form.reset(
      {
        ...contractHistoryRawValue,
        id: { value: contractHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      contractCreatedDate: currentTime,
      contractActiveDate: currentTime,
      contractInActiveDate: currentTime,
      contractLastModifiedDate: currentTime,
    };
  }

  private convertContractHistoryRawValueToContractHistory(
    rawContractHistory: ContractHistoryFormRawValue | NewContractHistoryFormRawValue,
  ): IContractHistory | NewContractHistory {
    return {
      ...rawContractHistory,
      contractCreatedDate: dayjs(rawContractHistory.contractCreatedDate, DATE_TIME_FORMAT),
      contractActiveDate: dayjs(rawContractHistory.contractActiveDate, DATE_TIME_FORMAT),
      contractInActiveDate: dayjs(rawContractHistory.contractInActiveDate, DATE_TIME_FORMAT),
      contractLastModifiedDate: dayjs(rawContractHistory.contractLastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertContractHistoryToContractHistoryRawValue(
    contractHistory: IContractHistory | (Partial<NewContractHistory> & ContractHistoryFormDefaults),
  ): ContractHistoryFormRawValue | PartialWithRequiredKeyOf<NewContractHistoryFormRawValue> {
    return {
      ...contractHistory,
      contractCreatedDate: contractHistory.contractCreatedDate ? contractHistory.contractCreatedDate.format(DATE_TIME_FORMAT) : undefined,
      contractActiveDate: contractHistory.contractActiveDate ? contractHistory.contractActiveDate.format(DATE_TIME_FORMAT) : undefined,
      contractInActiveDate: contractHistory.contractInActiveDate
        ? contractHistory.contractInActiveDate.format(DATE_TIME_FORMAT)
        : undefined,
      contractLastModifiedDate: contractHistory.contractLastModifiedDate
        ? contractHistory.contractLastModifiedDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
