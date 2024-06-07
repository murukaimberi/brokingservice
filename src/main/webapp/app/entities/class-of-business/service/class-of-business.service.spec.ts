import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClassOfBusiness } from '../class-of-business.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../class-of-business.test-samples';

import { ClassOfBusinessService } from './class-of-business.service';

const requireRestSample: IClassOfBusiness = {
  ...sampleWithRequiredData,
};

describe('ClassOfBusiness Service', () => {
  let service: ClassOfBusinessService;
  let httpMock: HttpTestingController;
  let expectedResult: IClassOfBusiness | IClassOfBusiness[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClassOfBusinessService);
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

    it('should create a ClassOfBusiness', () => {
      const classOfBusiness = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(classOfBusiness).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ClassOfBusiness', () => {
      const classOfBusiness = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(classOfBusiness).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ClassOfBusiness', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ClassOfBusiness', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ClassOfBusiness', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addClassOfBusinessToCollectionIfMissing', () => {
      it('should add a ClassOfBusiness to an empty array', () => {
        const classOfBusiness: IClassOfBusiness = sampleWithRequiredData;
        expectedResult = service.addClassOfBusinessToCollectionIfMissing([], classOfBusiness);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classOfBusiness);
      });

      it('should not add a ClassOfBusiness to an array that contains it', () => {
        const classOfBusiness: IClassOfBusiness = sampleWithRequiredData;
        const classOfBusinessCollection: IClassOfBusiness[] = [
          {
            ...classOfBusiness,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addClassOfBusinessToCollectionIfMissing(classOfBusinessCollection, classOfBusiness);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ClassOfBusiness to an array that doesn't contain it", () => {
        const classOfBusiness: IClassOfBusiness = sampleWithRequiredData;
        const classOfBusinessCollection: IClassOfBusiness[] = [sampleWithPartialData];
        expectedResult = service.addClassOfBusinessToCollectionIfMissing(classOfBusinessCollection, classOfBusiness);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classOfBusiness);
      });

      it('should add only unique ClassOfBusiness to an array', () => {
        const classOfBusinessArray: IClassOfBusiness[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const classOfBusinessCollection: IClassOfBusiness[] = [sampleWithRequiredData];
        expectedResult = service.addClassOfBusinessToCollectionIfMissing(classOfBusinessCollection, ...classOfBusinessArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const classOfBusiness: IClassOfBusiness = sampleWithRequiredData;
        const classOfBusiness2: IClassOfBusiness = sampleWithPartialData;
        expectedResult = service.addClassOfBusinessToCollectionIfMissing([], classOfBusiness, classOfBusiness2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classOfBusiness);
        expect(expectedResult).toContain(classOfBusiness2);
      });

      it('should accept null and undefined values', () => {
        const classOfBusiness: IClassOfBusiness = sampleWithRequiredData;
        expectedResult = service.addClassOfBusinessToCollectionIfMissing([], null, classOfBusiness, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classOfBusiness);
      });

      it('should return initial array if no ClassOfBusiness is added', () => {
        const classOfBusinessCollection: IClassOfBusiness[] = [sampleWithRequiredData];
        expectedResult = service.addClassOfBusinessToCollectionIfMissing(classOfBusinessCollection, undefined, null);
        expect(expectedResult).toEqual(classOfBusinessCollection);
      });
    });

    describe('compareClassOfBusiness', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareClassOfBusiness(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareClassOfBusiness(entity1, entity2);
        const compareResult2 = service.compareClassOfBusiness(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareClassOfBusiness(entity1, entity2);
        const compareResult2 = service.compareClassOfBusiness(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareClassOfBusiness(entity1, entity2);
        const compareResult2 = service.compareClassOfBusiness(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
