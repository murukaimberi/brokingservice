import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContractReport } from '../contract-report.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../contract-report.test-samples';

import { ContractReportService, RestContractReport } from './contract-report.service';

const requireRestSample: RestContractReport = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('ContractReport Service', () => {
  let service: ContractReportService;
  let httpMock: HttpTestingController;
  let expectedResult: IContractReport | IContractReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContractReportService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ContractReport', () => {
      const contractReport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contractReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContractReport', () => {
      const contractReport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contractReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContractReport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContractReport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContractReport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContractReportToCollectionIfMissing', () => {
      it('should add a ContractReport to an empty array', () => {
        const contractReport: IContractReport = sampleWithRequiredData;
        expectedResult = service.addContractReportToCollectionIfMissing([], contractReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractReport);
      });

      it('should not add a ContractReport to an array that contains it', () => {
        const contractReport: IContractReport = sampleWithRequiredData;
        const contractReportCollection: IContractReport[] = [
          {
            ...contractReport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContractReportToCollectionIfMissing(contractReportCollection, contractReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContractReport to an array that doesn't contain it", () => {
        const contractReport: IContractReport = sampleWithRequiredData;
        const contractReportCollection: IContractReport[] = [sampleWithPartialData];
        expectedResult = service.addContractReportToCollectionIfMissing(contractReportCollection, contractReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractReport);
      });

      it('should add only unique ContractReport to an array', () => {
        const contractReportArray: IContractReport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contractReportCollection: IContractReport[] = [sampleWithRequiredData];
        expectedResult = service.addContractReportToCollectionIfMissing(contractReportCollection, ...contractReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contractReport: IContractReport = sampleWithRequiredData;
        const contractReport2: IContractReport = sampleWithPartialData;
        expectedResult = service.addContractReportToCollectionIfMissing([], contractReport, contractReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractReport);
        expect(expectedResult).toContain(contractReport2);
      });

      it('should accept null and undefined values', () => {
        const contractReport: IContractReport = sampleWithRequiredData;
        expectedResult = service.addContractReportToCollectionIfMissing([], null, contractReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractReport);
      });

      it('should return initial array if no ContractReport is added', () => {
        const contractReportCollection: IContractReport[] = [sampleWithRequiredData];
        expectedResult = service.addContractReportToCollectionIfMissing(contractReportCollection, undefined, null);
        expect(expectedResult).toEqual(contractReportCollection);
      });
    });

    describe('compareContractReport', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContractReport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContractReport(entity1, entity2);
        const compareResult2 = service.compareContractReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContractReport(entity1, entity2);
        const compareResult2 = service.compareContractReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContractReport(entity1, entity2);
        const compareResult2 = service.compareContractReport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
