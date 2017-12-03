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
	$username = $_POST['username'];
	$comment = $_POST['comment'];
	$uniqueid = uniqid();

    $result = mysql_query("INSERT INTO insta_post_comments(comment_id, post_id, user_email, username, comment) VALUES('$uniqueid', $post_id', '$user_email, '$username', '$comment')");

    if ($result) {
       $response["result"] = "success";
        $response["message"] = "Comments added Successfully.";
        //echo $output;
        // echoing JSON response
        echo json_encode($response);
    } else {
        $response["result"] = "failed";
        $response["message"] = die(mysql_error());
        echo json_encode($response);
    }

?>

