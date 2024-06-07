import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ContractHistoryService } from '../service/contract-history.service';
import { IContractHistory } from '../contract-history.model';
import { ContractHistoryFormService } from './contract-history-form.service';

import { ContractHistoryUpdateComponent } from './contract-history-update.component';

describe('ContractHistory Management Update Component', () => {
  let comp: ContractHistoryUpdateComponent;
  let fixture: ComponentFixture<ContractHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractHistoryFormService: ContractHistoryFormService;
  let contractHistoryService: ContractHistoryService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ContractHistoryUpdateComponent],
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
      .overrideTemplate(ContractHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractHistoryFormService = TestBed.inject(ContractHistoryFormService);
    contractHistoryService = TestBed.inject(ContractHistoryService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const contractHistory: IContractHistory = { id: 456 };
      const updated: IUser = { id: 20473 };
      contractHistory.updated = updated;
      const approved: IUser = { id: 19629 };
      contractHistory.approved = approved;

      const userCollection: IUser[] = [{ id: 17476 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [updated, approved];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractHistory });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contractHistory: IContractHistory = { id: 456 };
      const updated: IUser = { id: 27282 };
      contractHistory.updated = updated;
      const approved: IUser = { id: 17968 };
      contractHistory.approved = approved;

      activatedRoute.data = of({ contractHistory });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(updated);
      expect(comp.usersSharedCollection).toContain(approved);
      expect(comp.contractHistory).toEqual(contractHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractHistory>>();
      const contractHistory = { id: 123 };
      jest.spyOn(contractHistoryFormService, 'getContractHistory').mockReturnValue(contractHistory);
      jest.spyOn(contractHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractHistory }));
      saveSubject.complete();

      // THEN
      expect(contractHistoryFormService.getContractHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(contractHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractHistory>>();
      const contractHistory = { id: 123 };
      jest.spyOn(contractHistoryFormService, 'getContractHistory').mockReturnValue({ id: null });
      jest.spyOn(contractHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractHistory }));
      saveSubject.complete();

      // THEN
      expect(contractHistoryFormService.getContractHistory).toHaveBeenCalled();
      expect(contractHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractHistory>>();
      const contractHistory = { id: 123 };
      jest.spyOn(contractHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
