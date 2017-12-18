import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { RestProvider } from '../../providers/rest/rest';
import { AddBranchPage } from '../add-branch/add-branch';

/**
 * Generated class for the BranchPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-branch',
  templateUrl: 'branch.html',
})
export class BranchPage {
  postList: any;

  constructor(public navCtrl: NavController, public navParams: NavParams,public restProvider: RestProvider) {
    this.getbranch();
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad BranchPage');
  }

  getbranch() {
    this.restProvider.getCustomer()
    .then(data => {
     this.postList = data;
      console.log(this.postList);
    });
  } 
  addLead(){
    this.navCtrl.push(AddBranchPage)
  }



}
