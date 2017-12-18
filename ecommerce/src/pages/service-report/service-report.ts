import { Component } from '@angular/core';
import { File } from '@ionic-native/file';
import { IonicPage, NavParams,NavController, LoadingController, ToastController } from 'ionic-angular';
import { FileTransfer, FileUploadOptions, FileTransferObject } from '@ionic-native/file-transfer';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { FileChooser } from '@ionic-native/file-chooser';
import { FilePath } from '@ionic-native/file-path';
import { Storage } from '@ionic/storage';

/**
 * Generated class for the ServiceReportPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-service-report',
  templateUrl: 'service-report.html',
})
export class ServiceReportPage {
  filepath :any;
  imageURI:any;
  fileURI:any;
  imageFileName:any;
  nativepath: string;
  custome = {eid:"",aid:""}
  file;

  constructor(public navCtrl: NavController,
    private transfer: FileTransfer,
    private camera: Camera,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,private storage: Storage,private fileChooser: FileChooser) {
      storage.get('empid').then((val) => {
        console.log('Your age is', val);
        this.custome.eid=val;
        this.custome.aid=val;
            });
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ServiceReportPage');
  }

  getFile(){
    this.fileChooser.open()
    .then(uri => {
      this.fileURI=uri;
      (<any>window).FilePath.resolveNativePath(uri, (result) => {
        this.nativepath = result;
        alert(this.nativepath);
         let path = this.nativepath.substring(this.nativepath.lastIndexOf(('/'))+1);
         //path.substring(path.lastIndexOf("/")+1);
       //  alert(path);
         this.imageURI =path ;
        })
    .catch(e => console.log(e));


      console.log(uri)})
    .catch(e => console.log(e));
  }

  getImage() {
    const options: CameraOptions = {
      quality: 100,
      destinationType: this.camera.DestinationType.FILE_URI,
      sourceType: this.camera.PictureSourceType.PHOTOLIBRARY
    }
  
    this.camera.getPicture(options).then((imageData) => {
      this.imageURI = imageData;
    }, (err) => {
      console.log(err);
      this.presentToast(err);
    });
  }

  uploadFile() {
    let loader = this.loadingCtrl.create({
      content: "Uploading..."
    });
    loader.present();
    const fileTransfer: FileTransferObject = this.transfer.create();
  
    let options: FileUploadOptions = {
      fileKey: 'file',
      fileName:  this.imageURI,
      chunkedMode: false,
     // mimeType: "image/jpeg",
      headers: {}
    }
  
    fileTransfer.upload(this.fileURI, 'https://itmspl.com/pioneer/upload.php?eid='+this.custome.aid+'&&aid='+this.custome.aid, options)
      .then((data) => {
      console.log(data+" Uploaded Successfully");
      let datafy =JSON.parse(data.response);
     // this.imageFileName = this.imageURI;
      loader.dismiss();
      this.presentToast(  this.imageURI+datafy["message"]);
    }, (err) => {
      console.log(err);
      loader.dismiss();
      this.presentToast(err);
    });
  }

  presentToast(msg) {
    let toast = this.toastCtrl.create({
      message: msg,
      duration: 3000,
      position: 'top'
    });
  
    toast.onDidDismiss(() => {
      console.log('Dismissed toast');
    });
  
    toast.present();
  }

}
