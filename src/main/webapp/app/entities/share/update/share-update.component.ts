import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBusinessPartner } from 'app/entities/business-partner/business-partner.model';
import { BusinessPartnerService } from 'app/entities/business-partner/service/business-partner.service';
import { IShare } from '../share.model';
import { ShareService } from '../service/share.service';
import { ShareFormService, ShareFormGroup } from './share-form.service';

@Component({
  standalone: true,
  selector: 'jhi-share-update',
  templateUrl: './share-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ShareUpdateComponent implements OnInit {
  isSaving = false;
  share: IShare | null = null;

  businessPartnersSharedCollection: IBusinessPartner[] = [];

  protected shareService = inject(ShareService);
  protected shareFormService = inject(ShareFormService);
  protected businessPartnerService = inject(BusinessPartnerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShareFormGroup = this.shareFormService.createShareFormGroup();

  compareBusinessPartner = (o1: IBusinessPartner | null, o2: IBusinessPartner | null): boolean =>
    this.businessPartnerService.compareBusinessPartner(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ share }) => {
      this.share = share;
      if (share) {
        this.updateForm(share);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const share = this.shareFormService.getShare(this.editForm);
    if (share.id !== null) {
      this.subscribeToSaveResponse(this.shareService.update(share));
    } else {
      this.subscribeToSaveResponse(this.shareService.create(share));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShare>>): void {
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

  protected updateForm(share: IShare): void {
    this.share = share;
    this.shareFormService.resetForm(this.editForm, share);

    this.businessPartnersSharedCollection = this.businessPartnerService.addBusinessPartnerToCollectionIfMissing<IBusinessPartner>(
      this.businessPartnersSharedCollection,
      share.reInsurer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.businessPartnerService
      .query()
      .pipe(map((res: HttpResponse<IBusinessPartner[]>) => res.body ?? []))
      .pipe(
        map((businessPartners: IBusinessPartner[]) =>
          this.businessPartnerService.addBusinessPartnerToCollectionIfMissing<IBusinessPartner>(businessPartners, this.share?.reInsurer),
        ),
      )
      .subscribe((businessPartners: IBusinessPartner[]) => (this.businessPartnersSharedCollection = businessPartners));
  }
}
