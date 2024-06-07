import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClassOfBusinessDetailComponent } from './class-of-business-detail.component';

describe('ClassOfBusiness Management Detail Component', () => {
  let comp: ClassOfBusinessDetailComponent;
  let fixture: ComponentFixture<ClassOfBusinessDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClassOfBusinessDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ClassOfBusinessDetailComponent,
              resolve: { classOfBusiness: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClassOfBusinessDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClassOfBusinessDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load classOfBusiness on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClassOfBusinessDetailComponent);

      // THEN
      expect(instance.classOfBusiness()).toEqual(expect.objectContaining({ id: 123 }));
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
