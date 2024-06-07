import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IContractReport } from '../contract-report.model';
import { ContractReportService } from '../service/contract-report.service';

@Component({
  standalone: true,
  templateUrl: './contract-report-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ContractReportDeleteDialogComponent {
  contractReport?: IContractReport;

  protected contractReportService = inject(ContractReportService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractReportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
