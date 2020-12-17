import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgentur } from 'app/shared/model/agentur.model';

@Component({
  selector: 'jhi-agentur-detail',
  templateUrl: './agentur-detail.component.html',
})
export class AgenturDetailComponent implements OnInit {
  agentur: IAgentur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agentur }) => (this.agentur = agentur));
  }

  previousState(): void {
    window.history.back();
  }
}
