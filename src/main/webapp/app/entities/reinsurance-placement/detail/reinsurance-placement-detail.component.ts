import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReinsurancePlacement } from '../reinsurance-placement.model';

@Component({
  standalone: true,
  selector: 'jhi-reinsurance-placement-detail',
  templateUrl: './reinsurance-placement-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReinsurancePlacementDetailComponent {
  reinsurancePlacement = input<IReinsurancePlacement | null>(null);

  previousState(): void {
    window.history.back();
  }
}
