import { Component ,ViewChild} from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Chart } from 'chart.js';
import { RestProvider } from '../../providers/rest/rest';



/**
 * Generated class for the DashboardPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-dashboard',
  templateUrl: 'dashboard.html',
})
export class DashboardPage {
  doughnutChart: any;
  postList = [{title:"test1"},{title:"test2"},{title:"test3"},{title:"test4"},{title:"test5"}];
  users: any;
  confirm :any;
  possitive :any;
  negative : any;
  installed : any;
  notify:any;
  @ViewChild('doughnutCanvas') doughnutCanvas;
  
     barChart: any;

  constructor(public navCtrl: NavController, public navParams: NavParams,public restProvider: RestProvider) {

  }

 
 

  ionViewDidLoad() {
    console.log('ionViewDidLoad DashboardPage');
    //Chart.defaults.global.tooltips.enabled = false;
    Chart.defaults.global.legend.position = 'bottom';
    this.doughnutChart = new Chart(this.doughnutCanvas.nativeElement, {
      
                 type: 'doughnut',
                 options: {
                    responsive: true,
                    maintainAspectRatio: false,        
                  },
                 data: {
                    labels: ["negative", "possitive", "installed", "Confirmed"],
                     datasets: [{
                         label: '# of Votes',
                         data: [1,2,3,4],
                         backgroundColor: [
                             'rgba(255, 99, 132, 0.2)',
                             'rgba(54, 162, 235, 0.2)',
                             'rgba(255, 206, 86, 0.2)',
                             'rgba(75, 192, 192, 0.2)',
                            /* 'rgba(153, 102, 255, 0.2)',
                             'rgba(255, 159, 64, 0.2)' */
                         ],
                         hoverBackgroundColor: [
                             "#FF6384",
                             "#36A2EB",
                             "#FFCE56",
                             "#FF6384",
                            /*  "#36A2EB",
                             "#FFCE56" */
                         ]
                     }]
                 }
      
             }); 

}
}
