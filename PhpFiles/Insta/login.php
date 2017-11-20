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

$username = $_POST['username'];
$password = $_POST['password'];


    $result = mysql_query("SELECT * FROM `user` WHERE `username`  = '" . $username . "' AND `password`  = '" . $password . "' ");

    if (!empty($result)) {

        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $response["shop_list"] = array();

            while ($row = mysql_fetch_array($result)) {
                // temp user array

                $product = array();
                $product["username"] = $row["username"];
                $product["password"] = $row["password"];
                $product["name"] = $row["name"];
                $product["email"] = $row["email"];
                
                // push single product into final response array
                array_push($response["result"], $product);
            }
            // success
            $response["result"] = "success";
            $response["message"] = "Login Successfully";
            // echoing JSON response
            echo json_encode($response);
        } else {
            $response["result"] = "failed";
            $response["message"] = "Please Enter Valid Credential";
            
            // echoing JSON response
            echo json_encode($response);
        }
    }

?>

