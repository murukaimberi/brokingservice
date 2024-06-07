import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClassOfBusiness } from 'app/entities/class-of-business/class-of-business.model';
import { ClassOfBusinessService } from 'app/entities/class-of-business/service/class-of-business.service';
import { ISubClassOfBusiness } from '../sub-class-of-business.model';
import { SubClassOfBusinessService } from '../service/sub-class-of-business.service';
import { SubClassOfBusinessFormService, SubClassOfBusinessFormGroup } from './sub-class-of-business-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sub-class-of-business-update',
  templateUrl: './sub-class-of-business-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubClassOfBusinessUpdateComponent implements OnInit {
  isSaving = false;
  subClassOfBusiness: ISubClassOfBusiness | null = null;

  classOfBusinessesSharedCollection: IClassOfBusiness[] = [];

  protected subClassOfBusinessService = inject(SubClassOfBusinessService);
  protected subClassOfBusinessFormService = inject(SubClassOfBusinessFormService);
  protected classOfBusinessService = inject(ClassOfBusinessService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubClassOfBusinessFormGroup = this.subClassOfBusinessFormService.createSubClassOfBusinessFormGroup();

  compareClassOfBusiness = (o1: IClassOfBusiness | null, o2: IClassOfBusiness | null): boolean =>
    this.classOfBusinessService.compareClassOfBusiness(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subClassOfBusiness }) => {
      this.subClassOfBusiness = subClassOfBusiness;
      if (subClassOfBusiness) {
        this.updateForm(subClassOfBusiness);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subClassOfBusiness = this.subClassOfBusinessFormService.getSubClassOfBusiness(this.editForm);
    if (subClassOfBusiness.id !== null) {
      this.subscribeToSaveResponse(this.subClassOfBusinessService.update(subClassOfBusiness));
    } else {
      this.subscribeToSaveResponse(this.subClassOfBusinessService.create(subClassOfBusiness));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubClassOfBusiness>>): void {
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

  protected updateForm(subClassOfBusiness: ISubClassOfBusiness): void {
    this.subClassOfBusiness = subClassOfBusiness;
    this.subClassOfBusinessFormService.resetForm(this.editForm, subClassOfBusiness);

    this.classOfBusinessesSharedCollection = this.classOfBusinessService.addClassOfBusinessToCollectionIfMissing<IClassOfBusiness>(
      this.classOfBusinessesSharedCollection,
      subClassOfBusiness.classOfBusiness,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classOfBusinessService
      .query()
      .pipe(map((res: HttpResponse<IClassOfBusiness[]>) => res.body ?? []))
      .pipe(
        map((classOfBusinesses: IClassOfBusiness[]) =>
          this.classOfBusinessService.addClassOfBusinessToCollectionIfMissing<IClassOfBusiness>(
            classOfBusinesses,
            this.subClassOfBusiness?.classOfBusiness,
          ),
        ),
      )
      .subscribe((classOfBusinesses: IClassOfBusiness[]) => (this.classOfBusinessesSharedCollection = classOfBusinesses));
  }
}
