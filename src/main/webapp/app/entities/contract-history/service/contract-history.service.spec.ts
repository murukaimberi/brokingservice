import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContractHistory } from '../contract-history.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../contract-history.test-samples';

import { ContractHistoryService, RestContractHistory } from './contract-history.service';

const requireRestSample: RestContractHistory = {
  ...sampleWithRequiredData,
  contractCreatedDate: sampleWithRequiredData.contractCreatedDate?.toJSON(),
  contractActiveDate: sampleWithRequiredData.contractActiveDate?.toJSON(),
  contractInActiveDate: sampleWithRequiredData.contractInActiveDate?.toJSON(),
  contractLastModifiedDate: sampleWithRequiredData.contractLastModifiedDate?.toJSON(),
};

describe('ContractHistory Service', () => {
  let service: ContractHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IContractHistory | IContractHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContractHistoryService);
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

    it('should create a ContractHistory', () => {
      const contractHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contractHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContractHistory', () => {
      const contractHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contractHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContractHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContractHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContractHistory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContractHistoryToCollectionIfMissing', () => {
      it('should add a ContractHistory to an empty array', () => {
        const contractHistory: IContractHistory = sampleWithRequiredData;
        expectedResult = service.addContractHistoryToCollectionIfMissing([], contractHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractHistory);
      });

      it('should not add a ContractHistory to an array that contains it', () => {
        const contractHistory: IContractHistory = sampleWithRequiredData;
        const contractHistoryCollection: IContractHistory[] = [
          {
            ...contractHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContractHistoryToCollectionIfMissing(contractHistoryCollection, contractHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContractHistory to an array that doesn't contain it", () => {
        const contractHistory: IContractHistory = sampleWithRequiredData;
        const contractHistoryCollection: IContractHistory[] = [sampleWithPartialData];
        expectedResult = service.addContractHistoryToCollectionIfMissing(contractHistoryCollection, contractHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractHistory);
      });

      it('should add only unique ContractHistory to an array', () => {
        const contractHistoryArray: IContractHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contractHistoryCollection: IContractHistory[] = [sampleWithRequiredData];
        expectedResult = service.addContractHistoryToCollectionIfMissing(contractHistoryCollection, ...contractHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contractHistory: IContractHistory = sampleWithRequiredData;
        const contractHistory2: IContractHistory = sampleWithPartialData;
        expectedResult = service.addContractHistoryToCollectionIfMissing([], contractHistory, contractHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractHistory);
        expect(expectedResult).toContain(contractHistory2);
      });

      it('should accept null and undefined values', () => {
        const contractHistory: IContractHistory = sampleWithRequiredData;
        expectedResult = service.addContractHistoryToCollectionIfMissing([], null, contractHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractHistory);
      });

      it('should return initial array if no ContractHistory is added', () => {
        const contractHistoryCollection: IContractHistory[] = [sampleWithRequiredData];
        expectedResult = service.addContractHistoryToCollectionIfMissing(contractHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(contractHistoryCollection);
      });
    });

    describe('compareContractHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContractHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContractHistory(entity1, entity2);
        const compareResult2 = service.compareContractHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContractHistory(entity1, entity2);
        const compareResult2 = service.compareContractHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContractHistory(entity1, entity2);
        const compareResult2 = service.compareContractHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
