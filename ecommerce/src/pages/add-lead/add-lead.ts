import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { RestProvider } from '../../providers/rest/rest';
import { ToastController } from 'ionic-angular';
import { LeadManagementPage } from '../lead-management/lead-management';

/**
 * Generated class for the AddLeadPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-add-lead',
  templateUrl: 'add-lead.html',
})
export class AddLeadPage {
  leadData:any
  item = [];

  lead = { name: '', username: '',password: '', email: '', mobileno: '', designation: '', empid: "1",aid: "1" };


  constructor(public navCtrl: NavController, public navParams: NavParams  ,public toastCtrl: ToastController,public restProvider: RestProvider) {
   
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad AddLeadPage');
  }
  addleadServer (){
    // this.item.push(this.lead)
     this.restProvider.addLeads( this.lead).then((result) => {
       console.log(result);
       this.leadData=result["success"];
       if(this.leadData == 1){
         let toast = this.toastCtrl.create({
           message: result["message"],
           duration: 3000
         });
          toast.present();
 
 
       this.navCtrl.setRoot(LeadManagementPage);
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
