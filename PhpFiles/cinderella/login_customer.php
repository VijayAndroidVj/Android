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

$mobile = $_POST['mobile'];
$pwd = $_POST['pwd'];

$result = mysql_query("SELECT * FROM `signup_customer` WHERE `mobile`  = '" . $mobile . "' AND `pwd`  = '" . $pwd . "' ");

if (!empty($result)) {

    // check for empty result
    if (mysql_num_rows($result) > 0) {

        $response["customer_list"] = array();

        while ($row = mysql_fetch_array($result)) {
            // temp user array

            $product = array();
            $product["name"] = $row["name"];
            $product["cid"] = $row["cid"];
            $product["mobile"] = $row["mobile"];
            $product["email"] = $row["email"];
            $product["address"] = $row["address"];
            $product["pwd"] = $row["pwd"];


            // push single product into final response array
            array_push($response["customer_list"], $product);
        }
        // success
        $response["success"] = 1;
        $response["message"] = "Successfully Logged";
        $response["type"] = "customer";
        // echoing JSON response
        echo json_encode($response);
    }
    else {
    $response["success"] = 0;
    $response["message"] = "Please Enter Valid Credential";
    $response["type"] = "customer";
    // echoing JSON response
    echo json_encode($response);
}
} 
?>

