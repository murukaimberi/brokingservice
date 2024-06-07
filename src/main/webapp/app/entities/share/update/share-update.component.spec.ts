import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IBusinessPartner } from 'app/entities/business-partner/business-partner.model';
import { BusinessPartnerService } from 'app/entities/business-partner/service/business-partner.service';
import { ShareService } from '../service/share.service';
import { IShare } from '../share.model';
import { ShareFormService } from './share-form.service';

import { ShareUpdateComponent } from './share-update.component';

describe('Share Management Update Component', () => {
  let comp: ShareUpdateComponent;
  let fixture: ComponentFixture<ShareUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shareFormService: ShareFormService;
  let shareService: ShareService;
  let businessPartnerService: BusinessPartnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ShareUpdateComponent],
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
      .overrideTemplate(ShareUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShareUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shareFormService = TestBed.inject(ShareFormService);
    shareService = TestBed.inject(ShareService);
    businessPartnerService = TestBed.inject(BusinessPartnerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BusinessPartner query and add missing value', () => {
      const share: IShare = { id: 456 };
      const reInsurer: IBusinessPartner = { id: 2458 };
      share.reInsurer = reInsurer;

      const businessPartnerCollection: IBusinessPartner[] = [{ id: 30649 }];
      jest.spyOn(businessPartnerService, 'query').mockReturnValue(of(new HttpResponse({ body: businessPartnerCollection })));
      const additionalBusinessPartners = [reInsurer];
      const expectedCollection: IBusinessPartner[] = [...additionalBusinessPartners, ...businessPartnerCollection];
      jest.spyOn(businessPartnerService, 'addBusinessPartnerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ share });
      comp.ngOnInit();

      expect(businessPartnerService.query).toHaveBeenCalled();
      expect(businessPartnerService.addBusinessPartnerToCollectionIfMissing).toHaveBeenCalledWith(
        businessPartnerCollection,
        ...additionalBusinessPartners.map(expect.objectContaining),
      );
      expect(comp.businessPartnersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const share: IShare = { id: 456 };
      const reInsurer: IBusinessPartner = { id: 11249 };
      share.reInsurer = reInsurer;

      activatedRoute.data = of({ share });
      comp.ngOnInit();

      expect(comp.businessPartnersSharedCollection).toContain(reInsurer);
      expect(comp.share).toEqual(share);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShare>>();
      const share = { id: 123 };
      jest.spyOn(shareFormService, 'getShare').mockReturnValue(share);
      jest.spyOn(shareService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ share });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: share }));
      saveSubject.complete();

      // THEN
      expect(shareFormService.getShare).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shareService.update).toHaveBeenCalledWith(expect.objectContaining(share));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShare>>();
      const share = { id: 123 };
      jest.spyOn(shareFormService, 'getShare').mockReturnValue({ id: null });
      jest.spyOn(shareService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ share: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: share }));
      saveSubject.complete();

      // THEN
      expect(shareFormService.getShare).toHaveBeenCalled();
      expect(shareService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShare>>();
      const share = { id: 123 };
      jest.spyOn(shareService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ share });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shareService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBusinessPartner', () => {
      it('Should forward to businessPartnerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(businessPartnerService, 'compareBusinessPartner');
        comp.compareBusinessPartner(entity, entity2);
        expect(businessPartnerService.compareBusinessPartner).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
