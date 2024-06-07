import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInsuranceType } from 'app/entities/insurance-type/insurance-type.model';
import { InsuranceTypeService } from 'app/entities/insurance-type/service/insurance-type.service';
import { IClassOfBusiness } from '../class-of-business.model';
import { ClassOfBusinessService } from '../service/class-of-business.service';
import { ClassOfBusinessFormService, ClassOfBusinessFormGroup } from './class-of-business-form.service';

@Component({
  standalone: true,
  selector: 'jhi-class-of-business-update',
  templateUrl: './class-of-business-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClassOfBusinessUpdateComponent implements OnInit {
  isSaving = false;
  classOfBusiness: IClassOfBusiness | null = null;

  insuranceTypesSharedCollection: IInsuranceType[] = [];

  protected classOfBusinessService = inject(ClassOfBusinessService);
  protected classOfBusinessFormService = inject(ClassOfBusinessFormService);
  protected insuranceTypeService = inject(InsuranceTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClassOfBusinessFormGroup = this.classOfBusinessFormService.createClassOfBusinessFormGroup();

  compareInsuranceType = (o1: IInsuranceType | null, o2: IInsuranceType | null): boolean =>
    this.insuranceTypeService.compareInsuranceType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classOfBusiness }) => {
      this.classOfBusiness = classOfBusiness;
      if (classOfBusiness) {
        this.updateForm(classOfBusiness);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classOfBusiness = this.classOfBusinessFormService.getClassOfBusiness(this.editForm);
    if (classOfBusiness.id !== null) {
      this.subscribeToSaveResponse(this.classOfBusinessService.update(classOfBusiness));
    } else {
      this.subscribeToSaveResponse(this.classOfBusinessService.create(classOfBusiness));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassOfBusiness>>): void {
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

  protected updateForm(classOfBusiness: IClassOfBusiness): void {
    this.classOfBusiness = classOfBusiness;
    this.classOfBusinessFormService.resetForm(this.editForm, classOfBusiness);

    this.insuranceTypesSharedCollection = this.insuranceTypeService.addInsuranceTypeToCollectionIfMissing<IInsuranceType>(
      this.insuranceTypesSharedCollection,
      classOfBusiness.insuranceType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.insuranceTypeService
      .query()
      .pipe(map((res: HttpResponse<IInsuranceType[]>) => res.body ?? []))
      .pipe(
        map((insuranceTypes: IInsuranceType[]) =>
          this.insuranceTypeService.addInsuranceTypeToCollectionIfMissing<IInsuranceType>(
            insuranceTypes,
            this.classOfBusiness?.insuranceType,
          ),
        ),
      )
      .subscribe((insuranceTypes: IInsuranceType[]) => (this.insuranceTypesSharedCollection = insuranceTypes));
  }
}
