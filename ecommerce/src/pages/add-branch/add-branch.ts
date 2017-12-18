import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { RestProvider } from '../../providers/rest/rest';
import { ToastController } from 'ionic-angular';
import { BranchPage } from '../branch/branch';

/**
 * Generated class for the AddBranchPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-add-branch',
  templateUrl: 'add-branch.html',
})
export class AddBranchPage {
  leadData:any
  item = [];

  lead = { line1: '', line2: '',line3: '', line4: '', line5: '',eid: "1" };


  constructor(public navCtrl: NavController, public navParams: NavParams,public toastCtrl: ToastController,public restProvider: RestProvider) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad AddBranchPage');
  }

  addleadServer (){
    // this.item.push(this.lead)
     this.restProvider.addCustomer( this.lead).then((result) => {
       console.log(result);
       this.leadData=result["success"];
       if(this.leadData == 1){
         let toast = this.toastCtrl.create({
           message: result["message"],
           duration: 3000
         });
          toast.present();
 
 
       this.navCtrl.setRoot(BranchPage);
       }else{        
         let toast = this.toastCtrl.create({
           message: result["message"],
           duration: 3000
         });
          toast.present();
       }
     }, (err) => {
       console.log(err);
       let toast = this.toastCtrl.create({
         message:err,
         duration: 3000
       });
        toast.present();
     });
   }

}
