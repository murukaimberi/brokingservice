import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IShare } from '../share.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../share.test-samples';

import { ShareService } from './share.service';

const requireRestSample: IShare = {
  ...sampleWithRequiredData,
};

describe('Share Service', () => {
  let service: ShareService;
  let httpMock: HttpTestingController;
  let expectedResult: IShare | IShare[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShareService);
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

    it('should create a Share', () => {
      const share = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(share).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Share', () => {
      const share = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(share).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Share', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Share', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Share', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addShareToCollectionIfMissing', () => {
      it('should add a Share to an empty array', () => {
        const share: IShare = sampleWithRequiredData;
        expectedResult = service.addShareToCollectionIfMissing([], share);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(share);
      });

      it('should not add a Share to an array that contains it', () => {
        const share: IShare = sampleWithRequiredData;
        const shareCollection: IShare[] = [
          {
            ...share,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShareToCollectionIfMissing(shareCollection, share);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Share to an array that doesn't contain it", () => {
        const share: IShare = sampleWithRequiredData;
        const shareCollection: IShare[] = [sampleWithPartialData];
        expectedResult = service.addShareToCollectionIfMissing(shareCollection, share);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(share);
      });

      it('should add only unique Share to an array', () => {
        const shareArray: IShare[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shareCollection: IShare[] = [sampleWithRequiredData];
        expectedResult = service.addShareToCollectionIfMissing(shareCollection, ...shareArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const share: IShare = sampleWithRequiredData;
        const share2: IShare = sampleWithPartialData;
        expectedResult = service.addShareToCollectionIfMissing([], share, share2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(share);
        expect(expectedResult).toContain(share2);
      });

      it('should accept null and undefined values', () => {
        const share: IShare = sampleWithRequiredData;
        expectedResult = service.addShareToCollectionIfMissing([], null, share, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(share);
      });

      it('should return initial array if no Share is added', () => {
        const shareCollection: IShare[] = [sampleWithRequiredData];
        expectedResult = service.addShareToCollectionIfMissing(shareCollection, undefined, null);
        expect(expectedResult).toEqual(shareCollection);
      });
    });

    describe('compareShare', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShare(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareShare(entity1, entity2);
        const compareResult2 = service.compareShare(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareShare(entity1, entity2);
        const compareResult2 = service.compareShare(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareShare(entity1, entity2);
        const compareResult2 = service.compareShare(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
