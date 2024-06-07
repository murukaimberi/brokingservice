import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IShare } from '../share.model';
import { ShareService } from '../service/share.service';

@Component({
  standalone: true,
  templateUrl: './share-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ShareDeleteDialogComponent {
  share?: IShare;

  protected shareService = inject(ShareService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shareService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
