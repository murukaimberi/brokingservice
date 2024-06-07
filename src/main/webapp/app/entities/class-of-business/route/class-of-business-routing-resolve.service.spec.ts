import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IClassOfBusiness } from '../class-of-business.model';
import { ClassOfBusinessService } from '../service/class-of-business.service';

import classOfBusinessResolve from './class-of-business-routing-resolve.service';

describe('ClassOfBusiness routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ClassOfBusinessService;
  let resultClassOfBusiness: IClassOfBusiness | null | undefined;

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
    service = TestBed.inject(ClassOfBusinessService);
    resultClassOfBusiness = undefined;
  });

  describe('resolve', () => {
    it('should return IClassOfBusiness returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        classOfBusinessResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultClassOfBusiness = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultClassOfBusiness).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        classOfBusinessResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultClassOfBusiness = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultClassOfBusiness).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IClassOfBusiness>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        classOfBusinessResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultClassOfBusiness = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultClassOfBusiness).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
