import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInsuranceType } from '../insurance-type.model';
import { InsuranceTypeService } from '../service/insurance-type.service';

@Component({
  standalone: true,
  templateUrl: './insurance-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InsuranceTypeDeleteDialogComponent {
  insuranceType?: IInsuranceType;

  protected insuranceTypeService = inject(InsuranceTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.insuranceTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
