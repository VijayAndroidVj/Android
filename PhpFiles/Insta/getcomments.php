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
$post_id = $_POST['post_id'];

	$strSQL = "SELECT * FROM insta_post_comments WHERE `post_id`  = '" . $post_id . "'";
	$objQuery = mysql_query($strSQL);
	$intNumField = mysql_num_fields($objQuery);
	$resultArray = array();
	while ($obResult = mysql_fetch_array($objQuery)) {
	     $arrCol = array();
	     $arrCol["comment_id"] =  $obResult['comment_id'];
	     $arrCol["post_id"] =  $obResult['post_id'];
	     $arrCol["user_email"] =   $obResult['user_email'];
	     $arrCol["username"] =  $obResult['username'];
	     $arrCol["comment"] =  $obResult['comment'];

	     array_push($resultArray, $arrCol);
	}
	echo json_encode($resultArray);

?>

