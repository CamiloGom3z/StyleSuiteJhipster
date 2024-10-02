import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICita } from '../cita.model';

@Component({
  selector: 'jhi-cita-detail',
  templateUrl: './cita-detail.component.html',
})
export class CitaDetailComponent implements OnInit {
  cita: ICita | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cita }) => {
      this.cita = cita;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
