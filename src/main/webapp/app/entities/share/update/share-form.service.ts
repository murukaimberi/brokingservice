import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IShare, NewShare } from '../share.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShare for edit and NewShareFormGroupInput for create.
 */
type ShareFormGroupInput = IShare | PartialWithRequiredKeyOf<NewShare>;

type ShareFormDefaults = Pick<NewShare, 'id'>;

type ShareFormGroupContent = {
  id: FormControl<IShare['id'] | NewShare['id']>;
  sharePercentage: FormControl<IShare['sharePercentage']>;
  totalSumInsured: FormControl<IShare['totalSumInsured']>;
  limitOfLiability: FormControl<IShare['limitOfLiability']>;
  grossPremium: FormControl<IShare['grossPremium']>;
  riCommission: FormControl<IShare['riCommission']>;
  netPremium: FormControl<IShare['netPremium']>;
  brokerage: FormControl<IShare['brokerage']>;
  brokerageAmount: FormControl<IShare['brokerageAmount']>;
  netPayable: FormControl<IShare['netPayable']>;
  reInsurer: FormControl<IShare['reInsurer']>;
};

export type ShareFormGroup = FormGroup<ShareFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ShareFormService {
  createShareFormGroup(share: ShareFormGroupInput = { id: null }): ShareFormGroup {
    const shareRawValue = {
      ...this.getFormDefaults(),
      ...share,
    };
    return new FormGroup<ShareFormGroupContent>({
      id: new FormControl(
        { value: shareRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sharePercentage: new FormControl(shareRawValue.sharePercentage, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      totalSumInsured: new FormControl(shareRawValue.totalSumInsured),
      limitOfLiability: new FormControl(shareRawValue.limitOfLiability),
      grossPremium: new FormControl(shareRawValue.grossPremium),
      riCommission: new FormControl(shareRawValue.riCommission),
      netPremium: new FormControl(shareRawValue.netPremium),
      brokerage: new FormControl(shareRawValue.brokerage, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      brokerageAmount: new FormControl(shareRawValue.brokerageAmount),
      netPayable: new FormControl(shareRawValue.netPayable),
      reInsurer: new FormControl(shareRawValue.reInsurer, {
        validators: [Validators.required],
      }),
    });
  }

  getShare(form: ShareFormGroup): IShare | NewShare {
    return form.getRawValue() as IShare | NewShare;
  }

  resetForm(form: ShareFormGroup, share: ShareFormGroupInput): void {
    const shareRawValue = { ...this.getFormDefaults(), ...share };
    form.reset(
      {
        ...shareRawValue,
        id: { value: shareRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ShareFormDefaults {
    return {
      id: null,
    };
  }
}
