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
$useremail = $_POST['useremail'];
$followed = $_POST['followed'];


	$strSQL = "SELECT * FROM insta_posts WHERE `user_mail`  = '" . $useremail . "'";
	$objQuery = mysql_query($strSQL);
	$intNumField = mysql_num_fields($objQuery);
	$resultArray = array();
	while ($obResult = mysql_fetch_array($objQuery)) {
	     $arrCol = array();

	     $arrCol["post_id"] =  $obResult['post_id'];
	     $arrCol["user_mail"] =   $obResult['user_mail'];
	     $arrCol["image"] =  $obResult['image'];
	     $arrCol["description"] =  $obResult['description'];
	     $query1="select * from insta_post_like where `post_id`  = '" . $obResult['post_id'] . "'";
	     $result1 = mysql_query($query1); 
	     $num_rows = mysql_num_rows($result1);
	     $arrCol["total_likes"] =  $num_rows;

	    array_push($resultArray, $arrCol);
	}

	//	mysql_close($objConnect);
	
	$myObj->data = $resultArray;
	$myObj->totalposts = sizeof($resultArray);
	
	
	 $query2="select * from insta_follower where `who`  = '" . $useremail . "'";
	     $result2 = mysql_query($query2); 
	     $num_rows2 = mysql_num_rows($result2);
	     $myObj->total_followering = $num_rows2;
	     
	      $query3="select * from insta_follower where `whom`  = '" . $useremail . "'";
	     $result3 = mysql_query($query3); 
	     $num_rows3 = mysql_num_rows($result3);
	     $myObj->total_followers = $num_rows3;
	     

	echo json_encode($myObj);

?>

