import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { InsuranceTypeDetailComponent } from './insurance-type-detail.component';

describe('InsuranceType Management Detail Component', () => {
  let comp: InsuranceTypeDetailComponent;
  let fixture: ComponentFixture<InsuranceTypeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InsuranceTypeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InsuranceTypeDetailComponent,
              resolve: { insuranceType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InsuranceTypeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InsuranceTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load insuranceType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InsuranceTypeDetailComponent);

      // THEN
      expect(instance.insuranceType()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
