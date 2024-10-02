import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImagen } from '../imagen.model';

@Component({
  selector: 'jhi-imagen-detail',
  templateUrl: './imagen-detail.component.html',
})
export class ImagenDetailComponent implements OnInit {
  imagen: IImagen | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imagen }) => {
      this.imagen = imagen;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
