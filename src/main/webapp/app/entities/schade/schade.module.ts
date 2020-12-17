import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KundenportalSharedModule } from 'app/shared/shared.module';
import { SchadeComponent } from './schade.component';
import { SchadeDetailComponent } from './schade-detail.component';
import { SchadeUpdateComponent } from './schade-update.component';
import { SchadeDeleteDialogComponent } from './schade-delete-dialog.component';
import { schadeRoute } from './schade.route';

@NgModule({
  imports: [KundenportalSharedModule, RouterModule.forChild(schadeRoute)],
  declarations: [SchadeComponent, SchadeDetailComponent, SchadeUpdateComponent, SchadeDeleteDialogComponent],
  entryComponents: [SchadeDeleteDialogComponent],
})
export class KundenportalSchadeModule {}
