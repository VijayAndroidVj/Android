<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$filepath = realpath(dirname(__FILE__));
require_once($filepath . "/db_connect.php");

// connecting to db
$db = new DB_CONNECT();

$response = array();
//if(isset($_POST['stock_by_member'])){
$pcid = $_POST['pcid'];
$completion = $_POST['completion'];
$delivery_assign_id = $_POST['delivery_assign_id'];
$delivery_contact_name = $_POST['delivery_contact_name'];
$delivery_contact = $_POST['delivery_contact'];

$smsSend = $_POST['sms'];
$FactoryJobDone = $_POST['FactoryJobDone'];



$result_stock = mysql_query("UPDATE pickup_customer SET completion = '$completion', delivery_assign_id = '$delivery_assign_id',delivery_assign_person = '$delivery_contact_name'"
        . ",delivery_assign_contact = '$delivery_contact' WHERE `pcid`  = '" . $pcid . "'");




// check if row inserted or not
if ($result_stock) {
    // successfully updated
    $response["success"] = 1;
    $response["message"] = "Successfully Sent.";
    
    try{
    if($smsSend == "true"){
        
        
        $query="select mobile, overall_total, overall_count, payment_mode, customer_id, shop_code_and_name from pickup_customer where `pcid`  = '" . $pcid . "'";
        $result = mysql_query($query) or die(); 
        $row = mysql_fetch_object($result);
        $mobile = $row->mobile;
        $overall_total = $row->overall_total;
        $overall_count = $row->overall_count;
        $payment_mode = $row->payment_mode;
	$customer_id = $row->customer_id; 
	$shop_code_and_name = $row->shop_code_and_name;
        
            $message = "Your Booking is scheduled for Delivery . Customer Id: ".$customer_id." Shop Code: ".$shop_code_and_name." Cloths: ".$overall_count." Amt: ".$overall_total." ".$payment_mode.".";
            $response["sms_message"] = $message;
            $smessage = urlencode($message);

            $response_type = 'json';

            //Define route 
            $route = "4";

            //Prepare you post parameters
            $postData = array(
                'authkey' => MSG91_AUTH_KEY1,
                'mobiles' => $mobile,
                'message' => $smessage,
                'sender' => MSG91_SENDER_ID1,
                'route' => $route,
                'response' => $response_type
            );

        //API URL
            $url = "https://control.msg91.com/sendhttp.php";

        // init the resource
            $ch = curl_init();
            curl_setopt_array($ch, array(
                CURLOPT_URL => $url,
                CURLOPT_RETURNTRANSFER => true,
                CURLOPT_POST => true,
                CURLOPT_POSTFIELDS => $postData
                    //,CURLOPT_FOLLOWLOCATION => true
            ));


            //Ignore SSL certificate verification
            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);

            //get response
            $output = curl_exec($ch);
            $response["sms_output"] = $output;
            //Print error if any
            if(curl_errno($ch))
            {
                $response["sms"] = 'error:' . curl_error($ch);
            }

            curl_close($ch);
    }
    if($FactoryJobDone == "true"){
       try{
        
        $query_="select shop_id,customer_name, factory_name from pickup_customer where `pcid`  = '" . $pcid . "'";
        $result_ = mysql_query($query_) or die(); 
        $row_ = mysql_fetch_object($result_);
        $shopid = $row_->shop_id; 
         $customer_name= $row1->customer_name; 
         $factory_name = $row1->factory_name;

        
        $query="select fcmid from cinderella_admin_factory where `sid`  = '" . $shopid . "'";
        $result = mysql_query($query) or die(); 
        $row = mysql_fetch_object($result);
        $shop_token = $row->fcmid; 

       
        $url = 'https://fcm.googleapis.com/fcm/send';
        $data = array('title'=>'Cinderella','message'=>'Factory '.$factory_name.' Finished Proocess. Customer Name : '.$customer_name);
        $fields = array();
        $fields['data'] = $data;
        $fields['title'] = "Cinderella";
        $fields['to'] = $shop_token;
        $fields['notification'] = array (
                        "body" => 'Cinderella','message'=>'Factory '.$factory_name.' Finished Proocess. Customer Name : '.$customer_name,
                        "title" => "Cinderella",
                );

        $fields = json_encode ( $fields );
        $headers = array (
                'Authorization: key=' . FIREBASE_API_KEY,
                'Content-Type: application/json'
        );

        $ch = curl_init ();
        curl_setopt ( $ch, CURLOPT_URL, $url );
        curl_setopt ( $ch, CURLOPT_POST, true );
        curl_setopt ( $ch, CURLOPT_HTTPHEADER, $headers );
        curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, true );
        curl_setopt ( $ch, CURLOPT_POSTFIELDS, $fields );

        $result = curl_exec ( $ch );
        $response["notification"] = $result;
        curl_close ( $ch );
        } catch (Exception $ex) {
            $response["notification"] = "Notification Failed";
        }
        }
  }catch(Exception $e) {
    $response["sms_error"] = 'Message: ' .$e->getMessage();
  }
  
  

    // echoing JSON response
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = die(mysql_error());

    // echoing JSON response
    echo json_encode($response);
}
