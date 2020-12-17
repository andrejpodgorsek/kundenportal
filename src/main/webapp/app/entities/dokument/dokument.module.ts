import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KundenportalSharedModule } from 'app/shared/shared.module';
import { DokumentComponent } from './dokument.component';
import { DokumentDetailComponent } from './dokument-detail.component';
import { DokumentUpdateComponent } from './dokument-update.component';
import { DokumentDeleteDialogComponent } from './dokument-delete-dialog.component';
import { dokumentRoute } from './dokument.route';

@NgModule({
  imports: [KundenportalSharedModule, RouterModule.forChild(dokumentRoute)],
  declarations: [DokumentComponent, DokumentDetailComponent, DokumentUpdateComponent, DokumentDeleteDialogComponent],
  entryComponents: [DokumentDeleteDialogComponent],
})
export class KundenportalDokumentModule {}
