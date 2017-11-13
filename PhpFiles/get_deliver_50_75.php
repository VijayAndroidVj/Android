<?php

/*
 * Following code will list all the products
 */

// array for JSON response
$response = array();

// include db connect class
$filepath = realpath(dirname(__FILE__));
require_once($filepath . "/db_connect.php");

// connecting to db
$db = new DB_CONNECT();
$delivery_id = $_POST['delivery_id'];
$completion1 = $_POST['completion1'];
$completion2 = $_POST['completion2'];


$strSQL = "SELECT * FROM pickup_customer WHERE `completion`  = '" . $completion2 . "' OR `completion`  = '" . $completion1 . "' AND `delivery_id`  = '" . $delivery_id . "'  order by pcid desc";
$objQuery = mysql_query($strSQL);
$intNumField = mysql_num_fields($objQuery);
$resultArray = array();
while ($obResult = mysql_fetch_array($objQuery)) {
    $arrCol = array();
    for ($i = 0; $i < $intNumField; $i++) {
        $arrCol[mysql_field_name($objQuery, $i)] = $obResult[$i];
    }
    array_push($resultArray, $arrCol);
}

//	mysql_close($objConnect);

echo json_encode($resultArray);
?>