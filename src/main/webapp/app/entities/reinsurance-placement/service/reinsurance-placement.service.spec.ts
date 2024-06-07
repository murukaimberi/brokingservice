import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReinsurancePlacement } from '../reinsurance-placement.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../reinsurance-placement.test-samples';

import { ReinsurancePlacementService } from './reinsurance-placement.service';

const requireRestSample: IReinsurancePlacement = {
  ...sampleWithRequiredData,
};

describe('ReinsurancePlacement Service', () => {
  let service: ReinsurancePlacementService;
  let httpMock: HttpTestingController;
  let expectedResult: IReinsurancePlacement | IReinsurancePlacement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReinsurancePlacementService);
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

    it('should create a ReinsurancePlacement', () => {
      const reinsurancePlacement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reinsurancePlacement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReinsurancePlacement', () => {
      const reinsurancePlacement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reinsurancePlacement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReinsurancePlacement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReinsurancePlacement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReinsurancePlacement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReinsurancePlacementToCollectionIfMissing', () => {
      it('should add a ReinsurancePlacement to an empty array', () => {
        const reinsurancePlacement: IReinsurancePlacement = sampleWithRequiredData;
        expectedResult = service.addReinsurancePlacementToCollectionIfMissing([], reinsurancePlacement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reinsurancePlacement);
      });

      it('should not add a ReinsurancePlacement to an array that contains it', () => {
        const reinsurancePlacement: IReinsurancePlacement = sampleWithRequiredData;
        const reinsurancePlacementCollection: IReinsurancePlacement[] = [
          {
            ...reinsurancePlacement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReinsurancePlacementToCollectionIfMissing(reinsurancePlacementCollection, reinsurancePlacement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReinsurancePlacement to an array that doesn't contain it", () => {
        const reinsurancePlacement: IReinsurancePlacement = sampleWithRequiredData;
        const reinsurancePlacementCollection: IReinsurancePlacement[] = [sampleWithPartialData];
        expectedResult = service.addReinsurancePlacementToCollectionIfMissing(reinsurancePlacementCollection, reinsurancePlacement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reinsurancePlacement);
      });

      it('should add only unique ReinsurancePlacement to an array', () => {
        const reinsurancePlacementArray: IReinsurancePlacement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reinsurancePlacementCollection: IReinsurancePlacement[] = [sampleWithRequiredData];
        expectedResult = service.addReinsurancePlacementToCollectionIfMissing(reinsurancePlacementCollection, ...reinsurancePlacementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reinsurancePlacement: IReinsurancePlacement = sampleWithRequiredData;
        const reinsurancePlacement2: IReinsurancePlacement = sampleWithPartialData;
        expectedResult = service.addReinsurancePlacementToCollectionIfMissing([], reinsurancePlacement, reinsurancePlacement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reinsurancePlacement);
        expect(expectedResult).toContain(reinsurancePlacement2);
      });

      it('should accept null and undefined values', () => {
        const reinsurancePlacement: IReinsurancePlacement = sampleWithRequiredData;
        expectedResult = service.addReinsurancePlacementToCollectionIfMissing([], null, reinsurancePlacement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reinsurancePlacement);
      });

      it('should return initial array if no ReinsurancePlacement is added', () => {
        const reinsurancePlacementCollection: IReinsurancePlacement[] = [sampleWithRequiredData];
        expectedResult = service.addReinsurancePlacementToCollectionIfMissing(reinsurancePlacementCollection, undefined, null);
        expect(expectedResult).toEqual(reinsurancePlacementCollection);
      });
    });

    describe('compareReinsurancePlacement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReinsurancePlacement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReinsurancePlacement(entity1, entity2);
        const compareResult2 = service.compareReinsurancePlacement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReinsurancePlacement(entity1, entity2);
        const compareResult2 = service.compareReinsurancePlacement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReinsurancePlacement(entity1, entity2);
        const compareResult2 = service.compareReinsurancePlacement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
