import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoServcioComponent } from './list/tipo-servcio.component';
import { TipoServcioDetailComponent } from './detail/tipo-servcio-detail.component';
import { TipoServcioUpdateComponent } from './update/tipo-servcio-update.component';
import { TipoServcioDeleteDialogComponent } from './delete/tipo-servcio-delete-dialog.component';
import { TipoServcioRoutingModule } from './route/tipo-servcio-routing.module';

@NgModule({
  imports: [SharedModule, TipoServcioRoutingModule],
  declarations: [TipoServcioComponent, TipoServcioDetailComponent, TipoServcioUpdateComponent, TipoServcioDeleteDialogComponent],
  entryComponents: [TipoServcioDeleteDialogComponent],
})
export class TipoServcioModule {}
