import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResenia } from '../resenia.model';

@Component({
  selector: 'jhi-resenia-detail',
  templateUrl: './resenia-detail.component.html',
})
export class ReseniaDetailComponent implements OnInit {
  resenia: IResenia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resenia }) => {
      this.resenia = resenia;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
