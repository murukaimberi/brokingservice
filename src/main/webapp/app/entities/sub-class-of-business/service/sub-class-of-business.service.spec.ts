import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISubClassOfBusiness } from '../sub-class-of-business.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../sub-class-of-business.test-samples';

import { SubClassOfBusinessService } from './sub-class-of-business.service';

const requireRestSample: ISubClassOfBusiness = {
  ...sampleWithRequiredData,
};

describe('SubClassOfBusiness Service', () => {
  let service: SubClassOfBusinessService;
  let httpMock: HttpTestingController;
  let expectedResult: ISubClassOfBusiness | ISubClassOfBusiness[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SubClassOfBusinessService);
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

    it('should create a SubClassOfBusiness', () => {
      const subClassOfBusiness = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(subClassOfBusiness).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SubClassOfBusiness', () => {
      const subClassOfBusiness = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(subClassOfBusiness).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SubClassOfBusiness', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SubClassOfBusiness', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SubClassOfBusiness', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSubClassOfBusinessToCollectionIfMissing', () => {
      it('should add a SubClassOfBusiness to an empty array', () => {
        const subClassOfBusiness: ISubClassOfBusiness = sampleWithRequiredData;
        expectedResult = service.addSubClassOfBusinessToCollectionIfMissing([], subClassOfBusiness);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subClassOfBusiness);
      });

      it('should not add a SubClassOfBusiness to an array that contains it', () => {
        const subClassOfBusiness: ISubClassOfBusiness = sampleWithRequiredData;
        const subClassOfBusinessCollection: ISubClassOfBusiness[] = [
          {
            ...subClassOfBusiness,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSubClassOfBusinessToCollectionIfMissing(subClassOfBusinessCollection, subClassOfBusiness);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SubClassOfBusiness to an array that doesn't contain it", () => {
        const subClassOfBusiness: ISubClassOfBusiness = sampleWithRequiredData;
        const subClassOfBusinessCollection: ISubClassOfBusiness[] = [sampleWithPartialData];
        expectedResult = service.addSubClassOfBusinessToCollectionIfMissing(subClassOfBusinessCollection, subClassOfBusiness);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subClassOfBusiness);
      });

      it('should add only unique SubClassOfBusiness to an array', () => {
        const subClassOfBusinessArray: ISubClassOfBusiness[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const subClassOfBusinessCollection: ISubClassOfBusiness[] = [sampleWithRequiredData];
        expectedResult = service.addSubClassOfBusinessToCollectionIfMissing(subClassOfBusinessCollection, ...subClassOfBusinessArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const subClassOfBusiness: ISubClassOfBusiness = sampleWithRequiredData;
        const subClassOfBusiness2: ISubClassOfBusiness = sampleWithPartialData;
        expectedResult = service.addSubClassOfBusinessToCollectionIfMissing([], subClassOfBusiness, subClassOfBusiness2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subClassOfBusiness);
        expect(expectedResult).toContain(subClassOfBusiness2);
      });

      it('should accept null and undefined values', () => {
        const subClassOfBusiness: ISubClassOfBusiness = sampleWithRequiredData;
        expectedResult = service.addSubClassOfBusinessToCollectionIfMissing([], null, subClassOfBusiness, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subClassOfBusiness);
      });

      it('should return initial array if no SubClassOfBusiness is added', () => {
        const subClassOfBusinessCollection: ISubClassOfBusiness[] = [sampleWithRequiredData];
        expectedResult = service.addSubClassOfBusinessToCollectionIfMissing(subClassOfBusinessCollection, undefined, null);
        expect(expectedResult).toEqual(subClassOfBusinessCollection);
      });
    });

    describe('compareSubClassOfBusiness', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSubClassOfBusiness(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSubClassOfBusiness(entity1, entity2);
        const compareResult2 = service.compareSubClassOfBusiness(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSubClassOfBusiness(entity1, entity2);
        const compareResult2 = service.compareSubClassOfBusiness(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSubClassOfBusiness(entity1, entity2);
        const compareResult2 = service.compareSubClassOfBusiness(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
