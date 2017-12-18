import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { LeadDetailsPage } from '../lead-details/lead-details';
import { AddLeadPage } from '../add-lead/add-lead';
import { RestProvider } from '../../providers/rest/rest';

/**
 * Generated class for the LeadManagementPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-lead-management',
  templateUrl: 'lead-management.html',
})
export class LeadManagementPage {
  postList: any;

  constructor(public navCtrl: NavController, public navParams: NavParams,public restProvider: RestProvider) {
    this.getLed();
  }

  getLed() {
    this.restProvider.getLeads()
    .then(data => {
     this.postList = data;
      console.log(this.postList);
    });
  } 

  ionViewDidLoad() {
    console.log('ionViewDidLoad LeadManagementPage');
  }
  openLEADpage(broker){
    this.navCtrl.push(LeadDetailsPage,broker);
}
addLead(){
  this.navCtrl.push(AddLeadPage)
}

}
