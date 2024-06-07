import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IClassOfBusiness } from 'app/entities/class-of-business/class-of-business.model';
import { ClassOfBusinessService } from 'app/entities/class-of-business/service/class-of-business.service';
import { SubClassOfBusinessService } from '../service/sub-class-of-business.service';
import { ISubClassOfBusiness } from '../sub-class-of-business.model';
import { SubClassOfBusinessFormService } from './sub-class-of-business-form.service';

import { SubClassOfBusinessUpdateComponent } from './sub-class-of-business-update.component';

describe('SubClassOfBusiness Management Update Component', () => {
  let comp: SubClassOfBusinessUpdateComponent;
  let fixture: ComponentFixture<SubClassOfBusinessUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subClassOfBusinessFormService: SubClassOfBusinessFormService;
  let subClassOfBusinessService: SubClassOfBusinessService;
  let classOfBusinessService: ClassOfBusinessService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SubClassOfBusinessUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SubClassOfBusinessUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubClassOfBusinessUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subClassOfBusinessFormService = TestBed.inject(SubClassOfBusinessFormService);
    subClassOfBusinessService = TestBed.inject(SubClassOfBusinessService);
    classOfBusinessService = TestBed.inject(ClassOfBusinessService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ClassOfBusiness query and add missing value', () => {
      const subClassOfBusiness: ISubClassOfBusiness = { id: 456 };
      const classOfBusiness: IClassOfBusiness = { id: 26949 };
      subClassOfBusiness.classOfBusiness = classOfBusiness;

      const classOfBusinessCollection: IClassOfBusiness[] = [{ id: 15686 }];
      jest.spyOn(classOfBusinessService, 'query').mockReturnValue(of(new HttpResponse({ body: classOfBusinessCollection })));
      const additionalClassOfBusinesses = [classOfBusiness];
      const expectedCollection: IClassOfBusiness[] = [...additionalClassOfBusinesses, ...classOfBusinessCollection];
      jest.spyOn(classOfBusinessService, 'addClassOfBusinessToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ subClassOfBusiness });
      comp.ngOnInit();

      expect(classOfBusinessService.query).toHaveBeenCalled();
      expect(classOfBusinessService.addClassOfBusinessToCollectionIfMissing).toHaveBeenCalledWith(
        classOfBusinessCollection,
        ...additionalClassOfBusinesses.map(expect.objectContaining),
      );
      expect(comp.classOfBusinessesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const subClassOfBusiness: ISubClassOfBusiness = { id: 456 };
      const classOfBusiness: IClassOfBusiness = { id: 6997 };
      subClassOfBusiness.classOfBusiness = classOfBusiness;

      activatedRoute.data = of({ subClassOfBusiness });
      comp.ngOnInit();

      expect(comp.classOfBusinessesSharedCollection).toContain(classOfBusiness);
      expect(comp.subClassOfBusiness).toEqual(subClassOfBusiness);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubClassOfBusiness>>();
      const subClassOfBusiness = { id: 123 };
      jest.spyOn(subClassOfBusinessFormService, 'getSubClassOfBusiness').mockReturnValue(subClassOfBusiness);
      jest.spyOn(subClassOfBusinessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subClassOfBusiness });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subClassOfBusiness }));
      saveSubject.complete();

      // THEN
      expect(subClassOfBusinessFormService.getSubClassOfBusiness).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(subClassOfBusinessService.update).toHaveBeenCalledWith(expect.objectContaining(subClassOfBusiness));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubClassOfBusiness>>();
      const subClassOfBusiness = { id: 123 };
      jest.spyOn(subClassOfBusinessFormService, 'getSubClassOfBusiness').mockReturnValue({ id: null });
      jest.spyOn(subClassOfBusinessService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subClassOfBusiness: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subClassOfBusiness }));
      saveSubject.complete();

      // THEN
      expect(subClassOfBusinessFormService.getSubClassOfBusiness).toHaveBeenCalled();
      expect(subClassOfBusinessService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubClassOfBusiness>>();
      const subClassOfBusiness = { id: 123 };
      jest.spyOn(subClassOfBusinessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subClassOfBusiness });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subClassOfBusinessService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClassOfBusiness', () => {
      it('Should forward to classOfBusinessService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(classOfBusinessService, 'compareClassOfBusiness');
        comp.compareClassOfBusiness(entity, entity2);
        expect(classOfBusinessService.compareClassOfBusiness).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
