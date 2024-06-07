import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IClassOfBusiness } from '../class-of-business.model';

@Component({
  standalone: true,
  selector: 'jhi-class-of-business-detail',
  templateUrl: './class-of-business-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ClassOfBusinessDetailComponent {
  classOfBusiness = input<IClassOfBusiness | null>(null);

  previousState(): void {
    window.history.back();
  }
}
