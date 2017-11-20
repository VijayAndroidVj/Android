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
$sid = $_POST['factory_id'];
$pcid = $_POST['pcid'];

$strSQL = "SELECT * FROM pickup_customer WHERE `factory_id`  = '" . $sid . "' AND `pcid`  = '" . $pcid . "' AND NOT `completion`  = '100' order by pcid desc";

$objQuery = mysql_query($strSQL);
$intNumField = mysql_num_fields($objQuery);
$resultArray = array();
$shop_id = "";
while ($obResult = mysql_fetch_array($objQuery)) {
    $arrCol = array();
    for ($i = 0; $i < $intNumField; $i++) {
	$columnname = mysql_field_name($objQuery, $i);
	$columnValue = $obResult[$i];
	if($columnname == "shop_id"){
		$shop_id = $columnValue;
		
	}
       	$arrCol[$columnname] = $columnValue;
    }
	
        try{
		$query_ = "SELECT shop_code_and_name,mobile FROM cinderella_admin_shop WHERE `sid`  = '" . $shop_id ."'";
	 	$result_ = mysql_query($query_); 
		$row_ = mysql_fetch_object($result_);
		$shop_code_and_name = $row_->shop_code_and_name; 
		$mobile = $row_->mobile; 
		$arrCol["shop_code_and_name"] = $shop_code_and_name;
		$arrCol["shop_contact"] = $mobile;

	} catch (Exception $ex) {
            $arrCol["error"] = $ex;
        }	
	
    array_push($resultArray, $arrCol);
}

//	mysql_close($objConnect);

echo json_encode($resultArray);
?>	
