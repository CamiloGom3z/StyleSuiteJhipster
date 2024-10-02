import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReseniaComponent } from './list/resenia.component';
import { ReseniaDetailComponent } from './detail/resenia-detail.component';
import { ReseniaUpdateComponent } from './update/resenia-update.component';
import { ReseniaDeleteDialogComponent } from './delete/resenia-delete-dialog.component';
import { ReseniaRoutingModule } from './route/resenia-routing.module';

@NgModule({
  imports: [SharedModule, ReseniaRoutingModule],
  declarations: [ReseniaComponent, ReseniaDetailComponent, ReseniaUpdateComponent, ReseniaDeleteDialogComponent],
  entryComponents: [ReseniaDeleteDialogComponent],
})
export class ReseniaModule {}
