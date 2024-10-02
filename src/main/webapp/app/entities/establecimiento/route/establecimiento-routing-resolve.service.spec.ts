import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEstablecimiento, Establecimiento } from '../establecimiento.model';
import { EstablecimientoService } from '../service/establecimiento.service';

import { EstablecimientoRoutingResolveService } from './establecimiento-routing-resolve.service';

describe('Establecimiento routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EstablecimientoRoutingResolveService;
  let service: EstablecimientoService;
  let resultEstablecimiento: IEstablecimiento | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
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
    routingResolveService = TestBed.inject(EstablecimientoRoutingResolveService);
    service = TestBed.inject(EstablecimientoService);
    resultEstablecimiento = undefined;
  });

  describe('resolve', () => {
    it('should return IEstablecimiento returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEstablecimiento = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEstablecimiento).toEqual({ id: 123 });
    });

    it('should return new IEstablecimiento if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEstablecimiento = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEstablecimiento).toEqual(new Establecimiento());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Establecimiento })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEstablecimiento = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEstablecimiento).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
