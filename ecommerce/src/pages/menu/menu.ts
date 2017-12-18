import { Component,ViewChild } from '@angular/core';
import { IonicPage, NavController, NavParams, Nav , ViewController,App} from 'ionic-angular';
import { HomePage } from '../home/home';
import { LoginPage } from '../login/login';
import { AddReportPage } from '../add-report/add-report';
import { LeadDetailsPage } from '../lead-details/lead-details';
import { DashboardPage } from '../dashboard/dashboard';
import { LeadManagementPage } from '../lead-management/lead-management';
import { MenuController } from 'ionic-angular';
import { BranchPage } from '../branch/branch';
import { ServiceReportPage } from '../service-report/service-report';
import { StockReportPage } from '../stock-report/stock-report';

/**
 * Generated class for the MenuPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
 
export interface PageInterface {
  title: string;
  pageName: string;
  tabComponent?: any;
  index?: number;
  icon: string;
}
@IonicPage()
@Component({
  selector: 'page-menu',
  templateUrl: 'menu.html',
})
export class MenuPage {
  rootPage = DashboardPage;
  @ViewChild(Nav) nav: Nav;
  
   pages: PageInterface[] = [
     { title: 'DASHBOARD', pageName: 'DashboardPage', tabComponent: 'DashboardPage', index: 0, icon: 'home' },
     { title: 'ENQUIY REPORT', pageName: 'AddReportPage', tabComponent: 'AddReportPage', index: 1, icon: 'md-people' },
     { title: 'SERVICE REPORT', pageName: 'ServiceReportPage', tabComponent: 'ServiceReportPage', index: 1, icon: 'md-people' },
     { title: 'STOCk REPORT', pageName: 'StockReportPage', tabComponent: 'StockReportPage', index: 1, icon: 'md-people' },
     { title: 'USER MANAGEMENT', pageName: 'LeadManagementPage',tabComponent:'LeadManagementPage', icon: 'contacts' },
     { title: 'BRANCH MANAGEMENT', pageName: 'BranchPage',tabComponent:'BranchPage',index: 1, icon: 'settings' },
     { title: 'LOGOUT', pageName: 'test',tabComponent:'test', icon: 'md-log-out' }
   ];

  constructor(public navCtrl: NavController, public navParams: NavParams,public appCtrl: App, public viewCtrl: ViewController,public menuCtrl: MenuController) {
    this.menuCtrl.enable(true, 'authenticated');
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad MenuPage');
  }

  openPage(page: PageInterface) {
    let params = {};
   

    if(page.pageName == "test"){
     // this.navCtrl.setRoot(LoginPage);
    // this.navCtrl.push(LoginPage).then(()=>{this.navCtrl.remove(-1)});
   // this.appCtrl.getRootNav().setRoot(DashboardPage);
    this.appCtrl.getRootNav().setRoot(LoginPage);
    this.navCtrl.remove(1);
    }else if(page.pageName == "testOne"){
      this.nav.setRoot(HomePage);
    }else{
      this.nav.setRoot(page.pageName);
      if (page.index) {
        params = { tabIndex: page.index };
      }
   
      // The active child nav is our Tabs Navigation
      if (this.nav.getActiveChildNav() && page.index != undefined) {
        this.nav.getActiveChildNav().select(page.index);
      } else {
        // Tabs are not active, so reset the root page 
        // In this case: moving to or from SpecialPage
        this.viewCtrl.dismiss();

        this.appCtrl.getRootNav().setRoot(page.pageName, params);
        
      }
    }
   // this.navCtrl.setRoot(page.pageName);
   
 
    // The index is equal to the order of our tabs inside tabs.ts
    

    
    


  }
/*
  openPage(page) {
    // Reset the content nav to have just this page
    // we wouldn't want the back button to show in this scenario
    this.nav.setRoot(page.component);
  }*/

 
isActive(page: PageInterface) {
    // Again the Tabs Navigation
    if(page.pageName == "test"){
     // this.navCtrl.setRoot(LoginPage);
    }else if(page.pageName == "testOne"){
      //this.navCtrl.setRoot(HomePage);
    }else{
      let childNav = this.nav.getActiveChildNav();
      
         if (childNav) {
           if (childNav.getSelected() && childNav.getSelected().root === page.tabComponent) {
             return 'primary';
           }
           return;
         }
      
         // Fallback needed when there is no active childnav (tabs not active)
         if (this.nav.getActive() && this.nav.getActive().name === page.pageName) {
           return 'primary';
         }
         return;
    }
 
  }
}



 

