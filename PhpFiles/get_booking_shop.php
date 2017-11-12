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
$locality = $_POST['locality_id'];
$city = $_POST['city_id'];


$strSQL = "SELECT * FROM pickup_customer WHERE `completion`  in ('' , '25') AND `city_id`  = '" . $city . "' AND `locality_id`  = '" . $locality . "' AND  `cancel`  = '' order by pcid desc";
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