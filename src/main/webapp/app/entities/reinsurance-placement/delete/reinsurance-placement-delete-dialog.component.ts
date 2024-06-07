import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReinsurancePlacement } from '../reinsurance-placement.model';
import { ReinsurancePlacementService } from '../service/reinsurance-placement.service';

@Component({
  standalone: true,
  templateUrl: './reinsurance-placement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReinsurancePlacementDeleteDialogComponent {
  reinsurancePlacement?: IReinsurancePlacement;

  protected reinsurancePlacementService = inject(ReinsurancePlacementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reinsurancePlacementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
