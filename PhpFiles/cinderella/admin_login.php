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
$pwd = $_POST['password'];
$type = $_POST['type'];

if ($type == "shop") {

    $result = mysql_query("SELECT * FROM `cinderella_admin_shop` WHERE `mobile`  = '" . $mobile . "' AND `password`  = '" . $pwd . "' ");

    if (!empty($result)) {

        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $response["shop_list"] = array();

            while ($row = mysql_fetch_array($result)) {
                // temp user array

                $product = array();
                $product["shop_name"] = $row["shop_name"];
                $product["sid"] = $row["sid"];
                $product["shop_keeper"] = $row["shop_keeper"];
                $product["email"] = $row["email"];
                $product["mobile"] = $row["mobile"];


                $product["address"] = $row["address"];
                $product["password"] = $row["password"];

                $product["city"] = $row["city"];
                $product["locality"] = $row["locality"];
                $product["shop_code"] = $row["shop_code"];
                $product["cid"] = $row["cid"];
                $product["aid"] = $row["aid"];

                $product["shop_code_and_name"] = $row["shop_code_and_name"];


                // push single product into final response array
                array_push($response["shop_list"], $product);
            }
            // success
            $response["success"] = 1;
            $response["message"] = "Shop Successfully Logged";
            $response["type"] = "shop";
            // echoing JSON response
            echo json_encode($response);
        } else {
            $response["success"] = 0;
            $response["message"] = "Please Enter Valid Shop Credential";
            $response["type"] = "shop";
            // echoing JSON response
            echo json_encode($response);
        }
    }
} else if ($type == "delivery") {

    $result = mysql_query("SELECT * FROM `cinderella_admin_deliver` WHERE `mobile`  = '" . $mobile . "' AND `password`  = '" . $pwd . "' ");

    if (!empty($result)) {

        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $response["delivery_list"] = array();

            while ($row = mysql_fetch_array($result)) {
                // temp user array

                $product = array();
                $product["delivery_person"] = $row["delivery_person"];
                $product["did"] = $row["did"];
                $product["sid"] = $row["sid"];

                $product["email"] = $row["email"];
                $product["address"] = $row["address"];
                $product["mobile"] = $row["mobile"];
				$product["shop_code"] = $row["shop_code"];
				$product["shop_code_and_name"] = $row["shop_code_and_name"];

                $product["address"] = $row["address"];
                $product["password"] = $row["password"];

                // push single product into final response array
                array_push($response["delivery_list"], $product);
            }
            // success
            $response["success"] = 1;
            $response["message"] = "Delivery Successfully Logged";
            $response["type"] = "delivery";
            // echoing JSON response
            echo json_encode($response);
        } else {
            $response["success"] = 0;
            $response["message"] = "Please Enter Valid Delivery Credential";
            $response["type"] = "delivery";
            // echoing JSON response
            echo json_encode($response);
        }
    }
} else if ($type == "factory") {

    $result = mysql_query("SELECT * FROM `cinderella_admin_factory` WHERE `mobile`  = '" . $mobile . "' AND `password`  = '" . $pwd . "' ");

    if (!empty($result)) {

        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $response["factory_list"] = array();

            while ($row = mysql_fetch_array($result)) {
                // temp user array

                $product = array();
                $product["factory_name"] = $row["factory_name"];
                $product["fid"] = $row["fid"];
                $product["contact_person"] = $row["contact_person"];

                $product["email"] = $row["email"];
                $product["address"] = $row["address"];
                $product["mobile"] = $row["mobile"];


                $product["password"] = $row["password"];



                // push single product into final response array
                array_push($response["factory_list"], $product);
            }
            // success
            $response["success"] = 1;
            $response["message"] = "Factory Successfully Logged";
            $response["type"] = "factory";
            // echoing JSON response
            echo json_encode($response);
        } else {
            $response["success"] = 0;
            $response["message"] = "Please Enter Valid Factory Credential";
            $response["type"] = "factory";
            // echoing JSON response
            echo json_encode($response);
        }
    }
}
?>

