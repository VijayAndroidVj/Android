import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';

import { MyApp } from './app.component';
import { HomePage } from '../pages/home/home';
import { LoginPage } from '../pages/login/login';
import { FileTransfer, FileUploadOptions, FileTransferObject } from '@ionic-native/file-transfer';
import { File } from '@ionic-native/file';
import { Camera } from '@ionic-native/camera';
import { FileChooser } from '@ionic-native/file-chooser';
import { RestProvider } from '../providers/rest/rest';
import { LeadDetailsPage } from '../pages/lead-details/lead-details';
import { AddBranchPage } from '../pages/add-branch/add-branch';
import { AddLeadPage } from '../pages/add-lead/add-lead';
import { HttpClientModule } from '@angular/common/http';
import { MenuPageModule } from '../pages/menu/menu.module';
import { DashboardPageModule } from '../pages/dashboard/dashboard.module';
import { AddReportPageModule } from '../pages/add-report/add-report.module';
import { IonicStorageModule } from '@ionic/storage';
import { ServiceReportPageModule } from '../pages/service-report/service-report.module';
import { StockReportPageModule } from '../pages/stock-report/stock-report.module';

@NgModule({
  declarations: [
    MyApp,
    LoginPage,
    LeadDetailsPage,
    AddBranchPage,
    AddLeadPage,
    HomePage
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MenuPageModule,
    DashboardPageModule,
    AddReportPageModule,
    StockReportPageModule,
    ServiceReportPageModule,
    IonicModule.forRoot(MyApp),
    IonicStorageModule.forRoot()
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    LoginPage,
    AddLeadPage,
    AddBranchPage,
    LeadDetailsPage,
    HomePage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    RestProvider,
    FileTransfer,
    // FileUploadOptions,
     //FileTransferObject,
     File,
     FileChooser,
     Camera,
    RestProvider
  

  ]
})
export class AppModule {}
