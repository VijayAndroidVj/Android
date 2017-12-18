import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { AddLeadPage } from './add-lead';

@NgModule({
  declarations: [
    AddLeadPage,
  ],
  imports: [
    IonicPageModule.forChild(AddLeadPage),
  ],
})
export class AddLeadPageModule {}
