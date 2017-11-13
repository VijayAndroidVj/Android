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


$city = $_POST['city'];





$result_chk = mysql_query("SELECT * FROM `cinderella_city` WHERE `city`  = '" . $city . "' ");


if (!empty($result_chk)) {

// check for empty result
    if (mysql_num_rows($result_chk) > 0) {

        $response["success"] = 3;
        $response["message"] = "City Already  Exist.";

// echoing JSON response
        echo json_encode($response);
    } else {
        $result = mysql_query("INSERT INTO cinderella_city(city) VALUES('$city')");



        if ($result) {
// successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "City SuccessFully Created.";
            $response["confirmation"] = $confirmation;


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
