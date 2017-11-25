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
$post_id = $_POST['post_id'];
$like = $_POST['like'];
$username = $_POST['username'];

if($like == "true"){

$result_chk = mysql_query("SELECT * FROM `insta_post_like` WHERE `user_email`  = '" . $user_email . "' AND `post_id` = '" . $post_id . "'");


    if (!empty($result_chk)) {
    
    // check for empty result
        if (mysql_num_rows($result_chk) > 0) {
    
            $response["result"] = "failed";
            $response["message"] = "Already  Liked.";
            
    
    // echoing JSON response
            echo json_encode($response);
        } else {
            $result = mysql_query("INSERT INTO insta_post_like(post_id, user_email, username) VALUES('$post_id', '$useremail, '$username')");
    
            if ($result) {
    // successfully inserted into database
               $response["result"] = "success";
                $response["message"] = "Liked Successfully.";
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
   
    $result1 = mysql_query("DELETE FROM `insta_post_like` WHERE `post_id` = '" .$post_id. "' AND `useremail` = '".$useremail. "'");	
            if ($result1) {
                // successfully inserted into database
               $response["result"] = "success";
                $response["message"] = "Unliked Successfully.";
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
