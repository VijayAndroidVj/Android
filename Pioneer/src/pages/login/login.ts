
import { Component,ViewChild } from '@angular/core';
import { IonicPage, NavController, NavParams, Nav } from 'ionic-angular';
import { RestProvider } from '../../providers/rest/rest';
import { HttpClient } from '@angular/common/http';
import { HomePage } from '../home/home';
import { ToastController } from 'ionic-angular';
import {App} from "ionic-angular"
import 'rxjs/add/operator/map';

/**
 * Generated class for the LoginPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
  providers:[RestProvider],
})
export class LoginPage {

  /*constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad LoginPage');
  }*/


  @ViewChild('myNav') nav: NavController
  
   
   
    username="";
    password="";
   
    items=[]; 
    newData: any;
  
  
  
    constructor(public navCtrl: NavController, protected app: App,public toastCtrl: ToastController,public navParams: NavParams,public restProvider: RestProvider,public http: HttpClient) {
    
      
      this.http = http;
    }
  
    submit() {
      this.app.getRootNav().setRoot(HomePage)
      var link = 'http://192.168.75.1/CRMAPP/add_user.php?username='+this.username+'&&password='+this.password;
     // var data = JSON.stringify({username: this.data.username});
      
      this.http.get(link)
      .subscribe(data => {
        this.newData=data["success"];
        if(this.newData == 1){
          let toast = this.toastCtrl.create({
            message: data["message"],
            duration: 3000
          });
           toast.present();
           //this.app.getRootNav().setRoot(HomePage)
          //this.navCtrl.push(HomePage);
          
        }else{
          let toast = this.toastCtrl.create({
            message: data["message"],
            duration: 3000
          });
          toast.present();
        }
         // this.data.response = data._body;
        // console.log(data);
        
  
      }, error => {
          console.log("Oooops!");
      });
  }
  nextPage()
  {
  
  }
  
    

}
