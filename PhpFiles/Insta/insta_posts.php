<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Path to move uploaded files
$target_path = "posts/";

$filepath = realpath(dirname(__FILE__));
require_once($filepath . "/db_connect.php");

// connecting to db
$db = new DB_CONNECT();

// getting server ip address
$hostname = "http://vajralabs.com/insta";
$server_ip = gethostbyname($hostname);

// final file url that is being uploaded
$file_upload_url = $server_ip . '/' . $target_path;


if (isset($_FILES['image']['name'])) {
    $target_path = $target_path . basename($_FILES['image']['name']);

    // reading other post parameters
    $description = isset($_POST['description']) ? $_POST['description'] : '';
    $uniqueid = uniqid();
    $user_mail = isset($_POST['user_mail']) ? $_POST['user_mail'] : '';
    
    //echo("unique id:" .$uniqueid . " image :". $_FILES['image']['name']);
    try {
        // Throws exception incase file is not being moved
        if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {
            // make error flag true
            
            $myObj->result = "error";
            $myObj->message = "Could not move the file!";
            $myJSON = json_encode($myObj);
            echo($myJSON);
        }else{

            $image_path = $file_upload_url . basename($_FILES['image']['name']);
    	    $qury = "INSERT INTO `insta_posts`(`post_id`,`user_mail`, `image`, `description`) VALUES ('$uniqueid', '$user_mail','$image_path','$description')";
    	   // echo($qury);
            $result_stock = mysql_query($qury);
            if ($result_stock) {
                $myObj->result = "success";
                $myObj->message = "SuccessFully Posted.";
                $myJSON = json_encode($myObj);
                echo($myJSON);
            } else {
                $myObj->result = "failed";
                $myObj->message =  die(mysql_error());
                $myJSON = json_encode($myObj);
                echo($myJSON);
            }
        }
    } catch (Exception $e) {
        // Exception occurred. Make error flag true
            $myObj->result = "failed";
            $myObj->message = $e->getMessage();
            $myJSON = json_encode($myObj);
            echo($myJSON);
    }
} else {
    $myObj->result = "failed";
    $myObj->message = "Not received any file!";
    $myJSON = json_encode($myObj);
    echo($myJSON);
    
}

?>


