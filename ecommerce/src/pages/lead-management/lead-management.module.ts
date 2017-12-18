import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { LeadManagementPage } from './lead-management';

@NgModule({
  declarations: [
    LeadManagementPage,
  ],
  imports: [
    IonicPageModule.forChild(LeadManagementPage),
  ],
})
export class LeadManagementPageModule {}
