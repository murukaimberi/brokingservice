import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReinsurancePlacementDetailComponent } from './reinsurance-placement-detail.component';

describe('ReinsurancePlacement Management Detail Component', () => {
  let comp: ReinsurancePlacementDetailComponent;
  let fixture: ComponentFixture<ReinsurancePlacementDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReinsurancePlacementDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ReinsurancePlacementDetailComponent,
              resolve: { reinsurancePlacement: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReinsurancePlacementDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReinsurancePlacementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reinsurancePlacement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReinsurancePlacementDetailComponent);

      // THEN
      expect(instance.reinsurancePlacement()).toEqual(expect.objectContaining({ id: 123 }));
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
