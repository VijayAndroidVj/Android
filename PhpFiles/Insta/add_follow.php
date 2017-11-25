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




$useremail = $_POST['useremail'];
$emailtofollow = $_POST['emailtofollow'];
$follow = $_POST['follow'];

if($follow == "true"){

$result_chk = mysql_query("SELECT * FROM `insta_follower` WHERE `who`  = '" . $useremail . "' AND `whom` = '" . $emailtofollow . "'");


    if (!empty($result_chk)) {
    
    // check for empty result
        if (mysql_num_rows($result_chk) > 0) {
    
            $response["result"] = "failed";
            $response["message"] = "Already  Followed.";
            
    
    // echoing JSON response
            echo json_encode($response);
        } else {
            $result = mysql_query("INSERT INTO insta_follower(who, whom) VALUES('$useremail', '$emailtofollow')");
    
            if ($result) {
    // successfully inserted into database
               $response["result"] = "success";
                $response["message"] = "Followed Successfully.";
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
}else{
   
    $result1 = mysql_query("DELETE FROM `insta_follower` WHERE `who` = '" .$useremail. "' AND `whom` = '".$emailtofollow. "'");	
            if ($result1) {
                // successfully inserted into database
               $response["result"] = "success";
                $response["message"] = "Unfollowed Successfully.";
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

?>
