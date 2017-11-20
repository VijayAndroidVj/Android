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
$cid = $_POST['customer_id'];
$did = $_POST['delivery_id'];
$fid = $_POST['factory_id'];
$sid = $_POST['shop_id'];


$strSQL = "SELECT * FROM pickup_customer WHERE `delivery_id`  = '" . $did . "' AND  `completion`  = '100'  order by pcid desc";
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