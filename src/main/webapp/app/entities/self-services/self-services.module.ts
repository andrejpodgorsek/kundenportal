import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KundenportalSharedModule } from 'app/shared/shared.module';
import { SelfServicesComponent } from './self-services.component';
import { SelfServicesDetailComponent } from './self-services-detail.component';
import { SelfServicesUpdateComponent } from './self-services-update.component';
import { SelfServicesDeleteDialogComponent } from './self-services-delete-dialog.component';
import { selfServicesRoute } from './self-services.route';

@NgModule({
  imports: [KundenportalSharedModule, RouterModule.forChild(selfServicesRoute)],
  declarations: [SelfServicesComponent, SelfServicesDetailComponent, SelfServicesUpdateComponent, SelfServicesDeleteDialogComponent],
  entryComponents: [SelfServicesDeleteDialogComponent],
})
export class KundenportalSelfServicesModule {}
