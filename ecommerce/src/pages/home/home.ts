import { Component } from '@angular/core';
import { NavController, LoadingController, ToastController } from 'ionic-angular';
import { FileTransfer, FileUploadOptions, FileTransferObject } from '@ionic-native/file-transfer';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { FileChooser } from '@ionic-native/file-chooser';
import { FilePath } from '@ionic-native/file-path';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  imageURI:any;
  imageFileName:any;
  fileURI:any;
  nativepath:any;

  constructor(public navCtrl: NavController,
    private transfer: FileTransfer,
    private camera: Camera,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,private fileChooser: FileChooser) {

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
  
    fileTransfer.upload(this.fileURI, 'https://itmspl.com/pioneer/upload.php', options)
      .then((data) => {
      console.log(data+" Uploaded Successfully");
      this.imageFileName = this.imageURI;
      loader.dismiss();
      this.presentToast("Image uploaded successfully");
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
      position: 'bottom'
    });
  
    toast.onDidDismiss(() => {
      console.log('Dismissed toast');
    });
  
    toast.present();
  }

}
