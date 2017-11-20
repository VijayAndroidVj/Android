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
$username = $_POST['username'];
$email = $_POST['email'];
$password = $_POST['password'];



$result_chk = mysql_query("SELECT * FROM `user` WHERE `email`  = '" . $email . "' ");


if (!empty($result_chk)) {

// check for empty result
    if (mysql_num_rows($result_chk) > 0) {

        $response["result"] = "failed";
        $response["message"] = "Account  Already  Exist.";
        

// echoing JSON response
        echo json_encode($response);
    } else {
        $result = mysql_query("INSERT INTO user(name, username, email, password) VALUES('$name', '$username', '$email', '$password')");

        if ($result) {
// successfully inserted into database
           $response["result"] = "success";
            $response["message"] = "Account Created Successfully.";
            //echo $output;
            // echoing JSON response
            echo json_encode($response);
        } else {
// failed to insert row
            $response["result"] = "failed";
            $response["message"] = die(mysql_error());

// echoing JSON response
            echo json_encode($response);
        }
    }
}

?>
