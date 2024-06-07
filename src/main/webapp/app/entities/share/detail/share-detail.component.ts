import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IShare } from '../share.model';

@Component({
  standalone: true,
  selector: 'jhi-share-detail',
  templateUrl: './share-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ShareDetailComponent {
  share = input<IShare | null>(null);

  previousState(): void {
    window.history.back();
  }
}
