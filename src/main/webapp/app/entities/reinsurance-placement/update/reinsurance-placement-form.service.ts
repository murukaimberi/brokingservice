import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReinsurancePlacement, NewReinsurancePlacement } from '../reinsurance-placement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReinsurancePlacement for edit and NewReinsurancePlacementFormGroupInput for create.
 */
type ReinsurancePlacementFormGroupInput = IReinsurancePlacement | PartialWithRequiredKeyOf<NewReinsurancePlacement>;

type ReinsurancePlacementFormDefaults = Pick<NewReinsurancePlacement, 'id'>;

type ReinsurancePlacementFormGroupContent = {
  id: FormControl<IReinsurancePlacement['id'] | NewReinsurancePlacement['id']>;
  riPercentageCommission: FormControl<IReinsurancePlacement['riPercentageCommission']>;
  riPercentageShare: FormControl<IReinsurancePlacement['riPercentageShare']>;
  riTotalSumInsured: FormControl<IReinsurancePlacement['riTotalSumInsured']>;
  riLimitOfLiability: FormControl<IReinsurancePlacement['riLimitOfLiability']>;
  grossPremiumHundred: FormControl<IReinsurancePlacement['grossPremiumHundred']>;
  riPremium: FormControl<IReinsurancePlacement['riPremium']>;
  riCommission: FormControl<IReinsurancePlacement['riCommission']>;
  netDueFromInsurer: FormControl<IReinsurancePlacement['netDueFromInsurer']>;
  contract: FormControl<IReinsurancePlacement['contract']>;
};

export type ReinsurancePlacementFormGroup = FormGroup<ReinsurancePlacementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReinsurancePlacementFormService {
  createReinsurancePlacementFormGroup(
    reinsurancePlacement: ReinsurancePlacementFormGroupInput = { id: null },
  ): ReinsurancePlacementFormGroup {
    const reinsurancePlacementRawValue = {
      ...this.getFormDefaults(),
      ...reinsurancePlacement,
    };
    return new FormGroup<ReinsurancePlacementFormGroupContent>({
      id: new FormControl(
        { value: reinsurancePlacementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      riPercentageCommission: new FormControl(reinsurancePlacementRawValue.riPercentageCommission, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      riPercentageShare: new FormControl(reinsurancePlacementRawValue.riPercentageShare, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      riTotalSumInsured: new FormControl(reinsurancePlacementRawValue.riTotalSumInsured),
      riLimitOfLiability: new FormControl(reinsurancePlacementRawValue.riLimitOfLiability),
      grossPremiumHundred: new FormControl(reinsurancePlacementRawValue.grossPremiumHundred, {
        validators: [Validators.required],
      }),
      riPremium: new FormControl(reinsurancePlacementRawValue.riPremium),
      riCommission: new FormControl(reinsurancePlacementRawValue.riCommission),
      netDueFromInsurer: new FormControl(reinsurancePlacementRawValue.netDueFromInsurer),
      contract: new FormControl(reinsurancePlacementRawValue.contract, {
        validators: [Validators.required],
      }),
    });
  }

  getReinsurancePlacement(form: ReinsurancePlacementFormGroup): IReinsurancePlacement | NewReinsurancePlacement {
    return form.getRawValue() as IReinsurancePlacement | NewReinsurancePlacement;
  }

  resetForm(form: ReinsurancePlacementFormGroup, reinsurancePlacement: ReinsurancePlacementFormGroupInput): void {
    const reinsurancePlacementRawValue = { ...this.getFormDefaults(), ...reinsurancePlacement };
    form.reset(
      {
        ...reinsurancePlacementRawValue,
        id: { value: reinsurancePlacementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReinsurancePlacementFormDefaults {
    return {
      id: null,
    };
  }
}
