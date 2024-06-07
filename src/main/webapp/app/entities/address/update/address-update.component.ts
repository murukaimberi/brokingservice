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
import { IBusinessPartner } from 'app/entities/business-partner/business-partner.model';
import { BusinessPartnerService } from 'app/entities/business-partner/service/business-partner.service';
import { AddressService } from '../service/address.service';
import { IAddress } from '../address.model';
import { AddressFormService, AddressFormGroup } from './address-form.service';

@Component({
  standalone: true,
  selector: 'jhi-address-update',
  templateUrl: './address-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AddressUpdateComponent implements OnInit {
  isSaving = false;
  address: IAddress | null = null;

  businessPartnersSharedCollection: IBusinessPartner[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected addressService = inject(AddressService);
  protected addressFormService = inject(AddressFormService);
  protected businessPartnerService = inject(BusinessPartnerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AddressFormGroup = this.addressFormService.createAddressFormGroup();

  compareBusinessPartner = (o1: IBusinessPartner | null, o2: IBusinessPartner | null): boolean =>
    this.businessPartnerService.compareBusinessPartner(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ address }) => {
      this.address = address;
      if (address) {
        this.updateForm(address);
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
    const address = this.addressFormService.getAddress(this.editForm);
    if (address.id !== null) {
      this.subscribeToSaveResponse(this.addressService.update(address));
    } else {
      this.subscribeToSaveResponse(this.addressService.create(address));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddress>>): void {
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

  protected updateForm(address: IAddress): void {
    this.address = address;
    this.addressFormService.resetForm(this.editForm, address);

    this.businessPartnersSharedCollection = this.businessPartnerService.addBusinessPartnerToCollectionIfMissing<IBusinessPartner>(
      this.businessPartnersSharedCollection,
      address.cedent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.businessPartnerService
      .query()
      .pipe(map((res: HttpResponse<IBusinessPartner[]>) => res.body ?? []))
      .pipe(
        map((businessPartners: IBusinessPartner[]) =>
          this.businessPartnerService.addBusinessPartnerToCollectionIfMissing<IBusinessPartner>(businessPartners, this.address?.cedent),
        ),
      )
      .subscribe((businessPartners: IBusinessPartner[]) => (this.businessPartnersSharedCollection = businessPartners));
  }
}
