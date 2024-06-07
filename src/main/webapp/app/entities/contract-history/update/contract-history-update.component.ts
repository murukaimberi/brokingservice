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
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ContractHistoryService } from '../service/contract-history.service';
import { IContractHistory } from '../contract-history.model';
import { ContractHistoryFormService, ContractHistoryFormGroup } from './contract-history-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contract-history-update',
  templateUrl: './contract-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContractHistoryUpdateComponent implements OnInit {
  isSaving = false;
  contractHistory: IContractHistory | null = null;

  usersSharedCollection: IUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected contractHistoryService = inject(ContractHistoryService);
  protected contractHistoryFormService = inject(ContractHistoryFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractHistoryFormGroup = this.contractHistoryFormService.createContractHistoryFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractHistory }) => {
      this.contractHistory = contractHistory;
      if (contractHistory) {
        this.updateForm(contractHistory);
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
    const contractHistory = this.contractHistoryFormService.getContractHistory(this.editForm);
    if (contractHistory.id !== null) {
      this.subscribeToSaveResponse(this.contractHistoryService.update(contractHistory));
    } else {
      this.subscribeToSaveResponse(this.contractHistoryService.create(contractHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractHistory>>): void {
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

  protected updateForm(contractHistory: IContractHistory): void {
    this.contractHistory = contractHistory;
    this.contractHistoryFormService.resetForm(this.editForm, contractHistory);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      contractHistory.updated,
      contractHistory.approved,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(users, this.contractHistory?.updated, this.contractHistory?.approved),
        ),
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
