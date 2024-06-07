import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IContractHistory } from '../contract-history.model';
import { ContractHistoryService } from '../service/contract-history.service';

@Component({
  standalone: true,
  templateUrl: './contract-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ContractHistoryDeleteDialogComponent {
  contractHistory?: IContractHistory;

  protected contractHistoryService = inject(ContractHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
