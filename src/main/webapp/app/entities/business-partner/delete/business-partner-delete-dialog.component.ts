import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBusinessPartner } from '../business-partner.model';
import { BusinessPartnerService } from '../service/business-partner.service';

@Component({
  standalone: true,
  templateUrl: './business-partner-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BusinessPartnerDeleteDialogComponent {
  businessPartner?: IBusinessPartner;

  protected businessPartnerService = inject(BusinessPartnerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.businessPartnerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
