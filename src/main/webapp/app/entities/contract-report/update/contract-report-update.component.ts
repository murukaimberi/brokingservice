import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ContractReportService } from '../service/contract-report.service';
import { IContractReport } from '../contract-report.model';
import { ContractReportFormService, ContractReportFormGroup } from './contract-report-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contract-report-update',
  templateUrl: './contract-report-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractReportUpdateComponent implements OnInit {
  isSaving = false;
  contractReport: IContractReport | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected contractReportService = inject(ContractReportService);
  protected contractReportFormService = inject(ContractReportFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractReportFormGroup = this.contractReportFormService.createContractReportFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractReport }) => {
      this.contractReport = contractReport;
      if (contractReport) {
        this.updateForm(contractReport);
      }
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
    const contractReport = this.contractReportFormService.getContractReport(this.editForm);
    if (contractReport.id !== null) {
      this.subscribeToSaveResponse(this.contractReportService.update(contractReport));
    } else {
      this.subscribeToSaveResponse(this.contractReportService.create(contractReport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractReport>>): void {
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

  protected updateForm(contractReport: IContractReport): void {
    this.contractReport = contractReport;
    this.contractReportFormService.resetForm(this.editForm, contractReport);
  }
}
