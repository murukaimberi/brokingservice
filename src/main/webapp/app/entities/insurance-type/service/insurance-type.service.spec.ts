import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInsuranceType } from '../insurance-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../insurance-type.test-samples';

import { InsuranceTypeService } from './insurance-type.service';

const requireRestSample: IInsuranceType = {
  ...sampleWithRequiredData,
};

describe('InsuranceType Service', () => {
  let service: InsuranceTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IInsuranceType | IInsuranceType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InsuranceTypeService);
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

    it('should create a InsuranceType', () => {
      const insuranceType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(insuranceType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InsuranceType', () => {
      const insuranceType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(insuranceType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InsuranceType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InsuranceType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InsuranceType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInsuranceTypeToCollectionIfMissing', () => {
      it('should add a InsuranceType to an empty array', () => {
        const insuranceType: IInsuranceType = sampleWithRequiredData;
        expectedResult = service.addInsuranceTypeToCollectionIfMissing([], insuranceType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceType);
      });

      it('should not add a InsuranceType to an array that contains it', () => {
        const insuranceType: IInsuranceType = sampleWithRequiredData;
        const insuranceTypeCollection: IInsuranceType[] = [
          {
            ...insuranceType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInsuranceTypeToCollectionIfMissing(insuranceTypeCollection, insuranceType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InsuranceType to an array that doesn't contain it", () => {
        const insuranceType: IInsuranceType = sampleWithRequiredData;
        const insuranceTypeCollection: IInsuranceType[] = [sampleWithPartialData];
        expectedResult = service.addInsuranceTypeToCollectionIfMissing(insuranceTypeCollection, insuranceType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceType);
      });

      it('should add only unique InsuranceType to an array', () => {
        const insuranceTypeArray: IInsuranceType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const insuranceTypeCollection: IInsuranceType[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceTypeToCollectionIfMissing(insuranceTypeCollection, ...insuranceTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const insuranceType: IInsuranceType = sampleWithRequiredData;
        const insuranceType2: IInsuranceType = sampleWithPartialData;
        expectedResult = service.addInsuranceTypeToCollectionIfMissing([], insuranceType, insuranceType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(insuranceType);
        expect(expectedResult).toContain(insuranceType2);
      });

      it('should accept null and undefined values', () => {
        const insuranceType: IInsuranceType = sampleWithRequiredData;
        expectedResult = service.addInsuranceTypeToCollectionIfMissing([], null, insuranceType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(insuranceType);
      });

      it('should return initial array if no InsuranceType is added', () => {
        const insuranceTypeCollection: IInsuranceType[] = [sampleWithRequiredData];
        expectedResult = service.addInsuranceTypeToCollectionIfMissing(insuranceTypeCollection, undefined, null);
        expect(expectedResult).toEqual(insuranceTypeCollection);
      });
    });

    describe('compareInsuranceType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInsuranceType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInsuranceType(entity1, entity2);
        const compareResult2 = service.compareInsuranceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInsuranceType(entity1, entity2);
        const compareResult2 = service.compareInsuranceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInsuranceType(entity1, entity2);
        const compareResult2 = service.compareInsuranceType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
