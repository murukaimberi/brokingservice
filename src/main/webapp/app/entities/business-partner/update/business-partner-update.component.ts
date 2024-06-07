import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { InsuranceAgentType } from 'app/entities/enumerations/insurance-agent-type.model';
import { BusinessPartnerService } from '../service/business-partner.service';
import { IBusinessPartner } from '../business-partner.model';
import { BusinessPartnerFormService, BusinessPartnerFormGroup } from './business-partner-form.service';

@Component({
  standalone: true,
  selector: 'jhi-business-partner-update',
  templateUrl: './business-partner-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BusinessPartnerUpdateComponent implements OnInit {
  isSaving = false;
  businessPartner: IBusinessPartner | null = null;
  insuranceAgentTypeValues = Object.keys(InsuranceAgentType);

  contractsSharedCollection: IContract[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected businessPartnerService = inject(BusinessPartnerService);
  protected businessPartnerFormService = inject(BusinessPartnerFormService);
  protected contractService = inject(ContractService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BusinessPartnerFormGroup = this.businessPartnerFormService.createBusinessPartnerFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ businessPartner }) => {
      this.businessPartner = businessPartner;
      if (businessPartner) {
        this.updateForm(businessPartner);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('brokingserviceApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const businessPartner = this.businessPartnerFormService.getBusinessPartner(this.editForm);
    if (businessPartner.id !== null) {
      this.subscribeToSaveResponse(this.businessPartnerService.update(businessPartner));
    } else {
      this.subscribeToSaveResponse(this.businessPartnerService.create(businessPartner));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessPartner>>): void {
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

  protected updateForm(businessPartner: IBusinessPartner): void {
    this.businessPartner = businessPartner;
    this.businessPartnerFormService.resetForm(this.editForm, businessPartner);

    this.contractsSharedCollection = this.contractService.addContractToCollectionIfMissing<IContract>(
      this.contractsSharedCollection,
      ...(businessPartner.reInsurerContracts ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, ...(this.businessPartner?.reInsurerContracts ?? [])),
        ),
      )
      .subscribe((contracts: IContract[]) => (this.contractsSharedCollection = contracts));
  }
}
