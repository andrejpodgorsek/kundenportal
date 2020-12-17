import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'kunde',
        loadChildren: () => import('./kunde/kunde.module').then(m => m.KundenportalKundeModule),
      },
      {
        path: 'vertrag',
        loadChildren: () => import('./vertrag/vertrag.module').then(m => m.KundenportalVertragModule),
      },
      {
        path: 'dokument',
        loadChildren: () => import('./dokument/dokument.module').then(m => m.KundenportalDokumentModule),
      },
      {
        path: 'nachricht',
        loadChildren: () => import('./nachricht/nachricht.module').then(m => m.KundenportalNachrichtModule),
      },
      {
        path: 'schade',
        loadChildren: () => import('./schade/schade.module').then(m => m.KundenportalSchadeModule),
      },
      {
        path: 'agentur',
        loadChildren: () => import('./agentur/agentur.module').then(m => m.KundenportalAgenturModule),
      },
      {
        path: 'self-services',
        loadChildren: () => import('./self-services/self-services.module').then(m => m.KundenportalSelfServicesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class KundenportalEntityModule {}
