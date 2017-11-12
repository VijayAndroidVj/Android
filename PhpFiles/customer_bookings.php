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

$smsoutput = "";
$city = $_POST['city'];
$locality = $_POST['locality'];
$cid = $_POST['cid'];
$mobile = $_POST['mobile'];
$pickup_date = $_POST['pickup_date'];
$pickup_time = $_POST['pickup_time'];
$customer_name = isset($_POST['customer_name']) ? $_POST['customer_name'] : '';
$mobile_date = $_POST['mobile_date'];
$city_id = $_POST['city_id'];
$locality_id = $_POST['locality_id'];

$address = $_POST['address'];
$unique_code = $_POST['unique_code'];
$customer_token = $_POST['customer_token'];





$result_chk = mysql_query("SELECT * FROM `pickup_customer` WHERE `customer_id`  = '" . $cid . "' AND `pickup_date`  = '" . $pickup_date . "' AND `pickup_time` ='" . $pickup_time . "' ");


if (!empty($result_chk)) {

// check for empty result
    if (mysql_num_rows($result_chk) > 0) {

        $response["success"] = 3;
        $response["type"] = "booking";

        $response["message"] = "Schedule  Already  Exist For\n$pickup_date\n$pickup_time";

// echoing JSON response
        echo json_encode($response);
    } else {
        $result = mysql_query("INSERT INTO pickup_customer(customer_id, customer_name, city, locality, mobile, pickup_date, pickup_time, address, mobile_date, unique_code, city_id, locality_id, customer_token) VALUES('$cid', '$customer_name'  , '$city', '$locality', '" . $mobile . "', '" . $pickup_date . "', '" . $pickup_time . "','" . $address . "','" . $mobile_date . "','" . $unique_code . "', '" . $city_id . "', '" . $locality_id . "', '" . $customer_token . "')");



        if ($result) {
// successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "SuccessFully Schedule Sent.\nCinderella Delivery Person Arrive Your Place Soon.";
            
            $response["type"] = "booking";

            //Your message to send, Add URL encoding here.
            $message = "Thanks for choosing CINDERELLA. Your cloths will be picked up soon. Customercare:7997565758";
            
            $response["sms_message"] = $mobile."  ".$message;
               
            //Your message to send, Add URL encoding here.
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
            $smsoutput = $output;
            //Print error if any
            if(curl_errno($ch))
            {
                $smsoutput = 'error:' . curl_error($ch);
            }
            curl_close($ch);
            $response["sms"] =  $smsoutput;
            echo json_encode($response);

        } else {
// failed to insert row
            $response["success"] = 0;
            $response["type"] = "booking";

            $response["message"] = die(mysql_error());

// echoing JSON response
            echo json_encode($response);
        }
    }
}
?>
