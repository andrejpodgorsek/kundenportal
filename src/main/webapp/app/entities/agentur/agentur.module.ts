import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KundenportalSharedModule } from 'app/shared/shared.module';
import { AgenturComponent } from './agentur.component';
import { AgenturDetailComponent } from './agentur-detail.component';
import { AgenturUpdateComponent } from './agentur-update.component';
import { AgenturDeleteDialogComponent } from './agentur-delete-dialog.component';
import { agenturRoute } from './agentur.route';

@NgModule({
  imports: [KundenportalSharedModule, RouterModule.forChild(agenturRoute)],
  declarations: [AgenturComponent, AgenturDetailComponent, AgenturUpdateComponent, AgenturDeleteDialogComponent],
  entryComponents: [AgenturDeleteDialogComponent],
})
export class KundenportalAgenturModule {}
