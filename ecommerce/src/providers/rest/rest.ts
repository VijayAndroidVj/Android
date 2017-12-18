import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

/*
  Generated class for the RestProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class RestProvider {
  // apiUrl = 'http://192.168.75.1/pioneer';
 apiUrl = 'https://itmspl.com/pioneer';// live

  constructor(public http: HttpClient) {
    console.log('Hello RestProvider Provider');
  }

  getLeads() {
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/get_leads.php').subscribe(data => {
        resolve(data["products"]);
      }, err => {
        console.log(err);
      });
    });
  }

  getChatLeads(id) {
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/get_chatlist.php?empId='+id).subscribe(data => {
        resolve(data["products"]);
      }, err => {
        console.log(err);
      });
    });
  }


  getChatCountLeads(id) {
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/get_chatlist.php?empId='+id).subscribe(data => {
        resolve(data);
      }, err => {
        console.log(err);
      });
    });
  }

  getCustomer() {
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/get_custromer.php').subscribe(data => {
        resolve(data["products"]);
      }, err => {
        console.log(err);
      });
    });
  }

  getCustomerId(ids) {
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/get_custromer.php?empId='+ids).subscribe(data => {
        resolve(data["products"]);
      }, err => {
        console.log(err);
      });
    });
  }
  getLeadId(ids) {
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/get_leads.php?empId='+ids).subscribe(data => {
        resolve(data["products"]);
      }, err => {
        console.log(err);
      });
    });
  }

  getVisitId(ids) {
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/get_visits.php?custId='+ids).subscribe(data => {
        resolve(data["products"]);
      }, err => {
        console.log(err);
      });
    });
  }

  updateStatus(ids,val) {
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/update_status.php?empId='+ids+'&&status='+val).subscribe(data => {
        resolve(data);
      }, err => {
        console.log(err);
      });
    });
  }

  getGraph(){
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/graph.php').subscribe(data => {
        resolve(data);
      }, err => {
        console.log(err);
      });
    });

  }

  getReport(stdate,eddate,eid,status) {
    return new Promise(resolve => {
      this.http.get(this.apiUrl+'/report.php?st_date='+stdate+'&&ed_date='+eddate+'&&empId='+eid+'&&status='+status).subscribe(data => {
        resolve(data["products"]);
      }, err => {
        console.log(err);
      });
    });
  }




  addLeads(data) {

    let body = JSON.stringify(data),
    headers = new Headers({'Content-Type': 'application/json'});
   // options = new RequestOptions({headers: headers});
return this.http.post(this.apiUrl+'/add_lead.php', body).toPromise();

/*
    return new Promise((resolve, reject) => {
      this.http.post(this.apiUrl+'/add_lead.php', JSON.stringify(data))
        .subscribe(res => {
          resolve(res);
        }, (err) => {
          reject(err);
        });
    });*/
  }
  addVisit(data) {
    
        let body = JSON.stringify(data),
        headers = new Headers({'Content-Type': 'application/json'});
       // options = new RequestOptions({headers: headers});
    return this.http.post(this.apiUrl+'/add_visit.php', body).toPromise();
    
    /*
        return new Promise((resolve, reject) => {
          this.http.post(this.apiUrl+'/add_lead.php', JSON.stringify(data))
            .subscribe(res => {
              resolve(res);
            }, (err) => {
              reject(err);
            });
        });*/
      }

    addCustomer(data) {

      let body = JSON.stringify(data),
      headers = new Headers({'Content-Type': 'application/json'});
     // options = new RequestOptions({headers: headers});
  return this.http.post(this.apiUrl+'/add_customer.php', body).toPromise();

  /*
      return new Promise((resolve, reject) => {
        this.http.post(this.apiUrl+'/users', JSON.stringify(data))
          .subscribe(res => {
            resolve(res);
          }, (err) => {
            reject(err);
          });
      });*/
  

}

}
