import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { ReinsurancePlacementService } from '../service/reinsurance-placement.service';
import { IReinsurancePlacement } from '../reinsurance-placement.model';
import { ReinsurancePlacementFormService } from './reinsurance-placement-form.service';

import { ReinsurancePlacementUpdateComponent } from './reinsurance-placement-update.component';

describe('ReinsurancePlacement Management Update Component', () => {
  let comp: ReinsurancePlacementUpdateComponent;
  let fixture: ComponentFixture<ReinsurancePlacementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reinsurancePlacementFormService: ReinsurancePlacementFormService;
  let reinsurancePlacementService: ReinsurancePlacementService;
  let contractService: ContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReinsurancePlacementUpdateComponent],
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
      .overrideTemplate(ReinsurancePlacementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReinsurancePlacementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reinsurancePlacementFormService = TestBed.inject(ReinsurancePlacementFormService);
    reinsurancePlacementService = TestBed.inject(ReinsurancePlacementService);
    contractService = TestBed.inject(ContractService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call contract query and add missing value', () => {
      const reinsurancePlacement: IReinsurancePlacement = { id: 456 };
      const contract: IContract = { id: 16455 };
      reinsurancePlacement.contract = contract;

      const contractCollection: IContract[] = [{ id: 14386 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const expectedCollection: IContract[] = [contract, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reinsurancePlacement });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(contractCollection, contract);
      expect(comp.contractsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reinsurancePlacement: IReinsurancePlacement = { id: 456 };
      const contract: IContract = { id: 6589 };
      reinsurancePlacement.contract = contract;

      activatedRoute.data = of({ reinsurancePlacement });
      comp.ngOnInit();

      expect(comp.contractsCollection).toContain(contract);
      expect(comp.reinsurancePlacement).toEqual(reinsurancePlacement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReinsurancePlacement>>();
      const reinsurancePlacement = { id: 123 };
      jest.spyOn(reinsurancePlacementFormService, 'getReinsurancePlacement').mockReturnValue(reinsurancePlacement);
      jest.spyOn(reinsurancePlacementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reinsurancePlacement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reinsurancePlacement }));
      saveSubject.complete();

      // THEN
      expect(reinsurancePlacementFormService.getReinsurancePlacement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reinsurancePlacementService.update).toHaveBeenCalledWith(expect.objectContaining(reinsurancePlacement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReinsurancePlacement>>();
      const reinsurancePlacement = { id: 123 };
      jest.spyOn(reinsurancePlacementFormService, 'getReinsurancePlacement').mockReturnValue({ id: null });
      jest.spyOn(reinsurancePlacementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reinsurancePlacement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reinsurancePlacement }));
      saveSubject.complete();

      // THEN
      expect(reinsurancePlacementFormService.getReinsurancePlacement).toHaveBeenCalled();
      expect(reinsurancePlacementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReinsurancePlacement>>();
      const reinsurancePlacement = { id: 123 };
      jest.spyOn(reinsurancePlacementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reinsurancePlacement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reinsurancePlacementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContract', () => {
      it('Should forward to contractService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contractService, 'compareContract');
        comp.compareContract(entity, entity2);
        expect(contractService.compareContract).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
