import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IReinsurancePlacement } from '../reinsurance-placement.model';
import { ReinsurancePlacementService } from '../service/reinsurance-placement.service';
import { ReinsurancePlacementFormService, ReinsurancePlacementFormGroup } from './reinsurance-placement-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reinsurance-placement-update',
  templateUrl: './reinsurance-placement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReinsurancePlacementUpdateComponent implements OnInit {
  isSaving = false;
  reinsurancePlacement: IReinsurancePlacement | null = null;

  contractsCollection: IContract[] = [];

  protected reinsurancePlacementService = inject(ReinsurancePlacementService);
  protected reinsurancePlacementFormService = inject(ReinsurancePlacementFormService);
  protected contractService = inject(ContractService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReinsurancePlacementFormGroup = this.reinsurancePlacementFormService.createReinsurancePlacementFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reinsurancePlacement }) => {
      this.reinsurancePlacement = reinsurancePlacement;
      if (reinsurancePlacement) {
        this.updateForm(reinsurancePlacement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reinsurancePlacement = this.reinsurancePlacementFormService.getReinsurancePlacement(this.editForm);
    if (reinsurancePlacement.id !== null) {
      this.subscribeToSaveResponse(this.reinsurancePlacementService.update(reinsurancePlacement));
    } else {
      this.subscribeToSaveResponse(this.reinsurancePlacementService.create(reinsurancePlacement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReinsurancePlacement>>): void {
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

  protected updateForm(reinsurancePlacement: IReinsurancePlacement): void {
    this.reinsurancePlacement = reinsurancePlacement;
    this.reinsurancePlacementFormService.resetForm(this.editForm, reinsurancePlacement);

    this.contractsCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsCollection,
      reinsurancePlacement.contract,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query({ filter: 'reinsuranceplacement-is-null' })
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.reinsurancePlacement?.contract),
        ),
      )
      .subscribe((contracts: IContract[]) => (this.contractsCollection = contracts));
  }
}
