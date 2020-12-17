import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KundenportalSharedModule } from 'app/shared/shared.module';
import { NachrichtComponent } from './nachricht.component';
import { NachrichtDetailComponent } from './nachricht-detail.component';
import { NachrichtUpdateComponent } from './nachricht-update.component';
import { NachrichtDeleteDialogComponent } from './nachricht-delete-dialog.component';
import { nachrichtRoute } from './nachricht.route';

@NgModule({
  imports: [KundenportalSharedModule, RouterModule.forChild(nachrichtRoute)],
  declarations: [NachrichtComponent, NachrichtDetailComponent, NachrichtUpdateComponent, NachrichtDeleteDialogComponent],
  entryComponents: [NachrichtDeleteDialogComponent],
})
export class KundenportalNachrichtModule {}
