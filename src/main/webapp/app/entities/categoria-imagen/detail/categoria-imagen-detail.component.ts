import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategoriaImagen } from '../categoria-imagen.model';

@Component({
  selector: 'jhi-categoria-imagen-detail',
  templateUrl: './categoria-imagen-detail.component.html',
})
export class CategoriaImagenDetailComponent implements OnInit {
  categoriaImagen: ICategoriaImagen | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaImagen }) => {
      this.categoriaImagen = categoriaImagen;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
