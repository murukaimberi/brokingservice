import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ISubClassOfBusiness } from '../sub-class-of-business.model';
import { SubClassOfBusinessService } from '../service/sub-class-of-business.service';

import subClassOfBusinessResolve from './sub-class-of-business-routing-resolve.service';

describe('SubClassOfBusiness routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: SubClassOfBusinessService;
  let resultSubClassOfBusiness: ISubClassOfBusiness | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(SubClassOfBusinessService);
    resultSubClassOfBusiness = undefined;
  });

  describe('resolve', () => {
    it('should return ISubClassOfBusiness returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        subClassOfBusinessResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSubClassOfBusiness = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultSubClassOfBusiness).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        subClassOfBusinessResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSubClassOfBusiness = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSubClassOfBusiness).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISubClassOfBusiness>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        subClassOfBusinessResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSubClassOfBusiness = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultSubClassOfBusiness).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
