import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInsuranceType } from '../insurance-type.model';
import { InsuranceTypeService } from '../service/insurance-type.service';
import { InsuranceTypeFormService, InsuranceTypeFormGroup } from './insurance-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-insurance-type-update',
  templateUrl: './insurance-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InsuranceTypeUpdateComponent implements OnInit {
  isSaving = false;
  insuranceType: IInsuranceType | null = null;

  protected insuranceTypeService = inject(InsuranceTypeService);
  protected insuranceTypeFormService = inject(InsuranceTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InsuranceTypeFormGroup = this.insuranceTypeFormService.createInsuranceTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insuranceType }) => {
      this.insuranceType = insuranceType;
      if (insuranceType) {
        this.updateForm(insuranceType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const insuranceType = this.insuranceTypeFormService.getInsuranceType(this.editForm);
    if (insuranceType.id !== null) {
      this.subscribeToSaveResponse(this.insuranceTypeService.update(insuranceType));
    } else {
      this.subscribeToSaveResponse(this.insuranceTypeService.create(insuranceType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInsuranceType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(insuranceType: IInsuranceType): void {
    this.insuranceType = insuranceType;
    this.insuranceTypeFormService.resetForm(this.editForm, insuranceType);
  }
}
