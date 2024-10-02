import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgendaEmpleado } from '../agenda-empleado.model';

@Component({
  selector: 'jhi-agenda-empleado-detail',
  templateUrl: './agenda-empleado-detail.component.html',
})
export class AgendaEmpleadoDetailComponent implements OnInit {
  agendaEmpleado: IAgendaEmpleado | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agendaEmpleado }) => {
      this.agendaEmpleado = agendaEmpleado;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
