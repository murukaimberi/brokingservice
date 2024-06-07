import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContractReport, NewContractReport } from '../contract-report.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContractReport for edit and NewContractReportFormGroupInput for create.
 */
type ContractReportFormGroupInput = IContractReport | PartialWithRequiredKeyOf<NewContractReport>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContractReport | NewContractReport> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type ContractReportFormRawValue = FormValueOf<IContractReport>;

type NewContractReportFormRawValue = FormValueOf<NewContractReport>;

type ContractReportFormDefaults = Pick<NewContractReport, 'id' | 'createdDate'>;

type ContractReportFormGroupContent = {
  id: FormControl<ContractReportFormRawValue['id'] | NewContractReport['id']>;
  contractDocument: FormControl<ContractReportFormRawValue['contractDocument']>;
  contractDocumentContentType: FormControl<ContractReportFormRawValue['contractDocumentContentType']>;
  createdDate: FormControl<ContractReportFormRawValue['createdDate']>;
};

export type ContractReportFormGroup = FormGroup<ContractReportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContractReportFormService {
  createContractReportFormGroup(contractReport: ContractReportFormGroupInput = { id: null }): ContractReportFormGroup {
    const contractReportRawValue = this.convertContractReportToContractReportRawValue({
      ...this.getFormDefaults(),
      ...contractReport,
    });
    return new FormGroup<ContractReportFormGroupContent>({
      id: new FormControl(
        { value: contractReportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      contractDocument: new FormControl(contractReportRawValue.contractDocument, {
        validators: [Validators.required],
      }),
      contractDocumentContentType: new FormControl(contractReportRawValue.contractDocumentContentType),
      createdDate: new FormControl(contractReportRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getContractReport(form: ContractReportFormGroup): IContractReport | NewContractReport {
    return this.convertContractReportRawValueToContractReport(
      form.getRawValue() as ContractReportFormRawValue | NewContractReportFormRawValue,
    );
  }

  resetForm(form: ContractReportFormGroup, contractReport: ContractReportFormGroupInput): void {
    const contractReportRawValue = this.convertContractReportToContractReportRawValue({ ...this.getFormDefaults(), ...contractReport });
    form.reset(
      {
        ...contractReportRawValue,
        id: { value: contractReportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContractReportFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertContractReportRawValueToContractReport(
    rawContractReport: ContractReportFormRawValue | NewContractReportFormRawValue,
  ): IContractReport | NewContractReport {
    return {
      ...rawContractReport,
      createdDate: dayjs(rawContractReport.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertContractReportToContractReportRawValue(
    contractReport: IContractReport | (Partial<NewContractReport> & ContractReportFormDefaults),
  ): ContractReportFormRawValue | PartialWithRequiredKeyOf<NewContractReportFormRawValue> {
    return {
      ...contractReport,
      createdDate: contractReport.createdDate ? contractReport.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
