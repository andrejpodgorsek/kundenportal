import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KundenportalSharedModule } from 'app/shared/shared.module';
import { KundeComponent } from './kunde.component';
import { KundeDetailComponent } from './kunde-detail.component';
import { KundeUpdateComponent } from './kunde-update.component';
import { KundeDeleteDialogComponent } from './kunde-delete-dialog.component';
import { kundeRoute } from './kunde.route';

@NgModule({
  imports: [KundenportalSharedModule, RouterModule.forChild(kundeRoute)],
  declarations: [KundeComponent, KundeDetailComponent, KundeUpdateComponent, KundeDeleteDialogComponent],
  entryComponents: [KundeDeleteDialogComponent],
})
export class KundenportalKundeModule {}
