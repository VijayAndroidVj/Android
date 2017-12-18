import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { StockReportPage } from './stock-report';

@NgModule({
  declarations: [
    StockReportPage,
  ],
  imports: [
    IonicPageModule.forChild(StockReportPage),
  ],
})
export class StockReportPageModule {}
