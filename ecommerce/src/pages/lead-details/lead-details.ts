import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { RestProvider } from '../../providers/rest/rest';


/**
 * Generated class for the LeadDetailsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-lead-details',
  templateUrl: 'lead-details.html',
})
export class LeadDetailsPage {
 
  broker:any;
  constructor(public navCtrl: NavController, public navParams: NavParams,public restProvider: RestProvider) {
    
    this.broker = this.navParams.data;
    console.log(this.broker);    
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad LeadDetailsPage');
  }

}
