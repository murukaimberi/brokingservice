import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ContractReportService } from '../service/contract-report.service';
import { IContractReport } from '../contract-report.model';
import { ContractReportFormService } from './contract-report-form.service';

import { ContractReportUpdateComponent } from './contract-report-update.component';

describe('ContractReport Management Update Component', () => {
  let comp: ContractReportUpdateComponent;
  let fixture: ComponentFixture<ContractReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractReportFormService: ContractReportFormService;
  let contractReportService: ContractReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ContractReportUpdateComponent],
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
      .overrideTemplate(ContractReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractReportFormService = TestBed.inject(ContractReportFormService);
    contractReportService = TestBed.inject(ContractReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contractReport: IContractReport = { id: 456 };

      activatedRoute.data = of({ contractReport });
      comp.ngOnInit();

      expect(comp.contractReport).toEqual(contractReport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractReport>>();
      const contractReport = { id: 123 };
      jest.spyOn(contractReportFormService, 'getContractReport').mockReturnValue(contractReport);
      jest.spyOn(contractReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractReport }));
      saveSubject.complete();

      // THEN
      expect(contractReportFormService.getContractReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractReportService.update).toHaveBeenCalledWith(expect.objectContaining(contractReport));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractReport>>();
      const contractReport = { id: 123 };
      jest.spyOn(contractReportFormService, 'getContractReport').mockReturnValue({ id: null });
      jest.spyOn(contractReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractReport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractReport }));
      saveSubject.complete();

      // THEN
      expect(contractReportFormService.getContractReport).toHaveBeenCalled();
      expect(contractReportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractReport>>();
      const contractReport = { id: 123 };
      jest.spyOn(contractReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractReportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
