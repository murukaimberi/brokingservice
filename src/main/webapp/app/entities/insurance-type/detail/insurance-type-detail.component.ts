import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInsuranceType } from '../insurance-type.model';

@Component({
  standalone: true,
  selector: 'jhi-insurance-type-detail',
  templateUrl: './insurance-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InsuranceTypeDetailComponent {
  insuranceType = input<IInsuranceType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
