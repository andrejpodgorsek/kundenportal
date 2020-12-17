import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVertrag } from 'app/shared/model/vertrag.model';

@Component({
  selector: 'jhi-vertrag-detail',
  templateUrl: './vertrag-detail.component.html',
})
export class VertragDetailComponent implements OnInit {
  vertrag: IVertrag | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vertrag }) => (this.vertrag = vertrag));
  }

  previousState(): void {
    window.history.back();
  }
}
