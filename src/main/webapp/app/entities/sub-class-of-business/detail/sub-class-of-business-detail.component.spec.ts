import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SubClassOfBusinessDetailComponent } from './sub-class-of-business-detail.component';

describe('SubClassOfBusiness Management Detail Component', () => {
  let comp: SubClassOfBusinessDetailComponent;
  let fixture: ComponentFixture<SubClassOfBusinessDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubClassOfBusinessDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SubClassOfBusinessDetailComponent,
              resolve: { subClassOfBusiness: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SubClassOfBusinessDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubClassOfBusinessDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load subClassOfBusiness on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SubClassOfBusinessDetailComponent);

      // THEN
      expect(instance.subClassOfBusiness()).toEqual(expect.objectContaining({ id: 123 }));
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
