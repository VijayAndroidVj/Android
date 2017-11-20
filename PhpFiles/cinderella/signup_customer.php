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



$name = $_POST['name'];
$mobile = $_POST['mobile'];
$email = $_POST['email'];
$pwd = $_POST['pwd'];
$address = $_POST['address'];



$result_chk = mysql_query("SELECT * FROM `signup_customer` WHERE `email`  = '" . $email . "' OR `mobile`  = '" . $mobile . "' ");


if (!empty($result_chk)) {

// check for empty result
    if (mysql_num_rows($result_chk) > 0) {

        $response["success"] = 2;
        $response["message"] = "Customer  Already  Exist.";
        

// echoing JSON response
        echo json_encode($response);
    } else {
        $result = mysql_query("INSERT INTO signup_customer(name, mobile, email, address, pwd) VALUES('$name', '$mobile', '$email', '$address', '$pwd')");



        if ($result) {
// successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Account Created Successfully.";
            
            $message = "Congrats! Welcome to CINDERELLA. Your User ID :".$mobile." Password : ".$pwd.".Enjoy Online Laundry. Helpline:7997595758";
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
                        
            //echo $output;
            // echoing JSON response
            echo json_encode($response);
        } else {
// failed to insert row
            $response["success"] = 0;
            $response["message"] = die(mysql_error());

// echoing JSON response
            echo json_encode($response);
        }
    }
}

?>
