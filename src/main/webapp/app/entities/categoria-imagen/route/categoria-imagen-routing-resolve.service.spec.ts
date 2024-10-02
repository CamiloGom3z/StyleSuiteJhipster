import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICategoriaImagen, CategoriaImagen } from '../categoria-imagen.model';
import { CategoriaImagenService } from '../service/categoria-imagen.service';

import { CategoriaImagenRoutingResolveService } from './categoria-imagen-routing-resolve.service';

describe('CategoriaImagen routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CategoriaImagenRoutingResolveService;
  let service: CategoriaImagenService;
  let resultCategoriaImagen: ICategoriaImagen | undefined;

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
    routingResolveService = TestBed.inject(CategoriaImagenRoutingResolveService);
    service = TestBed.inject(CategoriaImagenService);
    resultCategoriaImagen = undefined;
  });

  describe('resolve', () => {
    it('should return ICategoriaImagen returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaImagen = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaImagen).toEqual({ id: 123 });
    });

    it('should return new ICategoriaImagen if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaImagen = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCategoriaImagen).toEqual(new CategoriaImagen());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CategoriaImagen })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaImagen = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaImagen).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
