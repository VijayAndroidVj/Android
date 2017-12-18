import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ServiceReportPage } from './service-report';

@NgModule({
  declarations: [
    ServiceReportPage,
  ],
  imports: [
    IonicPageModule.forChild(ServiceReportPage),
  ],
})
export class ServiceReportPageModule {}
