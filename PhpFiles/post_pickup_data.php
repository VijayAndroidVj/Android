<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Path to move uploaded files
$target_path = "PickupData/";

$filepath = realpath(dirname(__FILE__));
require_once($filepath . "/db_connect.php");

// connecting to db
$db = new DB_CONNECT();
$response = array();

// getting server ip address
$hostname = "http://vajralabs.com/Cinderella";
$server_ip = gethostbyname($hostname);

// final file url that is being uploaded
$file_upload_url = $server_ip . '/' . $target_path;


if (isset($_FILES['image']['name'])) {
    $target_path = $target_path . basename($_FILES['image']['name']);
//    $target_path = $target_path . basename($_FILES['image1']['name']);
//    $target_path = $target_path . basename($_FILES['image2']['name']);


    // reading other post parameters
    $items = isset($_POST['items']) ? $_POST['items'] : '';
    $rate = isset($_POST['rate']) ? $_POST['rate'] : '';
    $count = isset($_POST['count']) ? $_POST['count'] : '';
    $total = isset($_POST['total']) ? $_POST['total'] : '';
    $instruction = isset($_POST['instruction']) ? $_POST['instruction'] : '';
    $overall_total = isset($_POST['overall_total']) ? $_POST['overall_total'] : '';
    $overall_count = isset($_POST['overall_count']) ? $_POST['overall_count'] : '';
    $discount = isset($_POST['discount']) ? $_POST['discount'] : '0';
    $initialTotalSum = isset($_POST['initialTotalSum']) ? $_POST['initialTotalSum'] : '0';
    $payment_mode = isset($_POST['payment_mode']) ? $_POST['payment_mode'] : '';
    $given_amt = isset($_POST['given_amt']) ? $_POST['given_amt'] : '';
    $balance_amt = isset($_POST['balance_amt']) ? $_POST['balance_amt'] : '';
    $unique_code = isset($_POST['unique_code']) ? $_POST['unique_code'] : '';
    $bill = isset($_POST['bill']) ? $_POST['bill'] : '';
    $shop_id = isset($_POST['shop_id']) ? $_POST['shop_id'] : '';
    $delivery_id = isset($_POST['delivery_id']) ? $_POST['delivery_id'] : '';
    $completion = isset($_POST['completion']) ? $_POST['completion'] : '';
    $mobile_date_pickuped = isset($_POST['mobile_date_pickuped']) ? $_POST['mobile_date_pickuped'] : '';




//    $response['file_name'] = basename($_FILES['image']['name']);
//    $response['title'] = $email;
//    $response['content'] = $content;
//    $response['posted_by'] = $posted_by;

    try {
        // Throws exception incase file is not being moved
        if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {
            // make error flag true
            $response['error'] = true;
            $response['message'] = 'Could not move the file!';
        }

        // File successfully uploaded
//        $response['message'] = 'File uploaded successfully!';
//        $response['error'] = false;
//        $response['file_path'] = $file_upload_url . basename($_FILES['image']['name']);

        $image_path = $file_upload_url . basename($_FILES['image']['name']);
//        $image_path1 = $file_upload_url . basename($_FILES['image1']['name']);
//        $image_path2 = $file_upload_url . basename($_FILES['image2']['name']);

        // mysql inserting a new row

        $query="select mobile, customer_id from pickup_customer where `delivery_id`  = '" . $delivery_id . "' AND `unique_code`  = '" . $unique_code . "'";
        $result = mysql_query($query) or die(); 
        $row = mysql_fetch_object($result);
        $mobile = $row->mobile;
	$customer_id = $row->customer_id; 


        $result_stock = mysql_query("UPDATE pickup_customer SET items = '" . $items . "',rate = '" . $rate . "',count = '" . $count . "',total = '" . $total . "',initialTotalSum = '".$initialTotalSum."', discount ='".$discount."',instruction = '" . $instruction . "',"
                . "overall_total = '" . $overall_total . "',overall_count = '" . $overall_count . "',payment_mode = '" . $payment_mode . "'"
                . ",given_amt = '" . $given_amt . "'"
                . ",balance_amt = '" . $balance_amt . "',unique_code = '" . $unique_code . "',image_two = '" . $image_path . "',image_three = '" . $image_path . "'"
                . ",bill = '" . $bill . "',shop_id = '" . $shop_id . "'"
                . ",delivery_id = '" . $delivery_id . "',completion = '50'"
                . ",mobile_date_pickuped = '" . $mobile_date_pickuped . "',image_one = '" . $image_path . "'"
                . " WHERE `delivery_id`  = '" . $delivery_id . "' AND `unique_code`  = '" . $unique_code . "'");




        if ($result_stock) {
// successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "SuccessFully Posted.";


            //Multiple mobiles numbers separated by comma
            $mobileNumber = $mobile;

            //Your message to send, Add URL encoding here.
            $message = "Your Booking is scheduled, Customer Id: ".$customer_id." Cloths: ".$overall_count." Amt: ".$overall_total." ".$payment_mode."";
            $response["smsMessage"] = $message;
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
// echoing JSON response
            echo json_encode($response);
        } else {
// failed to insert row
            $response["success"] = 0;
            $response["message"] = die(mysql_error());

// echoing JSON response
            echo json_encode($response);
        }




        // check if row inserted or not
    } catch (Exception $e) {
        // Exception occurred. Make error flag true
        $response['error'] = true;
        $response['message'] = $e->getMessage();
    }
} else {
    // File parameter is missing
    $response['error'] = true;
    $response['message'] = 'Not received any file!F';


    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}


// Echo final json response to client
echo json_encode($response);
?>
