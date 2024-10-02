import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEstablecimiento } from '../establecimiento.model';

@Component({
  selector: 'jhi-establecimiento-detail',
  templateUrl: './establecimiento-detail.component.html',
})
export class EstablecimientoDetailComponent implements OnInit {
  establecimiento: IEstablecimiento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ establecimiento }) => {
      this.establecimiento = establecimiento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
