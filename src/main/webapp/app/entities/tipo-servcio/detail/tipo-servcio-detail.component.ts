import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoServcio } from '../tipo-servcio.model';

@Component({
  selector: 'jhi-tipo-servcio-detail',
  templateUrl: './tipo-servcio-detail.component.html',
})
export class TipoServcioDetailComponent implements OnInit {
  tipoServcio: ITipoServcio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoServcio }) => {
      this.tipoServcio = tipoServcio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
