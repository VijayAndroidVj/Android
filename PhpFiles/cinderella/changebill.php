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
$pcid = $_POST['pcid'];
$total_amt = $_POST['total_amt'];
$amt_reduced = $_POST['amt_reduced'];



$result_stock = mysql_query("UPDATE pickup_customer SET overall_total = '$total_amt', amt_reduced = '$amt_reduced' WHERE `pcid`  = '" . $pcid . "'");




// check if row inserted or not
if ($result_stock) {
    // successfully updated
    $response["success"] = 1;
    $response["message"] = "Successfully Sent.";
    
    // echoing JSON response
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = die(mysql_error());

    // echoing JSON response
    echo json_encode($response);
}
