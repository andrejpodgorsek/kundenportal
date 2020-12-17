import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KundenportalSharedModule } from 'app/shared/shared.module';
import { VertragComponent } from './vertrag.component';
import { VertragDetailComponent } from './vertrag-detail.component';
import { VertragUpdateComponent } from './vertrag-update.component';
import { VertragDeleteDialogComponent } from './vertrag-delete-dialog.component';
import { vertragRoute } from './vertrag.route';

@NgModule({
  imports: [KundenportalSharedModule, RouterModule.forChild(vertragRoute)],
  declarations: [VertragComponent, VertragDetailComponent, VertragUpdateComponent, VertragDeleteDialogComponent],
  entryComponents: [VertragDeleteDialogComponent],
})
export class KundenportalVertragModule {}
