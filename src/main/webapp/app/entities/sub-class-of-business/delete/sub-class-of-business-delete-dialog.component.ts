import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISubClassOfBusiness } from '../sub-class-of-business.model';
import { SubClassOfBusinessService } from '../service/sub-class-of-business.service';

@Component({
  standalone: true,
  templateUrl: './sub-class-of-business-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SubClassOfBusinessDeleteDialogComponent {
  subClassOfBusiness?: ISubClassOfBusiness;

  protected subClassOfBusinessService = inject(SubClassOfBusinessService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subClassOfBusinessService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
