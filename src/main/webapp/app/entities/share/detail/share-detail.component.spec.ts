import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ShareDetailComponent } from './share-detail.component';

describe('Share Management Detail Component', () => {
  let comp: ShareDetailComponent;
  let fixture: ComponentFixture<ShareDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShareDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ShareDetailComponent,
              resolve: { share: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ShareDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShareDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load share on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ShareDetailComponent);

      // THEN
      expect(instance.share()).toEqual(expect.objectContaining({ id: 123 }));
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
