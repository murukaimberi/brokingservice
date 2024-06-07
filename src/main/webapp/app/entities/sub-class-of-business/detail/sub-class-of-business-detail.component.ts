import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISubClassOfBusiness } from '../sub-class-of-business.model';

@Component({
  standalone: true,
  selector: 'jhi-sub-class-of-business-detail',
  templateUrl: './sub-class-of-business-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SubClassOfBusinessDetailComponent {
  subClassOfBusiness = input<ISubClassOfBusiness | null>(null);

  previousState(): void {
    window.history.back();
  }
}
